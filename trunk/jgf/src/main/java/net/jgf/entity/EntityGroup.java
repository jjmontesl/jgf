
package net.jgf.entity;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.state.BaseStateNode;


/**
 * Each one of the game entities which are not part of the static map.
 * An Entity is server aware, meaning that if it is running on a dedicated server,
 * will avoid to do certain work.
 */
@Configurable
// TODO: Should this be an interface?? It is already states
public final class EntityGroup extends BaseStateNode<Entity> implements Entity {

	public EntityGroup() {
		super();
		this.autoActivate = true;
		this.autoLoad = true;
	}

	@Override
	public void update(float tpf) {
	    if (this.isActive()) {
    		for (Entity entity : children) {
    			if (entity.isActive()) entity.update(tpf);
    		}
	    }
	}



	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath, "entity", Entity.class);

	}

}
