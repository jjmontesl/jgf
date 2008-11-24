
package net.jgf.jme.view;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.IllegalStateException;
import net.jgf.jme.camera.CameraController;
import net.jgf.jme.scene.JmeScene;
import net.jgf.scene.SceneManager;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.system.DisplaySystem;

/**
 * <p>This state manages the level rendering. If disabled, no level
 * rendering will happen.</p>
 */
@Configurable
public class SceneRenderView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SceneRenderView.class);

	/**
	 * The camera controller that manages the Scene Render camera.
	 */
	protected CameraController camera;

	/**
	 *
	 */
	protected SceneManager sceneManager;


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

			JmeScene scene = (JmeScene) sceneManager.getScene();

			// TODO: Maybe we should allow an option to update the geometric state
			// TODO: Note that here is where the updateGeometricState call is done. Document!!!
			// TODO: Is this correct? doing this here? see SceneRenderView!
			// TODO: What about other things (skybox, etc...) that need to be updated?
			//scene.getRootNode().updateGeometricState(tpf, true);

			// Center the skybox on the camera
			// TODO: This should not behere, the skybox belongs to other place
			//scene.getRootNode().getChild("skybox").setLocalTranslation(DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation());

	}

	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void render(float tpf) {

		if (! this.active) return;

		if (camera == null) {
			throw new IllegalStateException("No camera is associated to " + this);
		}

		// Update the camera controller
		if (camera != null) camera.update(tpf);

		JmeScene scene = (JmeScene) sceneManager.getScene();

		// TODO: Do only when needed
		//scene.getRootNode().updateRenderState();

		//if (passManager != null) passManager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
		DisplaySystem.getDisplaySystem().getRenderer().draw(scene.getRootNode());

		/*
		SceneMonitor.getMonitor().renderViewer(DisplaySystem.getDisplaySystem().getRenderer());
		*/

	}

	/**
	 * @param cameraController the cameraController to set
	 */
	public void setCamera(CameraController cameraController) {

		this.camera = cameraController;


		// Set up default camera (only if not dedicated)
		// TODO: Delegate this to the camera
		// TODO: Should this be done in this setter? NO!
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		if ((display != null) && (display.getRenderer() != null)) {
			//display.getRenderer().getCamera().setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.01f, 1000 );
			display.getRenderer().getCamera().setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.1f, 800 );
			display.getRenderer().getCamera().update();
		}

	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		// TODO: Maybe these two ones should be optional / null (study carefully)
		String sceneManagerRef = config.getString(configPath + "/sceneManager/@ref");
		if (sceneManagerRef != null) net.jgf.system.Jgf.getDirectory().register(this, "sceneManager", sceneManagerRef);
		String cameraRef = config.getString(configPath + "/camera/@ref", null);
		if (cameraRef != null) Jgf.getDirectory().register(this, "camera", cameraRef);

	}

	/**
	 * @return the camera
	 */
	public CameraController getCamera() {
		return camera;
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
