
package net.jgf.logic.action;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;


/**
 *
 */
// TODO: Register the steps so the references are always resolved for performance reasons
@Configurable
public class QuitAction extends BaseLogicAction {

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void perform(Object arg) {
		Jgf.getApp().dispose();
	}


}
