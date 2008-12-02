package net.jgf.loader;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;



/**
 *
 */
// TODO: Organize conversion and loading strategy
@Configurable
public class ReferenceLoader<T> extends BaseLoader<T> {

	@Override
	public T load(T base, LoadProperties properties) throws LoaderException {

		combineProperties(properties);
		Loader<T> loader = Jgf.getDirectory().getObjectAs(properties.get("ReferenceLoader.loader"), Loader.class);
		return loader.load(base, properties);

	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

	}

}
