
package net.jgf.menu.items;

import net.jgf.config.Config;
import net.jgf.config.Configurable;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class ActionMenuItem extends TextMenuItem {

	protected String action;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		action = config.getString(configPath + "/action");

	}

}