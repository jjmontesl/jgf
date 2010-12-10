
package net.jgf.jme.view.devel;

import java.util.ArrayList;
import java.util.List;

import net.jgf.camera.CameraController;
import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.state.BaseState;
import net.jgf.jme.scene.JmeScene;
import net.jgf.scene.SceneManager;
import net.jgf.scenemonitor.StatePropertyPage;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.acarter.scenemonitor.SceneMonitor;
import com.acarter.scenemonitor.information.A_MonitorInformationPanel;
import com.acarter.scenemonitor.propertydescriptor.PropertyInformation;
import com.jme.input.InputHandler;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.sceneworker.SceneWorker;
import com.sceneworker.app.ISceneWorkerApp;
import com.sceneworker.app.SceneWorkerAppController;

/**
 */
// TODO: Add warnings for the updateInterval (too low will kill the app, negatives not accepted)
@Configurable
public final class SceneWorkerView extends BaseViewState implements ISceneWorkerApp {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SceneWorkerView.class);

	protected SceneManager sceneManager;
	
	// public static float DEFAULT_UPDATEINTERVAL = 60.0f; 

	//	protected float updateInterval = DEFAULT_UPDATEINTERVAL;
	
	protected float lastTpf = 0.05f;
	
	protected InputHandler inputHandler;
	
	private SceneWorkerAppController sceneWorkerAppController;
	
	private List<String> registerRefs = new ArrayList<String>();
	
	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void update(float tpf) {
		this.lastTpf = tpf;
		super.update(tpf);
		//SceneMonitor.getMonitor().updateViewer(tpf);
		sceneWorkerAppController.update();
		if (this.inputHandler == null) inputHandler.update(tpf);
		// TODO: Check for deactivation!
		//if (!SceneMonitor.getMonitor().isVisible()) this.deactivate();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#render(float)
	 */
	@Override
	public void render(float tpf) {
		this.lastTpf = tpf;
		super.render(tpf);
		//SceneMonitor.getMonitor().renderViewer(DisplaySystem.getDisplaySystem().getRenderer());
		sceneWorkerAppController.render();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#setActive(boolean)
	 */
	@Override
	public void activate() {
		super.activate();
		logger.debug ("Activating Scene Worker");
		
		SceneWorker.inst().initialiseSceneWorkerAndMonitor();
		sceneManager.getScene().setCurrentCameraController(new CameraController() {
			public void update (float tpf) {}
		});
		
		// Register SceneMonitor custom property pages
		A_MonitorInformationPanel mip = SceneMonitor.getMonitor().getMonitorInformationPanel();
		PropertyInformation propInfo = (PropertyInformation) mip;
		propInfo.getPropertyPageHandler().registerPropertyPage(BaseState.class, new StatePropertyPage());
		
		/*SceneWorker.inst().addTreeRepresentationBinding(StateNode.class, StateTreeNode.class);
		SceneWorker.inst().addTreeRepresentationBinding(ViewStateNode.class, StateTreeNode.class);*/
		
		SceneMonitor.getMonitor().registerNode(((JmeScene)sceneManager.getScene()).getRootNode());
		for (String ref : registerRefs) {
			SceneMonitor.getMonitor().registerNode(Jgf.getDirectory().getObjectAs(ref, Object.class), ref);
		}
		
		// Initialise the application handler so we get tools palette, input handler and rendering
		sceneWorkerAppController = new SceneWorkerAppController(this);
		sceneWorkerAppController.initialise();
        //SceneMonitor.getMonitor().showViewer(true);
	}


	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#deactivate()
	 */
	@Override
	public void deactivate() {
		logger.debug ("Deactivating Scene Worker");
		SceneMonitor.getMonitor().showViewer(false);
		SceneWorker.inst().deactivate();
		super.deactivate();
	}



	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#unload()
	 */
	@Override
	public void unload() {
		super.unload();
		SceneWorker.inst().unload();
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		String sceneManagerRef = config.getString(configPath + "/sceneManager/@ref", null);
		if (sceneManagerRef != null) {
			Jgf.getDirectory().register(this, "sceneManager", sceneManagerRef);
		}
		
		List<String> refs = config.getList(configPath + "/register/@ref");
		for (String ref : refs) {
			registerRefs.add(ref);
		}
		
	}

	/**
	 * @param scene the scene to set
	 */
	public void setSceneManager(SceneManager sceneManager) {
		if (this.isActive()) {
			throw new IllegalStateException("Can't set the SceneManager for " + this + " as the view is already running. You need to deactivate it first.");
		}
		this.sceneManager = sceneManager;
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
		if (this.inputHandler == null) this.inputHandler = new InputHandler();
		return this.inputHandler;
	}

	/* (non-Javadoc)
	 * @see com.sceneworker.app.ISceneWorkerApp#getRootNode()
	 */
	@Override
	public Node getRootNode() {
		return ((JmeScene)sceneManager.getScene()).getRootNode();
	}

	/* (non-Javadoc)
	 * @see com.sceneworker.app.ISceneWorkerApp#getTimePerFrame()
	 */
	@Override
	public float getTimePerFrame() {
		return this.lastTpf;
	}

}
