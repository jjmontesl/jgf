
package net.jgf.menu.items;

import net.jgf.config.Config;
import net.jgf.config.Configurable;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class TextMenuItem extends BaseMenuItem {


	protected String text;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		text = config.getString(configPath + "/text");

	}

}