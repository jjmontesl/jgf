
package net.jgf.menu.items;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.logic.action.LogicAction;
import net.jgf.system.Jgf;


/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class ActionMenuItem extends TextMenuItem {

	protected LogicAction action;

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
		action.perform(null);
	}

	/**
	 * @return the action
	 */
	public LogicAction getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(LogicAction action) {
		this.action = action;
	}



}