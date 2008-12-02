package net.jgf.example.tanks.entity;

import net.jgf.example.tanks.TanksSettings;
import net.jgf.example.tanks.logic.SpawnLogic;
import net.jgf.jme.audio.AudioItem;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.jme.view.CursorRenderView;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.intersection.BoundingCollisionResults;
import com.jme.intersection.CollisionResults;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public abstract class Tank extends SpatialEntity {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(PlayerTank.class);

	protected float topWalkSpeed = TanksSettings.PLAYER_WALK_SPEED;

	protected float topRotationSpeed = TanksSettings.PLAYER_ROTATE_SPEED;

	protected Vector3f direction = new Vector3f();

	protected float speedRel;

	protected float fireDelay = 0.200f;

	protected float fireWait;

	protected float accelRel = 5.0f;

	protected SpawnLogic spawnLogic;

	protected CursorRenderView cursorView;

	protected AudioItem audioItem;

	protected DefaultJmeScene scene;

	private boolean firing;

	private boolean mining;

	public Tank() {
		super();
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		super.load();
		spawnLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame/spawn", SpawnLogic.class);
		cursorView = Jgf.getDirectory().getObjectAs("view/root/level/cursor", CursorRenderView.class);
		audioItem = Jgf.getDirectory().getObjectAs("audio/shot", AudioItem.class);
		scene = Jgf.getDirectory().getObjectAs("scene", DefaultJmeScene.class);
	}

	@Override
	public void update(float tpf) {

		// TODO: Stop the tank when shooting?
		//if (fireWait > 0.1f) direction.set(0,0,0);

		Spatial hull = ((Node)((Node)spatial).getChild("Tank")).getChild("Hull");
		Vector3f orientation = hull.getWorldRotation().mult(Vector3f.UNIT_Y).normalizeLocal();

		float angle = orientation.angleBetween(direction.normalize());
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
					speedRel = FastMath.clamp(speedRel, -direction.length(), direction.length());
					throtling = true;
				}
			}

			// Rotate the tank towards the direction

			Vector3f rotDirection = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y).mult(direction.normalize());
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

		spatial.updateWorldVectors();

		// Fire
		Spatial canon = ((Node)((Node)spatial).getChild("Tank")).getChild("Canon");
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

}