
package net.jgf.view;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.logic.action.LogicAction;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class ActionView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(ActionView.class);
	
	protected LogicAction action;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);
		Jgf.getDirectory().register(this, "action", config.getString(configPath + "/action/@ref"));

	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void doUpdate(float tpf) {
		super.doUpdate(tpf);

		// Perform action
		if (action != null) {
			action.perform(null);
		} else {
			logger.error ("No action set when performing action at " + this);
		}

		this.deactivate();
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
