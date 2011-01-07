package net.jgf.example.tanks.entity;

import java.util.ArrayList;

import net.jgf.config.Configurable;
import net.jgf.entity.Entity;
import net.jgf.example.tanks.logic.SpawnLogic;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.jme.model.util.TransientSavable;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.intersection.BoundingCollisionResults;
import com.jme.intersection.CollisionData;
import com.jme.intersection.CollisionResults;
import com.jme.intersection.PickResults;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;

/**
 */
@Configurable
public class Bullet extends SpatialEntity {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Bullet.class);

	public final static float BULLET_SPEED = 3.3f;

	public static final float BULLET_TTL = 30.0f;

	public static final float BULLET_HALFWIDTH = 0.08f;

	private float ttl;

	private final Vector3f speed = new Vector3f();

	private DefaultJmeScene scene;

	private SpawnLogic spawnLogic;

	private int numBounces = 0;

	private int maxBounces = 1;

	private Entity owner;

	//PickResults pickResults = new BoundingPickResults();
	private final PickResults pickResults = new TrianglePickResults();

	private final ProjectileTrip trip = new ProjectileTrip();

	private final CollisionResults bulletResults = new BoundingCollisionResults();



	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#load()
	 */
	@Override
	public void doLoad() {
		super.doLoad();
		scene = Jgf.getDirectory().getObjectAs("scene", DefaultJmeScene.class);
		spawnLogic = Jgf.getDirectory().getObjectAs("logic/root/ingame/spawn", SpawnLogic.class);

		numBounces = 0;
		ttl = BULLET_TTL;
	}


	/**
	 * @return the trip
	 */
	public ProjectileTrip getTrip() {
		return trip;
	}




	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#unload()
	 */
	@Override
	public void doUnload() {
		super.doUnload();
		// TODO: Verify this is cleaning up correctly
		this.getSpatial().setUserData("entity", null);
	}

	
	@Override
	public void doActivate() {
		super.doActivate();
	}


	public void startFrom(Vector3f position, Vector3f direction) {

		spatial.getLocalTranslation().set(position.clone());
		spatial.getLocalRotation().lookAt(direction, Vector3f.UNIT_Y);
		spatial.getLocalRotation().multLocal(new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y));
		//spatial.getLocalScale().set(20,20,20);
		speed.set(direction).normalizeLocal().multLocal(BULLET_SPEED);

		// Check collision
		Ray ray = new Ray(position.clone(), speed.normalize());
		Node obstacles = (Node)((Node)(scene.getRootNode().getChild("fieldNode"))).getChild("obstaclesNode");
		pickResults.clear();
		pickResults.setCheckDistance(true);
		obstacles.findPick(ray, pickResults);
		Triangle triangle = null;

		if (pickResults.getNumber() > 0) {

			ArrayList<Integer> tris = pickResults.getPickData(0).getTargetTris();
			Geometry geom = pickResults.getPickData(0).getTargetMesh();

			TriMesh mesh = ((TriMesh) geom);

      for (int i = 0; i < /*1 */ (tris.size() > 0 ? 1 : 0); i++) {

        int triIndex = tris.get(i);
        Vector3f[] vec = new Vector3f[3];
        mesh.getTriangle(triIndex, vec);

        for (Vector3f v : vec) {
            v.multLocal(mesh.getWorldScale());
            mesh.getWorldRotation().mult(v, v);
            v.addLocal(mesh.getWorldTranslation());
        }

		triangle = new Triangle(vec[0], vec[1], vec[2]);

	  }
      
      if (tris.size() > 0) {

			Vector3f exactHit = new Vector3f();
			ray.intersectWhere(triangle, exactHit);

			trip.segment.getOrigin().set(position);
			trip.segment.getDirection().set(speed).normalizeLocal();
			trip.segment.setExtent(exactHit.subtract(position).length() - BULLET_HALFWIDTH); // FIXME: Objects created here
			trip.bounceDirection.set(speed).negateLocal().normalizeLocal(); // Temporal value, not bounce direction yet
			trip.hitPosition.set(exactHit).addLocal(trip.bounceDirection.mult(BULLET_HALFWIDTH));

			Quaternion q = new Quaternion();
			triangle.calculateNormal();
			q.fromAngleAxis(FastMath.PI, triangle.getNormal());
			q.mult(trip.bounceDirection.clone(), trip.bounceDirection); // Overwriting temporal value of bounceDirection

      		}
		}

		// We need to update this as bullet position will be immediately used in collisions: beware pooled entities!
		spatial.updateGeometricState(0, true);

	}

	@Override
	public void doUpdate(float tpf) {


		// Actually move the bullet
		spatial.getLocalTranslation().addLocal(speed.mult(tpf));
		spatial.updateWorldVectors();

		// Check position

		if (trip.segment != null)  {

			Vector3f loc = spatial.getWorldTranslation();

			if (! trip.segment.isPointInsideBounds(loc, 0.00001f)) {

				if (numBounces < maxBounces) {
					numBounces ++;
					startFrom(trip.hitPosition.clone(), trip.bounceDirection.clone());
				} else {
					// Destroy
					ttl = -1;
				}

			}

		}

		// Check time to live
		ttl -= tpf;
		if (ttl < 0) {
			spawnLogic.destroyBullet(this);
		} else {
			// Check collisions
			updateCollisions(tpf);
		}


	}

	protected void updateCollisions(float tpf) {

		// Check collisions with other bullets

		bulletResults.clear();
		this.getSpatial().calculateCollisions(scene.getRootNode().getChild("bullets"), bulletResults);

		if (bulletResults.getNumber() > 0) {
			// Impacted by bullet
			CollisionData data = bulletResults.getCollisionData(0);

			// Get the entity
			Spatial hitGeom = data.getTargetMesh();
			for (; (hitGeom != null) && (hitGeom.getUserData("entity") == null); hitGeom = hitGeom.getParent());

			if (hitGeom != null) {
				Bullet bullet = (Bullet) ((TransientSavable<Entity>) hitGeom.getUserData("entity")).getContent();

				// Destroy bullets
				spawnLogic.destroyBullet(this);
				spawnLogic.destroyBullet(bullet);

			}

		}

	}

	/**
	 * @return the owner
	 */
	public Entity getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Entity owner) {
		this.owner = owner;
	}


}
