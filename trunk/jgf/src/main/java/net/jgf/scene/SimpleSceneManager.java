
package net.jgf.scene;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.core.service.ServiceException;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;


/**
 * <p>Simple SceneManager is a straightforward service that
 * implements the {@link SceneManager} interface. 
 * It just holds a reference to a Scene.</p>
 * <p>Many components in JGF need a reference to a SceneManager
 * to work. When you create a scene, you will need to associate
 * it with your SceneManager. There are default loaders
 * that already create scenes and link them to the SceneManager.</p>
 * <p>Configuration allows for a single &lt;scene&gt; element
 * of type Scene, but in most cases, you will be loading your
 * scene later through loaders, and not defining a scene in 
 * JGF configuration file.</p>
 * @see SceneManager.
 */
@Configurable
public class SimpleSceneManager extends BaseService implements SceneManager {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SceneManager.class);

	/**
	 * The scene managed by this SceneManager.
	 */
	protected Scene scene;

	/* (non-Javadoc)
	 * @see net.jgf.scene.SceneManager#getScene()
	 */
	public Scene getScene() {
		if (scene == null) throw new ServiceException("Tried to retrieve a scene from " + this + " but no scene has been set");
		return scene;
	}
	
	/* (non-Javadoc)
	 * @see net.jgf.scene.SceneManager#setScene(net.jgf.scene.Scene)
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		if (config.containsKey(configPath + "/scene/@id")) {
			scene = ConfigurableFactory.newFromConfig(config, configPath + "/scene", Scene.class);
			Jgf.getDirectory().addObject(scene.getId(), scene);
			Jgf.getDirectory().register(this, "scene", scene.getId());
		}



	}


	@Override
	public void dispose() {
		super.dispose();
		if (scene != null) scene.dispose();
	}



	public void update(float tpf) {
		scene.update(tpf);
	}


}
