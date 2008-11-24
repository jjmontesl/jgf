package net.jgf.loader;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.model.ModelException;
import net.jgf.system.Jgf;



/**
 *
 */
// TODO: Organize conversion and loading strategy
@Configurable
public class ReferenceLoader<T> extends BaseLoader<T> {

	private Loader<T> loader;


	@Override
	public T load(T base, LoadProperties properties) throws ModelException {

		combineProperties(properties);
		return loader.load(base, properties);

	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);
		Jgf.getDirectory().register(this, "loader", config.getString(configPath + "/loader/@ref"));

	}


	/**
	 * @return the loader
	 */
	public Loader<T> getLoader() {
		return loader;
	}


	/**
	 * @param loader the loader to set
	 */
	public void setLoader(Loader<T> loader) {
		this.loader = loader;
	}



}
