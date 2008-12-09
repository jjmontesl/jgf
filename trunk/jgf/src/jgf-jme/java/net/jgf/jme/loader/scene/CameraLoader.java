
package net.jgf.jme.loader.scene;

import java.util.List;

import net.jgf.camera.CameraController;
import net.jgf.camera.HasCameras;
import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.loader.BaseLoader;
import net.jgf.loader.LoadProperties;
import net.jgf.scene.Scene;
import net.jgf.system.Jgf;



/**
 */
@Configurable
public class CameraLoader extends BaseLoader<Scene> {

	List<CameraController> controllers = null;

	String cameraRef = null;

	@Override
	public Scene load(Scene scene, LoadProperties properties) {

		combineProperties(properties);

		HasCameras hasCameras = (HasCameras) scene;
		for (CameraController camera : controllers) {
			hasCameras.getCameraControllers().addCameraController(camera);
		}

		if (cameraRef != null) {
			Jgf.getDirectory().register(scene, "camera", cameraRef);
		}

		return scene;

	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		controllers = ConfigurableFactory.newListFromConfig(config, configPath + "/cameras/camera", CameraController.class);
		for (CameraController camera : controllers) {
			Jgf.getDirectory().addObject(camera.getId(), camera);
		}

		if (config.containsKey(configPath + "/camera/@ref")) {
			cameraRef = config.getString(configPath + "/camera/@ref");
		}

	}

}
