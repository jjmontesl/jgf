
package net.jgf.jme.loader.scene;



import net.jgf.config.Configurable;
import net.jgf.jme.scene.JmeScene;
import net.jgf.loader.BaseLoader;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.scene.SceneLoader;
import net.jgf.scene.Scene;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.scene.Node;

/**
 */
@Configurable
public final class SceneModelLoader extends SceneLoader {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SceneModelLoader.class);

	public Scene load(Scene scene, LoadProperties properties) {

		// TODO: Check for non-null entity

		combineProperties(properties);

		String loaderRef = properties.get("SceneModelLoader.loader");
		BaseLoader<Node> modelLoader = Jgf.getDirectory().getObjectAs(loaderRef, BaseLoader.class);
		Node model = modelLoader.load(null, properties);

		Node sceneNode = new Node("scene");
		sceneNode.attachChild(model);

		((JmeScene)scene).getRootNode().attachChild(sceneNode);

		return scene;

	}

}
