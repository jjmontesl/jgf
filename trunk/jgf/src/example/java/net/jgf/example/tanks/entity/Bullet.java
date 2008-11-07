package net.jgf.example.tanks.entity;

import java.util.ArrayList;

import net.jgf.config.Configurable;
import net.jgf.example.tanks.TanksSettings;
import net.jgf.example.tanks.logic.SpawnLogic;
import net.jgf.jme.entity.SceneEntity;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.system.System;

import org.apache.log4j.Logger;

import com.jme.intersection.PickResults;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.FastMath;
import com.jme.math.LineSegment;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;

/**
 */
@Configurable
public class Bullet extends SceneEntity {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(Bullet.class);

	public static final float BULLET_TTL = 30.0f;

	public static final float BULLET_HALFWIDTH = 0.08f;

	private float ttl;

	private Vector3f speed = new Vector3f();

	private DefaultJmeScene scene;

	private SpawnLogic spawnLogic;

	private LineSegment segment;

	private Vector3f nextDir = new Vector3f();

	private Vector3f nextPos = new Vector3f();

	private int numBounces = 0;

	private int maxBounces = 1;

	//PickResults results = new BoundingPickResults();
	PickResults results = new TrianglePickResults();

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {
		super.load();
		scene = System.getDirectory().getObjectAs("scene", DefaultJmeScene.class);
		spawnLogic = System.getDirectory().getObjectAs("logic/root/ingame/spawn", SpawnLogic.class);
		numBounces = 0;
		ttl = BULLET_TTL;
	}

	public void startFrom(Vector3f position, Vector3f direction) {

		spatial.getLocalTranslation().set(position.clone());
		spatial.getLocalRotation().lookAt(direction, Vector3f.UNIT_Y);
		spatial.getLocalRotation().multLocal(new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y));
		//spatial.getLocalScale().set(20,20,20);
		speed = direction.normalizeLocal().mult(TanksSettings.BULLET_SPEED);

		// Check collision
		Ray ray = new Ray(position.clone(), speed.normalize				());
		Node obstacles = (Node)((Node)(scene.getRootNode().getChild("fieldNode"))).getChild("obstaclesNode");
		results.clear();
		segment = null;
		results.setCheckDistance(true);
		obstacles.findPick(ray, results);
		Triangle triangle = null;

		if (results.getNumber() > 0) {

			ArrayList<Integer> tris = results.getPickData(0).getTargetTris();
			Geometry geom = results.getPickData(0).getTargetMesh();

			TriMesh mesh = ((TriMesh) geom);

      for (int i = 0; i < 1 /* tris.size() */; i++) {

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

			Vector3f exactHit = new Vector3f();
			ray.intersectWhere(triangle, exactHit);

			segment = new LineSegment(position.clone(), speed.normalize(),
										exactHit.subtract(position).length() - BULLET_HALFWIDTH);
			nextDir = speed.clone().negateLocal().normalizeLocal();
			nextPos = exactHit.add(nextDir.mult(BULLET_HALFWIDTH));
			Quaternion q = new Quaternion();
			triangle.calculateNormal();
			q.fromAngleAxis(FastMath.PI, triangle.getNormal());
			q.mult(nextDir.clone(), nextDir);


		}






	}

	@Override
	public void update(float tpf) {


		// Actually move the tank
		spatial.getLocalTranslation().addLocal(speed.mult(tpf));
		spatial.updateWorldVectors();

		// Check position

		if (segment != null)  {

			Vector3f loc = spatial.getWorldTranslation();
			Vector3f neg = segment.getNegativeEnd(new Vector3f());
			Vector3f pos = segment.getPositiveEnd(new Vector3f());

			// TODO: Why this correction? Why negative and positive don't work as expected?
	 		if (neg.x > pos.x) { float t = neg.x; neg.x = pos.x; pos.x = t; }
	 		if (neg.y > pos.y) { float t = neg.y; neg.y = pos.y; pos.y = t; }
	 		if (neg.z > pos.z) { float t = neg.z; neg.z = pos.z; pos.z = t; }

			if ((loc.x < neg.x - 0.00001f || loc.x > pos.x + 0.00001f) ||
					(loc.y < neg.y - 0.00001f || loc.y > pos.y + 0.00001f) ||
					(loc.z < neg.z - 0.00001f || loc.z > pos.z + 0.00001f)) {

				if (numBounces < maxBounces) {
					numBounces ++;
					startFrom(nextPos.clone(), nextDir.clone());
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
		}


	}


}
