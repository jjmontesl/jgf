
package net.jgf.jme.view;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.camera.CameraController;
import net.jgf.jme.scene.sky.HasSky;
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
	 * The camera controller that manages the Scene Render camera.
	 */
	protected CameraController camera;

	/**
	 *
	 */
	protected HasSky hasSky;



	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void load() {
		super.load();
	}

	/**
	 * Scene geometry update.
	 */
	@Override
	public void update(float tpf) {

			if (! this.active) return;

			// Center the skybox on the camera
			// TODO: This should not behere, the skybox belongs to other place
			//scene.getRootNode().getChild("skybox").setLocalTranslation(DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation());

			// Update the camera controller
			if (camera != null) camera.update(tpf);


	}

	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void render(float tpf) {

		if (! this.active) return;

		if (camera == null) return;

		// TODO: Do only when needed
		//scene.getRootNode().updateRenderState();

		//if (passManager != null) passManager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
		DisplaySystem.getDisplaySystem().getRenderer().draw(hasSky.getSky().getRootNode());

		/*
		SceneMonitor.getMonitor().renderViewer(DisplaySystem.getDisplaySystem().getRenderer());
		*/

	}

	/**
	 * @return the cameraController
	 */
	public CameraController getCameraController() {
		return camera;
	}

	/**
	 * @param cameraController the cameraController to set
	 */
	public void setCameraController(CameraController cameraController) {

		this.camera = cameraController;

		// Set up default camera (only if not dedicated)
		// TODO: Delegate this to the camera
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		//display.getRenderer().getCamera().setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.01f, 1000 );
		display.getRenderer().getCamera().setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.1f, 800 );
		display.getRenderer().getCamera().update();



	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		net.jgf.system.Jgf.getDirectory().register(this, "hasSky", config.getString(configPath + "/hasSky/@ref"));
		net.jgf.system.Jgf.getDirectory().register(this, "camera", config.getString(configPath + "/camera/@ref"));
	}

	/**
	 * @return the camera
	 */
	public CameraController getCamera() {
		return camera;
	}

	/**
	 * @param camera the camera to set
	 */
	public void setCamera(CameraController camera) {
		this.camera = camera;
	}

	/**
	 * @return the hasSky
	 */
	public HasSky getHasSky() {
		return hasSky;
	}

	/**
	 * @param hasSky the hasSky to set
	 */
	public void setHasSky(HasSky hasSky) {
		this.hasSky = hasSky;
	}



}
