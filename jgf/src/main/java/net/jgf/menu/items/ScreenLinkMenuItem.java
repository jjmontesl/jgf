
package net.jgf.menu.items;

import net.jgf.config.Config;
import net.jgf.config.Configurable;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class ScreenLinkMenuItem extends TextMenuItem {

	protected String targetRef;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		targetRef = config.getString(configPath + "/target/@ref");

	}

}