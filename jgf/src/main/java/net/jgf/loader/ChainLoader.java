package net.jgf.loader;

import java.util.ArrayList;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.system.Jgf;



/**
 *
 */
// TODO: Organize conversion and loading strategy
@Configurable
public class ChainLoader<T> extends BaseLoader<T> {

	private List<Loader<T>> subLoaders;

	public ChainLoader () {
		subLoaders = new ArrayList<Loader<T>>();
	}

	@Override
	public T load(T object, LoadProperties properties) throws LoaderException {

		T result = object;

		combineProperties(properties);

		for (Loader<T> subLoader : subLoaders) {
			result = subLoader.load(result, properties);
		}

		return result;

	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		// Read list of subtransformers
		List<Loader> subloaders = ConfigurableFactory.newListFromConfig(config, configPath + "/loader", Loader.class);
		for (Loader<T> loader: subloaders) {
			addLoader(loader);
			Jgf.getDirectory().addObject(loader.getId(), loader);
		}


	}

	public void addLoader(Loader<T> loaderRef) {
		subLoaders.add(loaderRef);
	}

}
