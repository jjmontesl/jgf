package net.jgf.loader.entity;

import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.core.naming.ObjectCreator;
import net.jgf.entity.Entity;
import net.jgf.loader.BaseLoader;
import net.jgf.loader.LoadProperties;

/**
 */
@Configurable
public class EntityCreatorLoader extends BaseLoader<Entity> {

	@Override
	public Entity load(Entity base, LoadProperties properties) {

		if (base != null) {
			throw new ConfigException("Loader " + this + " doesn't accept a base Entity to load over, it must be passed a null Scene");
		}

		combineProperties(properties);

		String className = properties.get("EntityCreatorLoader.entityClass");

		if (className == null) {
			throw new ConfigException("Loader " + this + " could not find a EntityCreatorLoader.entityClass property");
		}


		Entity entity = (Entity) ObjectCreator.createObject(className);
		
		String sceneId = properties.get("EntityCreatorLoader.id");
		if (sceneId != null) {
			entity.setId(sceneId);
		}
		
		return entity;

	}

}
