package net.jgf.example.tanks.entity;

import net.jgf.core.state.StateLifecycleEvent;
import net.jgf.core.state.StateObserver;
import net.jgf.core.state.StateLifecycleEvent.LifecycleEventType;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.TanksSettings;
import net.jgf.example.tanks.logic.SpawnLogic;
import net.jgf.jme.audio.AudioItem;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.jme.model.util.TransientSavable;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.intersection.CollisionData;
import com.jme.intersection.CollisionResults;
import com.jme.intersection.TriangleCollisionResults;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public abstract class Tank extends SpatialEntity implements StateObserver {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PlayerTank.class);

	protected float topWalkSpeed = TanksSettings.PLAYER_WALK_SPEED;

	protected float topRotationSpeed = TanksSettings.PLAYER_ROTATE_SPEED;

	protected final Vector3f direction = new Vector3f();

	protected float speedRel;

	protected float fireDelay = 0.250f;

	protected float fireHold;

	protected float accelRel = 5.0f;

	protected SpawnLogic spawnLogic;

	//protected CursorRenderView cursorView;

	protected AudioItem audioItem;

	protected DefaultJmeScene scene;

	private boolean firing;

	private boolean mining;

	protected final Vector3f target = new Vector3f();

	private final CollisionResults bulletResults = new TriangleCollisionResults();

	private final CollisionResults obstaclesResults = new TriangleCollisionResults();
	
	private EntityGroup enemyEntities;
	
	private int simultaneousBullets = 5;
	
	private int currentBullets = 0;

	Spatial hull;

	Spatial canon;

	public Tank() {
		super();
	}

	/**
	 * Loads this entity.
	 */
	@Override
	public void load() {

		super.load();

		spawnLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame/spawn", SpawnLogic.class);
		//cursorView = Jgf.getDirectory().getObjectAs("view/root/level/cursor", CursorRenderView.class);
		audioItem = Jgf.getDirectory().getObjectAs("audio/shot", AudioItem.class);
		scene = Jgf.getDirectory().getObjectAs("scene", DefaultJmeScene.class);
		
		enemyEntities = Jgf.getDirectory().getObjectAs("entity/root/enemy", EntityGroup.class);

		hull = ((Node)((Node)spatial).getChild("Tank")).getChild("Hull");
		canon = ((Node)((Node)spatial).getChild("Tank")).getChild("Canon");

		direction.zero();
	}

	/**
	 * Move the tank according to the controls
	 */
	protected void updateMovement(float tpf) {

		// TODO: Stop the tank when shooting?
		//if (fireHold > 0.1f) direction.set(0,0,0);

		Vector3f orientation = hull.getWorldRotation().mult(Vector3f.UNIT_Y).normalizeLocal();

		// The tank needs to rotate in the optimal way towards the desired direction
		// Here the shortest rotation is calculated
		final float angle = orientation.angleBetween(direction.normalize());
		final float mirrorangle = angle > FastMath.HALF_PI ? FastMath.PI - angle : angle;
		final float obtuse = angle > FastMath.HALF_PI ? -1.0f : 1.0f;

		// If the controls are moving in some direction...
		boolean throttling = false;
		if (direction.length() > 0.01f) {

			// Calculate the new normalized speed and find out if we are accelerating
			// (as the tank may be rotating only)
			// TODO: Move this to a constant
			if (mirrorangle < ( (FastMath.abs(speedRel) > 0.1f ? 45.0f * FastMath.DEG_TO_RAD : 1.0f * FastMath.DEG_TO_RAD))) {
				// Adjust speed
				if ((obtuse >= 0 && speedRel >= 0) || (obtuse <= 0 && speedRel <= 0)) {
					speedRel = speedRel + (obtuse * accelRel * tpf);
					speedRel = FastMath.clamp(speedRel, -direction.length(), direction.length());
					throttling = true;
				}
			}

			// Rotate the tank towards the direction
			Vector3f rotDirection = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y).mult(direction.normalize());
			float anglePositive = orientation.angleBetween(rotDirection);
			float sign = obtuse * (anglePositive > FastMath.HALF_PI ? 1.0f : -1.0f);

			// This goes in local coordinates, because we are rotating the hull, not the tank node!
			// This separation of hull/tank is done to avoid rotating the turret when rotating the tank root node
			float angleRotate = FastMath.clamp(topRotationSpeed * tpf, 0, mirrorangle);
			hull.getLocalRotation().multLocal(new Quaternion().fromAngleAxis(angleRotate * sign, Vector3f.UNIT_Z));

		}

		// Now, if we are actually moving, we update the speedRel and then the spatial
		// Note that we do move the whole spatial, as both hull and turret need to be moved
		if (!throttling) {
			speedRel = speedRel + ((speedRel > 0 ? -1.0f : 1.0f) * FastMath.clamp(accelRel * 2 * tpf, 0, FastMath.abs(speedRel)));
		}
		spatial.getLocalTranslation().addLocal(orientation.normalize().mult(speedRel * topWalkSpeed * tpf));

	}

	/**
	 * 
	 */
	protected void updateCollisionsTanks(float tpf) {
		
		for (Entity entity : enemyEntities.children()) {
			Tank tank = (Tank) entity;
			obstaclesResults.clear();
			hull.calculateCollisions(tank.hull, obstaclesResults);
			for (int i = 0 ; i < obstaclesResults.getNumber(); i++) {

				CollisionData data = obstaclesResults.getCollisionData(i);
				if ((data.getTargetTris().size() > 0)||(data.getSourceTris().size() > 0)) {
					
					Vector3f targetPos = obstaclesResults.getCollisionData(i).getTargetMesh().getWorldBound().getCenter().clone();
					Vector3f sourcePos = obstaclesResults.getCollisionData(i).getSourceMesh().getWorldBound().getCenter().clone();
					targetPos.y = sourcePos.y = 0;
					Vector3f diff = targetPos.subtract(sourcePos);
					diff.y = 0;
					diff.normalizeLocal();

					tank.getSpatial().getLocalTranslation().addLocal(diff.mult(topWalkSpeed * tpf));
					tank.getSpatial().getLocalTranslation().y = 0;
				}
			}

			tank.getSpatial().updateWorldVectors();
		}
		
		
	}
	
	/**
	 * Calculate collisions with the obstacles node of the scene
	 */
	protected void updateCollisions(float tpf) {

		// Collisions with hull

		obstaclesResults.clear();
		hull.calculateCollisions(((Node)(scene.getRootNode().getChild("fieldNode"))).getChild("obstaclesNode"),  obstaclesResults);
		for (int i = 0 ; i < obstaclesResults.getNumber(); i++) {
			//spatial.setLocalTranslation(lastPosition);

			CollisionData data = obstaclesResults.getCollisionData(i);
			if ((data.getTargetTris().size() > 0)||(data.getSourceTris().size() > 0)) {
				
				/*
				int firstTriangleIndex = obstaclesResults.getCollisionData(i).getTargetTris().get(0);
				Vector3f[] vec = new Vector3f[3];
				((TriMesh)(obstaclesResults.getCollisionData(i).getTargetMesh())).getTriangle(firstTriangleIndex, vec);
				Vector3f normal = 
				*/
				
				Vector3f targetPos = obstaclesResults.getCollisionData(i).getTargetMesh().getWorldBound().getCenter().clone();
				Vector3f sourcePos = obstaclesResults.getCollisionData(i).getSourceMesh().getWorldBound().getCenter().clone();
				targetPos.y = sourcePos.y = 0;
				Vector3f diff = sourcePos.subtract(targetPos);
				diff.y = 0;
				diff.normalizeLocal();

				spatial.getLocalTranslation().addLocal(diff.mult(topWalkSpeed * tpf));
				spatial.getLocalTranslation().y = 0;
			}
		}

		spatial.updateWorldVectors();

	}

	/**
	 * Shoots a bullet from this tank. The bullet will not be shot if the cannon has recently
	 * been used (as defined by the fireHold variable).
	 * @return
	 */
	protected boolean fire() {
		// Spawn bullet
		// We don't contain logic to spawn other entities, as they could live longer than this entity
		// An additional logic class is used
		// TODO: On networked game this is likely to change
		
		if ((fireHold < 0) && (currentBullets < simultaneousBullets)) {

			Bullet bullet = spawnLogic.spawnBullet(canon.getWorldTranslation().clone(), canon.getWorldRotation().clone());
			bullet.setOwner(this);

			audioItem.play();
			fireHold = fireDelay;
			
			bullet.addStateObserver(this);
			currentBullets++;
			
			return true;
		} else {
			return false;
		}

	}

	
	
	/**
	 * This listens for bullet deactivation, and increments the number
	 * of bullets available for this tank.
	 */
	@Override
	public void onStateLifecycle(StateLifecycleEvent evt) {
		if (evt.getType() == LifecycleEventType.Deactivate) {
			currentBullets--;
		}
	}

	/**
	 * Updates the tank weapons, according to the controllers (firing)
	 * @param tpf
	 */
	protected void updateWeapons(float tpf) {

		fireHold = fireHold - tpf;
		if (firing) {
			fire();
		}
		firing = false;

	}

	/**
	 * Updates the canon, so it looks at the target
	 * @param tpf
	 */
	protected void updateCanon (float tpf) {
		// Canon
		Spatial canon = ((Node)((Node)spatial).getChild("Tank")).getChild("Canon");
		target.setY(canon.getLocalTranslation().y);
		canon.lookAt(target, Vector3f.UNIT_Y);
	}

	/**
	 */
	protected void updateHits (float tpf) {
		// Check collisions

		canon = ((Node)((Node)spatial).getChild("Tank")).getChild("Canon");
		bulletResults.clear();
		canon.calculateCollisions(scene.getRootNode().getChild("bullets"), bulletResults);

		if (bulletResults.getNumber() > 0) {
			// Impacted by bullet. Destroy tank and bullet.
			CollisionData data = bulletResults.getCollisionData(0);
			// If the bounding is hit, we check tris
			if (data.getTargetTris().size() > 0) {
				Spatial hitGeom = data.getTargetMesh();
				for (; (hitGeom != null) && (hitGeom.getUserData("entity") == null); hitGeom = hitGeom.getParent());

				if (hitGeom != null) {
					Bullet bullet = (Bullet) ((TransientSavable<Entity>) hitGeom.getUserData("entity")).getContent();

					// Destroy tank
					spawnLogic.destroyTank(this, bullet);
					// Destroy bullet
					spawnLogic.destroyBullet(bullet);

				}
			}

		}

	}

	/**
	 * Updates the tank. This is called every frame.
	 */
	@Override
	public void update(float tpf) {

		updateMovement(tpf);

		updateCollisionsTanks(tpf);
		
		updateCollisions(tpf);

		updateHits(tpf);

		updateWeapons(tpf);

		updateCanon(tpf);

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
	 * @return the direction
	 */
	public Vector3f getDirection() {
		return direction;
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
		this.target.set(target);
	}

}