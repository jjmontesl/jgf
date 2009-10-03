
package net.jgf.player;

import net.jgf.config.Config;
import net.jgf.config.Configurable;


/**
 */
@Configurable
public class DefaultPlayerGroup extends BasePlayerGroup {



	public DefaultPlayerGroup() {
		super();
	}

	public DefaultPlayerGroup(String name) {
		super(name);
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

	}

}
