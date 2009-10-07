
package net.jgf.jme.view.devel;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.scene.JmeScene;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.input.InputHandler;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.sceneworker.SceneWorker;
import com.sceneworker.app.ISceneWorkerApp;
import com.sceneworker.app.SceneWorkerAppHandler;

/**
 */
// TODO: Add warnings for the updateInterval (too low will kill the app, negatives not accepted)
@Configurable
public final class SceneWorkerView extends BaseViewState implements ISceneWorkerApp {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SceneWorkerView.class);

	protected JmeScene scene;

	//protected float updateInterval = 60.0f;
	
	protected float lastTpf = 0.05f;
	
	protected InputHandler inputHandler;
	
	private SceneWorkerAppHandler sceneWorkerHandler;
	
	
	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void update(float tpf) {
		this.lastTpf = tpf;
		super.update(tpf);
		SceneMonitor.getMonitor().updateViewer(tpf);
		sceneWorkerHandler.update();
		if (!SceneMonitor.getMonitor().isVisible()) this.deactivate();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#render(float)
	 */
	@Override
	public void render(float tpf) {
		this.lastTpf = tpf;
		super.render(tpf);
		SceneMonitor.getMonitor().renderViewer(DisplaySystem.getDisplaySystem().getRenderer());
		sceneWorkerHandler.render();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#setActive(boolean)
	 */
	@Override
	public void activate() {
		super.activate();
		logger.debug ("Activating Scene Worker");
		SceneWorker.inst().initialiseSceneWorkerAndMonitor();
		SceneMonitor.getMonitor().registerNode(scene.getRootNode());
		SceneMonitor.getMonitor().registerNode(Jgf.getApp().getEngine().getViewManager().getRootState(), "ViewStates");
		//SceneMonitor.getMonitor().setViewerUpdateInterval(updateInterval);
		//SceneMonitor.getMonitor().showViewer(true);
		// initialise the application handler so we get tools palette, input handler and rendering
        sceneWorkerHandler = new SceneWorkerAppHandler(this);
        sceneWorkerHandler.initialise();
	}


	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#deactivate()
	 */
	@Override
	public void deactivate() {
		logger.debug ("Deactivating Scene Worker");
		SceneMonitor.getMonitor().showViewer(false);
		SceneMonitor.getMonitor().unregisterNode(scene.getRootNode());
		super.deactivate();
	}



	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#unload()
	 */
	@Override
	public void unload() {
		super.unload();
		SceneMonitor.getMonitor().cleanup();
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		String sceneRef = config.getString(configPath + "/scene/@ref", null);
		if (sceneRef != null) {
			Jgf.getDirectory().register(this, "scene", sceneRef);
		}
		//updateInterval = config.getFloat(configPath + "/updateInterval", updateInterval);
	}

	/**
	 * @param scene the scene to set
	 */
	public void setScene(JmeScene scene) {
		if (this.scene != null) SceneMonitor.getMonitor().unregisterNode(this.scene.getRootNode());
		this.scene = scene;
		if (scene != null) SceneMonitor.getMonitor().registerNode(scene.getRootNode());
	}

	
	/* (non-Javadoc)
	 * @see com.sceneworker.app.ISceneWorkerApp#getCamera()
	 */
	@Override
	public Camera getCamera() {
		// TODO Auto-generated method stub
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		return display.getRenderer().getCamera();
	}

	/* (non-Javadoc)
	 * @see com.sceneworker.app.ISceneWorkerApp#getDisplaySystem()
	 */
	@Override
	public DisplaySystem getDisplaySystem() {
		return DisplaySystem.getDisplaySystem();
	}

	/* (non-Javadoc)
	 * @see com.sceneworker.app.ISceneWorkerApp#getInputHandler()
	 */
	@Override
	public InputHandler getInputHandler() {
		return this.inputHandler;
	}

	/* (non-Javadoc)
	 * @see com.sceneworker.app.ISceneWorkerApp#getRootNode()
	 */
	@Override
	public Node getRootNode() {
		return scene.getRootNode();
	}

	/* (non-Javadoc)
	 * @see com.sceneworker.app.ISceneWorkerApp#getTimePerFrame()
	 */
	@Override
	public float getTimePerFrame() {
		return this.lastTpf;
	}

	/* (non-Javadoc)
	 * @see com.sceneworker.app.ISceneWorkerApp#setInputHandler(com.jme.input.InputHandler)
	 */
	@Override
	public void setInputHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}
	
	

}
