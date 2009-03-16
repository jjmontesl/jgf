
package net.jgf.menu.items;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.menu.Menu;
import net.jgf.menu.MenuController;
import net.jgf.system.Jgf;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class ScreenLinkMenuItem extends BaseMenuItem {

	protected String targetRef;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		targetRef = config.getString(configPath + "/target/@ref");

	}

	@Override
	public boolean isNavigable() {
		return true;
	}

	@Override
	public void perform(MenuController controller) {

		Menu menu = Jgf.getDirectory().getObjectAs(targetRef, Menu.class);
		if (Jgf.getApp().isDebug()) {
			if (menu == null) {
				throw new ConfigException("Can't perform menu task " + this + " as no reference is set");
			}
		}

		controller.setCurrentMenu(menu);
		controller.reset();

	}



}