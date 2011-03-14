
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.example.tanks.entity.Tank;
import net.jgf.example.tanks.logic.network.ServerNetworkLogic;
import net.jgf.example.tanks.messages.BulletMessage;
import net.jgf.example.tanks.view.EffectsView;
import net.jgf.jme.model.util.TransientSavable;
import net.jgf.jme.refs.SpatialReference;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.BaseLogicState;

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


    public enum PlayerSpawn {
        PLAYER1,
        PLAYER2,
        MULTIPLAYER
    }
    
    @Register (ref = "loader/entity/pool")
	protected EntityPoolLoader entityLoader;

    @Register (ref = "entity/root/tanks")
	protected EntityGroup tanks;

    @Register (ref = "entity/root/bullets")
	protected EntityGroup bullets;

    @Register (ref = "entity/root/enemies")
	protected EntityGroup enemies;
    
    @Register (ref = "logic/root/network/server")
    protected ServerNetworkLogic serverLogic;

    @Register (ref = "scene")
	protected DefaultJmeScene scene;

	@Register (ref = "view/root/scene/effects")
	protected EffectsView effectsView;

	int bulletCount = 0;

	/* (non-Javadoc)
     * @see net.jgf.core.state.State#load()
     */
    @Override
    public void doLoad() {
        super.doLoad();
    }
	
	@Override
	public void doUpdate(float tpf) {
		// Nothing to do here
	}

	public PlayerTank spawnTank(String id, PlayerSpawn spawnType) {

		// Choose a Spawn Point
		// TODO: Choose an empty one

		Vector3f position = null;
		
		if (spawnType == PlayerSpawn.PLAYER1) {
		    position = ((SpatialReference)scene.getReferences().getReference("z")).getSpatial().getLocalTranslation();
		} else if (spawnType == PlayerSpawn.PLAYER2){
		    position = ((SpatialReference)scene.getReferences().getReference("y")).getSpatial().getLocalTranslation();
		} else if (spawnType == PlayerSpawn.MULTIPLAYER) {
		    position = ((SpatialReference)scene.getReferences().getReference("x")).getSpatial().getLocalTranslation();
		}
		
		Tank tank = (Tank) entityLoader.load(null, "FileChainLoader.resourceUrl=tanks/entity/tank.xml");
		tank.setId(id);
		Spatial hull = ((Node)((Node)tank.getSpatial()).getChild("Tank")).getChild("Hull");
		// TODO: Model Bounds don't quite belong to logic...
		// maybe to the entity or better yet, to loader
		BoundingBox obb = new BoundingBox();
		hull.setModelBound(obb);
		hull.updateModelBound();

		tank.integrate(tanks, scene.getRootNode(), position);
		scene.getRootNode().updateRenderState();
		
		StateHelper.loadAndActivate(tank);

		return (PlayerTank) tank;
	}

	public Bullet spawnBullet(Vector3f position, Vector3f orientation) {

		// Choose a Spawn Point
		// TODO: Choose an empty one

		Bullet bullet = (Bullet) entityLoader.load(null, "FileChainLoader.resourceUrl=tanks/entity/bullet.xml");
		bullet.setId("!entity/root/bullets/bullet" + bulletCount++);
		bullet.clearStateObservers();

		bullet.getSpatial().setModelBound(new BoundingSphere());
		bullet.getSpatial().updateModelBound();

	    Node bulletNode = (Node) scene.getRootNode().getChild("bullets");
		bullet.integrate(bullets, bulletNode, position);
		bullet.getSpatial().setUserData("entity", new TransientSavable<Entity>(bullet));

		StateHelper.loadAndActivate(bullet);
		//bullet.getSpatial().updateRenderState();
		scene.getRootNode().updateRenderState();

		Vector3f newPosition = position.clone();
		newPosition.addLocal(orientation.normalizeLocal().multLocal(1.10f));
		newPosition.y = 0.6f;
		bullet.startFrom(newPosition, orientation);

		effectsView.addBullet(bullet);
		
		StateHelper.loadAndActivate(bullet);
		
		if (serverLogic.isActive()) serverLogic.sendBullet(null, bullet);

		return bullet;
	}

	public void destroyBullet(Bullet bullet) {

	    Node bulletNode = (Node) scene.getRootNode().getChild("bullets");
		bullet.withdraw(bullets, bulletNode);
		effectsView.addExplosion(bullet.getSpatial().getWorldTranslation(), EffectsView.EXPLOSION_BULLET_TTL);

		StateHelper.deactivateAndUnload(bullet);
		bullet.clearStateObservers();
		
		if (serverLogic.isActive()) serverLogic.sendBullet(null, bullet);

		entityLoader.returnToPool(bullet);

	}

	public void destroyTank(Tank tank, Bullet bullet) {

		if (tanks.containsChild(tank)) {
			tank.withdraw(tanks, scene.getRootNode());
		} else {
			// Increase points
		    if (bullet != null) {
    			if (bullet.getOwner() instanceof PlayerTank) {
    				PlayerTank player = (PlayerTank) bullet.getOwner();
    				player.setKills(player.getKills() + 1);
    			}
		    }
			tank.withdraw(enemies, scene.getRootNode());
		}
		effectsView.addExplosion(tank.getSpatial().getWorldTranslation(), EffectsView.EXPLOSION_TANK_TTL);
		StateHelper.deactivateAndUnload(tank);
		if (serverLogic.isActive()) serverLogic.sendTank(null, tank, true);

	}

}
