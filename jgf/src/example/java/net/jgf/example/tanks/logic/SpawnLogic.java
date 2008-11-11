
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateUtil;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.jme.refs.SpatialReference;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.BaseLogicState;
import net.jgf.system.System;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;




/**
 */
@Configurable
public class SpawnLogic extends BaseLogicState {

	EntityPoolLoader entityLoader;

	EntityGroup playerEntityGroup;

	EntityGroup bulletEntityGroup;

	DefaultJmeScene scene;

	int bullets = 0;

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {
		super.load();
		entityLoader = System.getDirectory().getObjectAs("loader/entity/pool", EntityPoolLoader.class);
		playerEntityGroup = System.getDirectory().getObjectAs("entity/root/players", EntityGroup.class);
		bulletEntityGroup = System.getDirectory().getObjectAs("entity/root/bullets", EntityGroup.class);
		scene = System.getDirectory().getObjectAs("scene", DefaultJmeScene.class);
	}

	@Override
	public void update(float tpf) {
		// Nothing to do here
	}


	public PlayerTank spawnPlayer() {

		// Choose a Spawn Point
		// TODO: Choose an empty one

		Vector3f position = ((SpatialReference)scene.getReferences().getReference("playerStart0")).getSpatial().getLocalTranslation();
		PlayerTank tank = (PlayerTank) entityLoader.load("FileChainLoader.resourceUrl=tanks/entity/tank.xml");
		tank.setId("player1");
		Spatial hull = ((Node)((Node)tank.getSpatial()).getChild("Tank")).getChild("Hull");
		// TODO: Model Bounds don't quite belong to logic...
		// maybe to the entity or better yet, to loader
		BoundingBox obb = new BoundingBox();
		hull.setModelBound(obb);
		hull.updateModelBound();

		tank.integrate(playerEntityGroup, scene.getRootNode(), position);
		scene.getRootNode().updateRenderState();

		return tank;
	}

	public Bullet spawnBullet(Vector3f position, Quaternion orientation) {

		// Choose a Spawn Point
		// TODO: Choose an empty one

		Bullet bullet = (Bullet) entityLoader.load("FileChainLoader.resourceUrl=tanks/entity/bullet.xml");
		bullet.setId("bullet" + bullets++);

		// TODO: Model Bounds don't quite belong to logic...
		// maybe to the entity or better yet, to loader
		bullet.getSpatial().setModelBound(new BoundingSphere());
		bullet.getSpatial().updateModelBound();

		bullet.integrate(bulletEntityGroup, scene.getRootNode(), position);
		StateUtil.loadAndActivate(bullet);
		bullet.getSpatial().updateRenderState();

		Vector3f newPosition = position.clone();
		newPosition.addLocal(orientation.mult(Vector3f.UNIT_Z).normalizeLocal().multLocal(1.05f));
		newPosition.y = 0.6f;
		bullet.startFrom(newPosition, orientation.mult(Vector3f.UNIT_Z));

		return bullet;
	}

	public void destroyBullet(Bullet bullet) {

		bullet.exclude(bulletEntityGroup, scene.getRootNode());
		StateUtil.deactivateAndUnload(bullet);

		entityLoader.returnToPool(bullet);

	}

}
