
package net.jgf.loader.entity.pool;

import java.util.ArrayList;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.component.BaseComponent;
import net.jgf.entity.Entity;
import net.jgf.loader.LoadProperties;

import org.apache.log4j.Logger;


/**
 */
@Configurable
public class EntityPool extends BaseComponent {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(EntityPool.class);

	private String key;

	private String value;

	private int maxInstances;

	public EntityPool() {
		super();
	}

	private ArrayList<Entity> usedItems = new ArrayList<Entity>();

	private ArrayList<Entity> freeItems = new ArrayList<Entity>();



	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		key = config.getString(configPath + "/key");
		value = config.getString(configPath + "/value");
		maxInstances = config.getInt(configPath + "/maxInstances");

	}

	public boolean matches(LoadProperties properties) {
		String actualValue = properties.get(key);
		return (value.equals(actualValue));
	}

	public Entity getEntity() {

		logger.debug("Trying to retrieve entity from pool " + this);

		Entity entity = null;
		if (freeItems.size() > 0) {
			logger.trace ("Returning item " + entity + " from pool " + this);
			entity = freeItems.remove(freeItems.size() - 1);
			usedItems.add(entity);
		}
		return entity;
	}

	public void addEntity(Entity entity) {
		if (this.getSize() >= maxInstances) {
			logger.warn("Can't add entity to the pool: the maximum of entities in pool " + this + " has been reached");
		} else {
			this.usedItems.add(entity);
		}
	}

	public int getSize() {
		return (freeItems.size() + usedItems.size());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[id=" + id +",items=" + this.getSize() + "]";
	}

	/**
	 *
	 * @param entity
	 * @return
	 */
	public boolean returnToPool(Entity item) {
		int index = usedItems.indexOf(item);
		if (index >= 0) {
			usedItems.remove(index);
			freeItems.add(item);
			return true;
		} else {
			return false;
		}
	}


}
