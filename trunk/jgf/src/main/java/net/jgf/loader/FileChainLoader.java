
package net.jgf.loader;



import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.system.System;

import org.apache.log4j.Logger;

import com.jme.util.Timer;

/**
 * Indoor Battles level loader.
 * It reads the specified level descriptor and creates a Scene
 * instance to hold the level.
 */
@Configurable
public class FileChainLoader<T> extends BaseLoader<T> {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(FileChainLoader.class);

	private List<Loader<T>> subLoaders;

	/**
	 * Loads a scene, including scene data
	 */
	@Override
	public T load(T object, LoadProperties properties) {

		// TODO: Check this conversion
		combineProperties(properties);

		String resourceUrl = properties.get("FileChainLoader.resourceUrl");
		if (resourceUrl == null) {
			throw new ConfigException("Loader " + this + " needs to receive a valid 'FileChainLoader.resourceUrl' property");
		}

		logger.info("Loading " + object + " from " + resourceUrl);

		// Read descriptor
		Config config = new Config(resourceUrl);

		// Read list of subtransformers
		List<Loader> subLoadersList = ConfigurableFactory.newListFromConfig(config, "loader/loader", Loader.class);
		subLoaders = (List) subLoadersList;

		// Apply subtransformers
		combineProperties(properties);

		T result = object;

		for (Loader<T> subLoader : subLoaders) {
			// TODO: properties are left to following loaders... only selected properties should survive???!!!!
			// TODO: this could conflict with default values, as they could be already added by a precedent loader
			System.getDirectory().addObject(subLoader.getId(), subLoader);
			result = subLoader.load(result, properties);
		}

		// Restart timer!
		Timer.getTimer().reset();

		return result;

	}



}
