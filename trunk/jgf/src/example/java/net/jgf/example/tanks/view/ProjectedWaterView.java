
package net.jgf.example.tanks.view;

import net.jgf.config.Configurable;
import net.jgf.core.IllegalStateException;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.scene.SceneManager;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import com.jme.math.Plane;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.FogState;
import com.jme.system.DisplaySystem;
import com.jmex.effects.water.ProjectedGrid;
import com.jmex.effects.water.WaterHeightGenerator;
import com.jmex.effects.water.WaterRenderPass;

@Configurable
public class ProjectedWaterView extends BaseViewState {
    
	private WaterRenderPass waterEffectRenderPass;
    private ProjectedGrid projectedGrid;
    private float farPlane = 10000.0f;
    
    protected BasicPassManager pManager;
    
	DefaultJmeScene scene;
    
    Node rootNode;

    @Override
	public void unload() {
		super.unload();
        waterEffectRenderPass.cleanup();
	}

    @Override
    public void load() {
        
    	super.load();
    	
    	pManager = new BasicPassManager();
    	
    	rootNode = new Node("projectedWaterView");
    	
    	setupFog();

    	scene = (DefaultJmeScene) Jgf.getDirectory().getObjectAs("scene/manager", SceneManager.class).getScene();
    	

        waterEffectRenderPass = new WaterRenderPass(DisplaySystem.getDisplaySystem().getRenderer().getCamera(), 4, true, true);
        waterEffectRenderPass.setClipBias(0.5f);
        waterEffectRenderPass.setWaterMaxAmplitude(5.0f);
        // setting to default value just to show
        waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,
                0.0f), 0.0f));

        projectedGrid = new ProjectedGrid("ProjectedGrid", DisplaySystem.getDisplaySystem().getRenderer().getCamera(), 20, 15, 0.01f,
                new WaterHeightGenerator());
        projectedGrid.setLocalScale(new Vector3f(1.0f, 0.2f, 1.0f));
        projectedGrid.setLocalTranslation(new Vector3f(0.0f, -2.0f, 0.0f));
        // or implement your own waves like this(or in a separate class)...
        // projectedGrid = new ProjectedGrid( "ProjectedGrid", cam, 50, 50,
        // 0.01f, new HeightGenerator() {
        // public float getHeight( float x, float z, float time ) {
        // return
        // FastMath.sin(x*0.05f+time*2.0f)+FastMath.cos(z*0.1f+time*4.0f)*2;
        // }
        // } );

        waterEffectRenderPass.setWaterEffectOnSpatial(projectedGrid);
        ((Node) scene.getRootNode()).attachChild(projectedGrid);

        waterEffectRenderPass.setReflectedScene((Node) scene.getRootNode());
        waterEffectRenderPass.setSkybox(scene.getSky().getRootNode());
        pManager.add(waterEffectRenderPass);

        RenderPass rootPass = new RenderPass();
        rootPass.add((Node) scene.getRootNode());
        pManager.add(rootPass);

        rootNode.setCullHint(Spatial.CullHint.Never);
        rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
    }

	/**
	 * Scene geometry update.
	 */
	@Override
	public void update(float tpf) {

			if (! this.active) return;

			// Update the camera controller
			/*
			if (sceneManager.getScene().getCurrentCameraController() != null) {

				((HasSky)sceneManager.getScene()).getSky().getRootNode().setLocalTranslation(
						DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation()
				);
				((HasSky)sceneManager.getScene()).getSky().getRootNode().updateGeometricState(tpf, true);
			}
			*/


	}

	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void render(float tpf) {

		if (! this.active) return;
		
		if (scene.getCurrentCameraController() == null) {
			throw new IllegalStateException("No camera is associated to " + this);
		}

		// Set up default camera (only if not dedicated)
		// TODO: Delegate this to the camera! not the place! to EACH camera
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		//display.getRenderer().getCamera().setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.01f, 1000 );
		display.getRenderer().getCamera().setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.1f, 800 );
		//display.getRenderer().getCamera().setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.1f, 10000 );
		display.getRenderer().getCamera().update();

		// Update the camera controller
		scene.getCurrentCameraController().update(tpf);

		// Have the PassManager render.
		pManager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
		
	}
    
    private void setupFog() {
        FogState fogState = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
        fogState.setDensity(1.0f);
        fogState.setEnabled(true);
        fogState.setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        fogState.setEnd(farPlane);
        fogState.setStart(farPlane / 10.0f);
        fogState.setDensityFunction(FogState.DensityFunction.Linear);
        fogState.setQuality(FogState.Quality.PerVertex);
        rootNode.setRenderState(fogState);
    }

 
}