
package net.jgf.jme.loader.entity;



import net.jgf.config.Configurable;
import net.jgf.entity.Entity;
import net.jgf.jme.entity.SpatialEntity;
import net.jgf.loader.BaseLoader;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.entity.EntityLoader;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.scene.Node;

/**
 */
@Configurable
public final class EntityModelLoader extends EntityLoader {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(EntityModelLoader.class);

	public Entity load(Entity entity, LoadProperties properties) {

		// TODO: Check for non-null entity

		combineProperties(properties);

		String loaderRef = properties.get("EntityModelLoader.loader");
		BaseLoader<Node> modelLoader = Jgf.getDirectory().getObjectAs(loaderRef, BaseLoader.class);
		Node model = modelLoader.load(null, properties);
		((SpatialEntity)entity).setSpatial(model);

		return entity;

	}

}
