
package net.jgf.jme.loader.scene;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.jme.scene.util.SceneUtils;
import net.jgf.loader.BaseLoader;
import net.jgf.loader.LoadProperties;
import net.jgf.scene.Scene;

import com.jme.scene.Node;



/**
 */
@Configurable
public class SkyboxLoader extends BaseLoader<Scene> {

	@Override
	public Scene load(Scene scene, LoadProperties properties) {

		combineProperties(properties);

		String prefix = properties.get("SkyboxLoader.prefix");
		String suffix  = properties.get("SkyboxLoader.suffix");

  	if (prefix == null) {
  		throw new ConfigException("No skybox filename prefix has been set to " + this);
  	}

		Node skybox = SceneUtils.setupSkyBox(prefix, suffix);
      	skybox.setRenderState(((DefaultJmeScene)scene).getCommonRenderStates().get("fogDisabled"));
      	((DefaultJmeScene)scene).getSky().setRootNode(skybox);
      	skybox.updateRenderState();

		return scene;

	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

	}

}
