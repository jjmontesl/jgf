
package net.jgf.jme.view.devel;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.scene.JmeScene;
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

	protected JmeScene scene;

	protected float updateInterval = 60.0f;

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void update(float tpf) {
		super.update(tpf);
		SceneMonitor.getMonitor().updateViewer(tpf);
		if (!SceneMonitor.getMonitor().isVisible()) this.deactivate();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#render(float)
	 */
	@Override
	public void render(float tpf) {
		super.render(tpf);
		SceneMonitor.getMonitor().renderViewer(DisplaySystem.getDisplaySystem().getRenderer());
	}



	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#setActive(boolean)
	 */
	@Override
	public void activate() {
		super.activate();
		SceneMonitor.getMonitor().registerNode(scene.getRootNode());
		SceneMonitor.getMonitor().setViewerUpdateInterval(updateInterval);
		SceneMonitor.getMonitor().showViewer(true);
	}


	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#deactivate()
	 */
	@Override
	public void deactivate() {
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
		updateInterval = config.getFloat(configPath + "/updateInterval", updateInterval);
	}

	/**
	 * @param scene the scene to set
	 */
	public void setScene(JmeScene scene) {
		if (this.scene != null) SceneMonitor.getMonitor().unregisterNode(this.scene.getRootNode());
		this.scene = scene;
		if (scene != null) SceneMonitor.getMonitor().registerNode(scene.getRootNode());
	}

}
