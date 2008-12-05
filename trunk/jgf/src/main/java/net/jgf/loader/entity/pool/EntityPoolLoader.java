
package net.jgf.loader.entity.pool;

import java.util.ArrayList;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.entity.Entity;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.Loader;
import net.jgf.loader.LoaderException;
import net.jgf.loader.entity.EntityLoader;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;



/**
 *
 */
@Configurable
public class EntityPoolLoader extends EntityLoader {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(EntityPoolLoader.class);

	private Loader<Entity> loader;

	List<EntityPool> pools;



	public EntityPoolLoader() {
		super();
		pools = new ArrayList<EntityPool>();
	}



	@Override
	// TODO: Add methods to use from code, but beware that the loaderRef is being resolved in every load
	public Entity load(Entity base, LoadProperties properties) throws LoaderException {

		combineProperties(properties);
		checkNullBase(base);

		Entity entity = null;

		for (EntityPool pool : pools) {
			if (pool.matches(properties)) {
				// Try to retrieve the entity from the pool
				entity = pool.getEntity();

				// If the result is null, load the entity from the underlying loader and add it to the pool
				if (entity == null) {
					entity = loader.load(base, properties);
					pool.addEntity(entity);
				}
				break;
			}

		}

		if (entity == null) {
			entity = loader.load(base, properties);
		}

		return entity;
	}


	public void returnToPool(Entity item) {

		boolean found = false;

		// TODO: Diagnostics mode: check if entity returned is deactivated and unloaded!

		for (EntityPool pool : pools) {

			// Try to retrieve the entity from the pool
			if (pool.returnToPool(item)) {
				found = true;
				break;
			}
		}

		if (!found) {
			logger.warn("Tried to return an unexisted item " + item + " to " + this);
		}

	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		Jgf.getDirectory().register(this, "loader", config.getString(configPath + "/loader/@ref"));

		// Read list of subtransformers
		List<EntityPool> poolList = ConfigurableFactory.newListFromConfig(config, configPath + "/pool", EntityPool.class);
		for (EntityPool pool : poolList) {
			pools.add(pool);
			Jgf.getDirectory().addObject(pool.getId(), pool);
		}

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[id=" + id +",pools=" + pools.size() + "]";
	}

	/**
	 * @return the loader
	 */
	public Loader<Entity> getLoader() {
		return loader;
	}

	/**
	 * @param loader the loader to set
	 */
	public void setLoader(Loader<Entity> loader) {
		this.loader = loader;
	}


}
