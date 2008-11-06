package net.jgf.loader;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.model.ModelException;
import net.jgf.system.System;



/**
 *
 */
// TODO: Organize conversion and loading strategy
@Configurable
public class ReferenceLoader<T> extends BaseLoader<T> {

	private String loaderRef;


	@Override
	public T load(T base, LoadProperties properties) throws ModelException {

		combineProperties(properties);
		Loader<T> loader = System.getDirectory().getObjectAs(loaderRef, Loader.class);

		return loader.load(base, properties);

	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		// Read list of subtransformers
		loaderRef = config.getString(configPath + "/loader/@ref");

	}

}
