
package net.jgf.example.tanks.entity;

import net.jgf.config.Configurable;
import net.jgf.example.tanks.TanksSettings;
import net.jgf.example.tanks.logic.SpawnLogic;
import net.jgf.jme.audio.AudioItem;
import net.jgf.jme.entity.SceneEntity;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.jme.view.CursorRenderView;
import net.jgf.system.System;

import org.apache.log4j.Logger;

import com.jme.input.MouseInput;
import com.jme.intersection.BoundingCollisionResults;
import com.jme.intersection.CollisionResults;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

/**
 */
@Configurable
public class PlayerTank extends SceneEntity {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(PlayerTank.class);

	private float topWalkSpeed = TanksSettings.PLAYER_WALK_SPEED;

	private float topRotationSpeed = TanksSettings.PLAYER_ROTATE_SPEED;

	private boolean walkLeft;

	private boolean walkRight;

	private boolean walkUp;

	private boolean walkDown;

	private Vector3f direction = new Vector3f();

	private float speedRel;

	private Vector3f target = new Vector3f();

	private boolean firing;

	private boolean mining;

	private float fireDelay = 0.200f;

	private float fireWait;

	private float accelRel = 5.0f;

	private SpawnLogic spawnLogic;

	private CursorRenderView cursorView;

	private AudioItem audioItem;

	private DefaultJmeScene scene;

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {
		// TODO Auto-generated method stub
		super.load();
		spawnLogic = System.getDirectory().getObjectAs("logic/root/ingame/spawn", SpawnLogic.class);
		cursorView = System.getDirectory().getObjectAs("view/root/level/cursor", CursorRenderView.class);
		audioItem = System.getDirectory().getObjectAs("audio/shot", AudioItem.class);
		scene = System.getDirectory().getObjectAs("scene", DefaultJmeScene.class);
	}

	@Override
	public void update(float tpf) {

		// Don't let cursor get too away
		Vector3f distanceToTarget = target.subtract(spatial.getLocalTranslation());
		if (distanceToTarget.length() > 15) {

			distanceToTarget.normalizeLocal().multLocal(15).addLocal(spatial.getLocalTranslation());
			Vector3f newPos = DisplaySystem.getDisplaySystem().getRenderer().getCamera().getScreenCoordinates(distanceToTarget);
			cursorView.getMouse().setLocalTranslation(newPos);
			MouseInput.get().setCursorPosition((int)newPos.x, (int)newPos.y);

		}


		direction.set(0, 0, 0);
		if (walkLeft) direction.x = -1;
		if (walkRight) direction.x = 1;
		if (walkUp) direction.z = -1;
		if (walkDown) direction.z = 1;
		direction.normalizeLocal();

		Spatial hull = ((Node)((Node)spatial).getChild("Tank")).getChild("Hull");
		Vector3f orientation = hull.getWorldRotation().mult(Vector3f.UNIT_Y).normalizeLocal();

		float angle = orientation.angleBetween(direction);
		float mirrorangle = angle > FastMath.HALF_PI ? FastMath.PI - angle : angle;
		float obtuse = angle > FastMath.HALF_PI ? -1.0f : 1.0f;

		boolean throtling = false;

		if (direction.length() > 0.01f) {

			// We want to move: check if we are oriented

			// TODO: Move this to a constant
			if (mirrorangle < ( (FastMath.abs(speedRel) > 0.1f ? 45.0f * FastMath.DEG_TO_RAD : 1.0f * FastMath.DEG_TO_RAD))) {
				// Adjust speed
				if ((obtuse >= 0 && speedRel >= 0) || (obtuse <= 0 && speedRel <= 0)) {
					speedRel = speedRel + (obtuse * accelRel * tpf);
					speedRel = FastMath.clamp(speedRel, -1, 1);
					throtling = true;
				}
			}

			// Rotate the tank towards the direction

			Vector3f rotDirection = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y).mult(direction);
			float anglePositive = orientation.angleBetween(rotDirection);
			float sign = obtuse * (anglePositive > FastMath.HALF_PI ? 1.0f : -1.0f);

			// This goes in local coordinates!
			float angleRotate = FastMath.clamp(topRotationSpeed * tpf, 0, mirrorangle);
			hull.getLocalRotation().multLocal(new Quaternion().fromAngleAxis(angleRotate * sign, Vector3f.UNIT_Z));

		}

		if (!throtling) {
			speedRel = speedRel + ((speedRel > 0 ? -1.0f : 1.0f) * FastMath.clamp(accelRel * 2 * tpf, 0, FastMath.abs(speedRel)));
		}

		// Actually move the tank
		//Vector3f lastPosition = spatial.getLocalTranslation().clone();
		spatial.getLocalTranslation().addLocal(orientation.normalize().mult(speedRel * topWalkSpeed * tpf));

		// Collisions
		CollisionResults results = new BoundingCollisionResults();
		hull.calculateCollisions(((Node)(scene.getRootNode().getChild("fieldNode"))).getChild("obstaclesNode"),  results);
		for (int i = 0 ; i < results.getNumber(); i++) {
			//spatial.setLocalTranslation(lastPosition);

			Vector3f targetPos = results.getCollisionData(i).getTargetMesh().getWorldBound().getCenter().clone();
			Vector3f sourcePos = results.getCollisionData(i).getSourceMesh().getWorldBound().getCenter().clone();
			targetPos.y = sourcePos.y = 0;
			Vector3f diff = sourcePos.subtract(targetPos);
			diff.y = 0;
			diff.normalizeLocal();

			spatial.getLocalTranslation().addLocal(diff.mult(topWalkSpeed * tpf));
			spatial.getLocalTranslation().y = 0;
		}

		// Canon
		Spatial canon = ((Node)((Node)spatial).getChild("Tank")).getChild("Canon");
		target.setY(canon.getLocalTranslation().y);
		canon.lookAt(target, Vector3f.UNIT_Y);

		spatial.updateWorldVectors();

		// Fire
		if (fireWait < 0) {
			if (firing) {
				fireWait = fireDelay;

				// Spawn bullet
				// TODO: Should this be an action????
				spawnLogic.spawnBullet(canon.getWorldTranslation().clone(), canon.getWorldRotation().clone());
				audioItem.play();

				scene.getRootNode().updateRenderState();
				firing = false;
			}
		} else {
			fireWait = fireWait - tpf;
		}
	}

	/**
	 * @return the walkLeft
	 */
	public boolean isWalkLeft() {
		return walkLeft;
	}

	/**
	 * @param walkLeft the walkLeft to set
	 */
	public void setWalkLeft(boolean walkLeft) {
		this.walkLeft = walkLeft;
	}

	/**
	 * @return the walkRight
	 */
	public boolean isWalkRight() {
		return walkRight;
	}

	/**
	 * @param walkRight the walkRight to set
	 */
	public void setWalkRight(boolean walkRight) {
		this.walkRight = walkRight;
	}

	/**
	 * @return the walkUp
	 */
	public boolean isWalkUp() {
		return walkUp;
	}

	/**
	 * @param walkUp the walkUp to set
	 */
	public void setWalkUp(boolean walkUp) {
		this.walkUp = walkUp;
	}

	/**
	 * @return the walkDown
	 */
	public boolean isWalkDown() {
		return walkDown;
	}

	/**
	 * @param walkDown the walkDown to set
	 */
	public void setWalkDown(boolean walkDown) {
		this.walkDown = walkDown;
	}

	/**
	 * @return the firing
	 */
	public boolean isFiring() {
		return firing;
	}

	/**
	 * @param firing the firing to set
	 */
	public void setFiring(boolean firing) {
		this.firing = firing;
	}

	/**
	 * @return the mining
	 */
	public boolean isMining() {
		return mining;
	}

	/**
	 * @param mining the mining to set
	 */
	public void setMining(boolean mining) {
		this.mining = mining;
	}

	/**
	 * @return the target
	 */
	public Vector3f getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Vector3f target) {
		this.target = target;
	}



}
