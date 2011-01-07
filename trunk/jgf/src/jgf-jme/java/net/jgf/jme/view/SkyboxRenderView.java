
package net.jgf.jme.view;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.scene.sky.HasSky;
import net.jgf.scene.SceneManager;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.system.DisplaySystem;

/**
 */
@Configurable
public class SkyboxRenderView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SkyboxRenderView.class);

	/**
	 *
	 */
	protected SceneManager sceneManager;



	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void doLoad() {
		super.doLoad();
	}

	/**
	 * Scene geometry update.
	 */
	@Override
	public void doUpdate(float tpf) {

			if (! this.active) return;

			// Center the skybox on the camera
			// TODO: This should not behere, the skybox belongs to other place ??? Not sure, maybe it does
			// TODO: Actually implement this!
			//scene.getRootNode().getChild("skybox").setLocalTranslation(DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation());


			// Update the camera controller
			if (sceneManager.getScene().getCurrentCameraController() != null) {

				((HasSky)sceneManager.getScene()).getSky().getRootNode().setLocalTranslation(
						DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation()
				);
				((HasSky)sceneManager.getScene()).getSky().getRootNode().updateGeometricState(tpf, true);
			}


	}

	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void doRender(float tpf) {

		if (! this.active) return;

		if (sceneManager.getScene().getCurrentCameraController() == null) return;

		// TODO: Do only when needed
		//scene.getRootNode().updateRenderState();

		//if (passManager != null) passManager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
		DisplaySystem.getDisplaySystem().getRenderer().draw(((HasSky)sceneManager.getScene()).getSky().getRootNode());

		/*
		SceneMonitor.getMonitor().renderViewer(DisplaySystem.getDisplaySystem().getRenderer());
		*/

	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		net.jgf.system.Jgf.getDirectory().register(this, "sceneManager", config.getString(configPath + "/sceneManager/@ref"));
	}

	/**
	 * @return the sceneManager
	 */
	public SceneManager getSceneManager() {
		return sceneManager;
	}

	/**
	 * @param sceneManager the sceneManager to set
	 */
	public void setSceneManager(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}



}
