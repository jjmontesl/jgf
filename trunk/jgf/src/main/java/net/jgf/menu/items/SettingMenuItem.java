
package net.jgf.menu.items;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.menu.MenuController;
import net.jgf.settings.Setting;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class SettingMenuItem extends BaseMenuItem {

	protected Setting setting;
	
	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		this.key = "Undefined";
		super.readConfig(config, configPath);
		
		String settingRef = config.getString(configPath + "/setting/@ref", null);
		this.key = settingRef;

	}

	@Override
	public boolean isNavigable() {
	  return true;
	}

	@Override
	public void perform(MenuController controller) {

	}


}