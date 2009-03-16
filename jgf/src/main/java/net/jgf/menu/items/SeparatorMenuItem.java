
package net.jgf.menu.items;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.menu.MenuController;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class SeparatorMenuItem extends BaseMenuItem {


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

	}

	@Override
	public boolean isNavigable() {
	  return false;
	}

	@Override
	public void perform(MenuController controller) {
	}

}