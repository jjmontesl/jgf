package net.jgf.example.tanks.view;

import java.util.ArrayList;
import java.util.Iterator;

import net.jgf.config.Configurable;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.jme.model.util.ModelUtil;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.BaseLoader;
import net.jgf.loader.Loader;
import net.jgf.system.Jgf;
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

	private static final float EXPLOSION_TTL = 0.9f;

	private class SmokeEffect {
		public ParticlePoints particles = null;
		public SpatialEntity entity = null;
		public float ttl = 3.0f;
	}

	private class ExplosionEffect {
		public Node node = null;
		public Quaternion rotation = null;
		public ColorRGBA fadeColor = null;
		public float ttl = EXPLOSION_TTL;
	}

	private DefaultJmeScene scene;

	private ArrayList<SmokeEffect> smokes = new ArrayList<SmokeEffect>(60);

	private ArrayList<ExplosionEffect> explosions = new ArrayList<ExplosionEffect>(30);

	private Loader<Node> loader;

	/*
	 * (non-Javadoc)
	 *
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {
		super.load();
		loader = Jgf.getDirectory().getObjectAs("loader/model/tanks", Loader.class);
	}



	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#unload()
	 */
	@Override
	public void unload() {
		super.unload();
		// TODO: Unregister object from directory, do the same to cleanup other states
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see net.jgf.core.state.BaseState#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		scene = Jgf.getDirectory().getObjectAs("scene", DefaultJmeScene.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void update(float tpf) {
		super.update(tpf);

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
					scene.getRootNode().detachChild(smoke.particles);
					iterator.remove();
				}
			}
		}

		// Update explosions
		for (Iterator<ExplosionEffect> iterator = explosions.iterator(); iterator.hasNext();) {
			ExplosionEffect explosion = iterator.next();
			explosion.ttl -= tpf;
			if (explosion.ttl > 0) {
				float scaleTpf = tpf * ((explosion.ttl) * (explosion.ttl) * 1.0f);
				explosion.node.getLocalScale().addLocal(scaleTpf, scaleTpf, scaleTpf);

				explosion.node.getLocalRotation().multLocal(explosion.rotation);

				explosion.fadeColor.a = (explosion.ttl) / EXPLOSION_TTL;

				explosion.node.updateRenderState();

			} else {
				scene.getRootNode().detachChild(explosion.node);
				iterator.remove();
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
		scene.getRootNode().attachChild(smokeParticles);
		scene.getRootNode().updateRenderState();

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

	public void addExplosion(Vector3f location) {

		Node node = (Node)((BaseLoader) loader).load(null, "ConverterLoader.resourceUrl=tanks/model/explosion/explosion.dae");
		node.getLocalTranslation().set(location);
		node.getLocalScale().set(0.2f, 0.2f, 0.2f);
		BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();

		bs.setBlendEnabled(true);
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.One);
		bs.setBlendEquation(BlendEquation.Add);
		bs.setEnabled(true);

		TriMesh mesh = (TriMesh) ModelUtil.findChild(node, "Sphere_001-explosion_jpg");
		ColorRGBA fadecolor = new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);
		mesh.setDefaultColor(fadecolor);

		MaterialState ms = (MaterialState) mesh.getRenderState(RenderState.RS_MATERIAL);
		ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
		//ms.getSpecular().set(0,0,0,0);
		//ms.setEnabled(false);

		mesh.setRenderState(bs);
		node.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		mesh.updateRenderState();

		mesh.setRenderState(((DefaultJmeScene)scene).getCommonRenderStates().get("zBufferReadOnly"));

		// TODO: Don't put explosions in the root node
		scene.getRootNode().attachChild(node);
		scene.getRootNode().updateRenderState();

		ExplosionEffect explosion = new ExplosionEffect();
		explosion.node = node;
		explosion.rotation = new Quaternion();
		explosion.fadeColor = fadecolor;
		// TODO: Optimize
		explosion.rotation.fromAngleAxis(FastMath.DEG_TO_RAD * 3.0f * FastMath.nextRandomFloat(),
				new Vector3f(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat()).normalizeLocal());


		explosions.add(explosion);
	}

}
