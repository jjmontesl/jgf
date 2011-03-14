package net.jgf.example.tanks.view;

import java.util.ArrayList;
import java.util.Iterator;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Explosion;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.jme.model.util.ModelUtil;
import net.jgf.jme.model.util.TransientSavable;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.BaseLoader;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.BlendState.BlendEquation;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.scene.state.MaterialState.ColorMaterial;
import com.jme.scene.state.ZBufferState.TestFunction;
import com.jme.system.DisplaySystem;
import com.jmex.effects.particles.ParticleController;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticlePoints;

/**
 */
@Configurable
public class EffectsView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(EffectsView.class);

	public static final float EXPLOSION_BULLET_TTL = 0.8f;

	public static final float EXPLOSION_TANK_TTL = 1.7f;

	private class SmokeEffect {
		public ParticlePoints particles = null;
		public SpatialEntity entity = null;
		public float ttl = 3.0f;
	}

	@Register (ref = "scene")
	private DefaultJmeScene scene;
	
	@Register (ref = "entity/root/explosions")
	private EntityGroup explosionsGroup;
	
	@Register (ref = "loader/entity/pool")
    private EntityPoolLoader entityLoader;

	private Node smokesNode;

	private ArrayList<SmokeEffect> smokes = new ArrayList<SmokeEffect>(60);

	/*
	 * (non-Javadoc)
	 *
	 * @see net.jgf.core.state.State#load()
	 */
	@Override
	public void doLoad() {
		super.doLoad();
		smokesNode = new Node("smokes");
		StateHelper.loadAndActivate(explosionsGroup);
		
	    entityLoader.preload(40, "FileChainLoader.resourceUrl=tanks/entity/explosion.xml");
	}



	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#unload()
	 */
	@Override
	public void doUnload() {
		smokesNode = null;
	    super.doUnload();
		// TODO: Unregister object from directory, do the same to cleanup other states
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see net.jgf.core.state.State#activate()
	 */
	@Override
	public void doActivate() {
		super.doActivate();
		scene.getRootNode().attachChild(smokesNode);
	}

	
	
	@Override
    public void doDeactivate() {
        super.doDeactivate();
        scene.getRootNode().detachChild(smokesNode);
    }



    /*
	 * (non-Javadoc)
	 *
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void doUpdate(float tpf) {
		super.doUpdate(tpf);
		
		// TODO: This shouldn't be here, it's not an effect
		scene.getRootNode().sortLights();

		// Update smokes
		for (Iterator<SmokeEffect> iterator = smokes.iterator(); iterator.hasNext();) {
			SmokeEffect smoke = iterator.next();
			if (smoke.entity != null) {
				if (smoke.entity.isActive()) {
					smoke.particles.setOriginOffset(smoke.entity.getSpatial().getWorldTranslation());
					// TODO: Optimize
					smoke.particles.setEmissionDirection(smoke.entity.getSpatial().getWorldRotation().mult(Vector3f.UNIT_Z));
				} else {
					smoke.entity = null;
					smoke.particles.getParticleController().setRepeatType(ParticleController.RT_CLAMP);
				}
			} else {
				smoke.ttl -= tpf;
				if (smoke.ttl < 0) {
					// TODO: This shouldn't be in the root node
					smokesNode.detachChild(smoke.particles);
					iterator.remove();
				}
			}
		}

	}

	protected ParticlePoints createSmoke() {

		// Particles
		ParticlePoints pPoints = ParticleFactory.buildPointParticles("particles",
				30);
		pPoints.setPointSize(8);
		pPoints.setAntialiased(true);
		pPoints.setOriginOffset(new Vector3f(0, 0, 0));
		pPoints.setInitialVelocity(-0.0005f);
		pPoints.setStartSize(0.5f);
		pPoints.setEndSize(5.5f);
		pPoints.setMinimumLifeTime(400f);
		pPoints.setMaximumLifeTime(600f);
		pPoints.setStartColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 0.5f));
		pPoints.setEndColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 0));
		pPoints.setMaximumAngle(2f * FastMath.DEG_TO_RAD);
		pPoints.getParticleController().setControlFlow(true);
		pPoints.warmUp(15);

		BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		as1.setBlendEnabled(true);
		as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		as1
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		as1.setEnabled(true);
		pPoints.setRenderState(as1);

		ZBufferState zstate = DisplaySystem.getDisplaySystem().getRenderer()
				.createZBufferState();
		zstate.setEnabled(true);
		zstate.setWritable(false);
		zstate.setFunction(TestFunction.LessThanOrEqualTo);
		pPoints.setRenderState(zstate);

		// pPoints.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT); // Slows down and doesn't effect

		// TODO: Drawing always!
		pPoints.setCullHint(CullHint.Never);

		return pPoints;
	}

	public void addBullet(SpatialEntity bullet) {

		// TODO: Pool SmokeEffects for performance
		ParticlePoints smokeParticles = createSmoke();
		// TODO: This shouldn't be in the root node
		smokesNode.attachChild(smokeParticles);
		smokesNode.updateRenderState();

		smokeParticles.getParticleController().setRepeatType(ParticleController.RT_WRAP);
		smokeParticles.getParticleController().setActive(true);

		SmokeEffect smoke = new SmokeEffect();
		smoke.particles = smokeParticles;
		smoke.entity = bullet;

		smokes.add(smoke);

	}

	public void addBounce() {

	}

	public void addCollision() {

	}

	public void addExplosion(Vector3f location, float factor) {

	    Explosion explosion = (Explosion) entityLoader.load(null, "FileChainLoader.resourceUrl=tanks/entity/explosion.xml");
        explosion.setId("!entity/root/explosions/explosion" + explosionsGroup.children().size());
        explosion.clearStateObservers();

	    // TODO: Don't put them into the rootNode()
	    Node explosionNode = (Node) scene.getRootNode();
	    explosion.integrate(explosionsGroup, explosionNode);
	    
		explosion.getSpatial().getLocalTranslation().set(location);
		explosion.getSpatial().getLocalScale().set(0.2f, 0.2f, 0.2f);

		explosion.rotation = new Quaternion();
		explosion.ttl = factor;
		explosion.initialTtl = factor;
		// TODO: Optimize
		explosion.rotation.fromAngleAxis(FastMath.DEG_TO_RAD * 3.0f * FastMath.nextRandomFloat(),
				new Vector3f(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat()).normalizeLocal());

		StateHelper.loadAndActivate(explosion);
		
	}

}
