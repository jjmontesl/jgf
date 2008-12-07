
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateUtil;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.Tank;
import net.jgf.example.tanks.view.EffectsView;
import net.jgf.jme.refs.SpatialReference;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.BaseLogicState;
import net.jgf.system.Jgf;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;




/**
 * This class is likely going to change once the game is networked
 */
@Configurable
public class SpawnLogic extends BaseLogicState {

	EntityPoolLoader entityLoader;

	EntityGroup playerEntityGroup;

	EntityGroup bulletEntityGroup;

	DefaultJmeScene scene;

	EffectsView effectsView;

	int bullets = 0;

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {
		super.load();
		entityLoader = Jgf.getDirectory().getObjectAs("loader/entity/pool", EntityPoolLoader.class);
		playerEntityGroup = Jgf.getDirectory().getObjectAs("entity/root/players", EntityGroup.class);
		bulletEntityGroup = Jgf.getDirectory().getObjectAs("entity/root/bullets", EntityGroup.class);
		effectsView = Jgf.getDirectory().getObjectAs("view/root/level/fight/effects", EffectsView.class);
		scene = Jgf.getDirectory().getObjectAs("scene", DefaultJmeScene.class);
	}

	@Override
	public void update(float tpf) {
		// Nothing to do here
	}


	public Tank spawnPlayer() {

		// Choose a Spawn Point
		// TODO: Choose an empty one

		Vector3f position = ((SpatialReference)scene.getReferences().getReference("z")).getSpatial().getLocalTranslation();
		Tank tank = (Tank) entityLoader.load(null, "FileChainLoader.resourceUrl=tanks/entity/tank.xml");
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

		Bullet bullet = (Bullet) entityLoader.load(null, "FileChainLoader.resourceUrl=tanks/entity/bullet.xml");
		bullet.setId("bullet" + bullets++);

		// TODO: Model Bounds don't quite belong to logic...
		// maybe to the entity or better yet, to loader
		bullet.getSpatial().setModelBound(new BoundingSphere());
		bullet.getSpatial().updateModelBound();

		bullet.integrate(bulletEntityGroup, scene.getRootNode(), position);
		StateUtil.loadAndActivate(bullet);
		//bullet.getSpatial().updateRenderState();
		scene.getRootNode().updateRenderState();

		Vector3f newPosition = position.clone();
		newPosition.addLocal(orientation.mult(Vector3f.UNIT_Z).normalizeLocal().multLocal(1.05f));
		newPosition.y = 0.6f;
		bullet.startFrom(newPosition, orientation.mult(Vector3f.UNIT_Z));

		effectsView.addBullet(bullet);

		return bullet;
	}

	public void destroyBullet(Bullet bullet) {

		bullet.withdraw(bulletEntityGroup, scene.getRootNode());
		StateUtil.deactivateAndUnload(bullet);

		effectsView.addExplosion(bullet.getSpatial().getWorldTranslation());

		entityLoader.returnToPool(bullet);

	}

}
