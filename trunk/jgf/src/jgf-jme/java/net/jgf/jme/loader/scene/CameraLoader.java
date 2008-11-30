
package net.jgf.jme.loader.scene;

import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.jme.camera.CameraController;
import net.jgf.jme.camera.HasCameras;
import net.jgf.loader.BaseLoader;
import net.jgf.loader.LoadProperties;
import net.jgf.scene.Scene;
import net.jgf.system.Jgf;



/**
 */
@Configurable
public class CameraLoader extends BaseLoader<Scene> {

	List<CameraController> controllers = null;

	@Override
	public Scene load(Scene scene, LoadProperties properties) {

		combineProperties(properties);

		HasCameras hasCameras = (HasCameras) scene;
		for (CameraController camera : controllers) {
			hasCameras.getCameraControllers().addCameraController(camera);
		}

		return scene;

	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		controllers = ConfigurableFactory.newListFromConfig(config, configPath + "/camera", CameraController.class);
		for (CameraController camera : controllers) {
			Jgf.getDirectory().addObject(camera.getId(), camera);
		}

	}

}
