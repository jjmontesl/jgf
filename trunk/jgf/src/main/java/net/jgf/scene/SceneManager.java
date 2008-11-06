
package net.jgf.scene;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.core.service.ServiceException;
import net.jgf.system.System;

import org.apache.log4j.Logger;


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
			scene = ConfigurableFactory.newFromConfig(config, configPath + "/scene", Scene.class);
			System.getDirectory().addObject(scene.getId(), scene);
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



}
