
package net.jgf.menu.items;

import net.jgf.action.Action;
import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class ActionMenuItem extends TextMenuItem {

	protected Action action;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		Jgf.getDirectory().register(this, "action", config.getString(configPath + "/action/@ref"));

	}

	@Override
	public boolean isNavigable() {
	  return true;
	}

	@Override
	public void perform() {
		action.perform();
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}



}