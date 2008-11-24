
package net.jgf.loader;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.system.Jgf;



/**
 * The EntityManager holds and manages the level entities, and provides
 * methods to create new ones.
 */
@Configurable
public class DefaultLoaderManager extends BaseService implements LoaderManager {

	private Map<String, Loader<?>> loaders;

	public DefaultLoaderManager() {
		loaders = new Hashtable<String, Loader<?>>();
	}

	@Override
	public void addLoader(Loader<?> loader) {
		loaders.put(loader.getId(), loader);
	}

	@Override
	public Loader<?> getLoader(String loaderId) {
		Loader<?> result = loaders.get(loaderId);
		if (result == null) {
			throw new ConfigException("Tried to retrieve a non existent Loader named '" + loaderId + "' from " + this);
		}
		return result;
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		List<Loader> cfgLoaders = ConfigurableFactory.newListFromConfig(config, configPath + "/loader", Loader.class);
		for (Loader<?> loader : cfgLoaders) {
			Jgf.getDirectory().addObject(loader.getId(), loader);
			this.addLoader (loader);
		}

	}

}
