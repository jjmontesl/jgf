
package net.jgf.scene;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.core.service.ServiceException;
import net.jgf.jme.camera.CameraController;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.system.DisplaySystem;


/**
 * Hold a Scene
 */
//TODO: This should use an interface
@Configurable
public class SceneManager extends BaseService {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SceneManager.class);

	protected Scene scene;

	protected CameraController camera;

	/**
	 * @return the scene
	 */
	public Scene getScene() {
		if (scene == null) throw new ServiceException("Tried to retrieve a scene from " + this + " but no scene was loaded");
		return scene;
	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		if (config.containsKey(configPath + "/scene/@id")) {
			// TODO: Is not advisable to initialize scenes at reading time because they can be engine
			// dependent. Scene initialization should be done at runtime??
			scene = ConfigurableFactory.newFromConfig(config, configPath + "/scene", Scene.class);
			Jgf.getDirectory().addObject(scene.getId(), scene);
		}

		if (config.containsKey(configPath + "/camera/@ref")) {
			String cameraRef = config.getString(configPath + "/camera/@ref");
			Jgf.getDirectory().register(this, "camera", cameraRef);
		}

	}


	@Override
	public void dispose() {
		super.dispose();
		if (scene != null) scene.dispose();
	}


	/**
	 * @param scene the scene to set
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public void update(float tpf) {
		scene.update(tpf);
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

		if (camera != null) {
			// Set up default camera (only if not dedicated)
			// TODO: Delegate this to the camera! not the place!
			DisplaySystem display = DisplaySystem.getDisplaySystem();
			//display.getRenderer().getCamera().setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.01f, 1000 );
			display.getRenderer().getCamera().setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 0.1f, 800 );
			display.getRenderer().getCamera().update();
		}

	}



}
