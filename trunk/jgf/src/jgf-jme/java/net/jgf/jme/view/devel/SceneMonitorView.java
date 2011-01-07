
package net.jgf.jme.view.devel;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.scene.JmeScene;
import net.jgf.scene.SceneManager;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.system.DisplaySystem;

/**
 */
// TODO: Add warnings for the updateInterval (too low will kill the app, negatives not accepted)
@Configurable
public final class SceneMonitorView extends BaseViewState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SceneMonitorView.class);

	protected SceneManager sceneManager;

	public static float DEFAULT_UPDATEINTERVAL = 60.0f; 

	protected float updateInterval = DEFAULT_UPDATEINTERVAL;

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void doUpdate(float tpf) {
		super.update(tpf);
		SceneMonitor.getMonitor().updateViewer(tpf);
		if (!SceneMonitor.getMonitor().isVisible()) this.deactivate();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#render(float)
	 */
	@Override
	public void doRender(float tpf) {
		super.render(tpf);
		SceneMonitor.getMonitor().renderViewer(DisplaySystem.getDisplaySystem().getRenderer());
	}



	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#setActive(boolean)
	 */
	@Override
	public void doActivate() {
		super.doActivate();
		SceneMonitor.getMonitor().registerNode(((JmeScene)sceneManager.getScene()).getRootNode());
		SceneMonitor.getMonitor().setViewerUpdateInterval(updateInterval);
		SceneMonitor.getMonitor().showViewer(true);
	}


	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#deactivate()
	 */
	@Override
	public void doDeactivate() {
		SceneMonitor.getMonitor().showViewer(false);
		SceneMonitor.getMonitor().unregisterNode(((JmeScene)sceneManager.getScene()).getRootNode());
		super.deactivate();
	}



	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#unload()
	 */
	@Override
	public void doUnload() {
		super.doUnload();
		SceneMonitor.getMonitor().cleanup();
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
		updateInterval = config.getFloat(configPath + "/updateInterval", DEFAULT_UPDATEINTERVAL);
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

}
