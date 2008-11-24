
package net.jgf.view;

import net.jgf.action.Action;
import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;


/**
 *
 */
@Configurable
public class RunActionView extends BaseViewState {

	protected Action action;

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
	public void update(float tpf) {
		super.update(tpf);

		// Perform action
		action.perform();

		this.deactivate();
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
