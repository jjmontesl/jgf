
package net.jgf.loader.scene;

import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.loader.LoadProperties;
import net.jgf.scene.Scene;



/**
 */
@Configurable
public class SceneCreatorLoader extends SceneLoader {

	@Override
	public Scene load(Scene base, LoadProperties properties) {

		if (base != null) {
			throw new ConfigException("Loader " + this + " doesn't accept a base Scene to load over, it must be passed a null Scene");
		}

		combineProperties(properties);

		String className = properties.get("SceneCreatorLoader.sceneClass");

		Class<?> classUnchecked = null;
		try {
		classUnchecked = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ConfigException("Invalid class '" + className + "' to be created by " + this);
		}
		if (Scene.class.isInstance(classUnchecked)) {
			throw new ConfigException("When creating a scene the specified SceneCreatorLoader.sceneClass is not a Scene class");
		}

		if (className == null) {
			throw new ConfigException("Loader " + this + " could not find a SceneCreatorLoader.sceneClass property");
		}

		// TODO: Add checks!

		Scene scene = null;
		try {
			// It has been checked that this class is a Scene
			@SuppressWarnings("unchecked") Class<Scene> sceneClass = (Class<Scene>) classUnchecked;
			scene = sceneClass.newInstance();
		} catch (InstantiationException e) {
			throw new ConfigException("Could not create class '" + className + "' by " + this);
		} catch (IllegalAccessException e) {
			throw new ConfigException("Could not create class '" + className + "' by " + this);
		}

		String sceneId = properties.get("SceneCreatorLoader.id");
		if (sceneId != null) {
			scene.setId(sceneId);
		}

		return scene;

	}

}
