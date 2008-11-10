
package net.jgf.view;

import net.jgf.action.Action;
import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.naming.DirectoryInjector;
import net.jgf.core.naming.DirectoryRef;


/**
 *
 */
@Configurable
public class RunActionView extends BaseViewState {

	protected Action action;

	protected String actionRef;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.setActionRef(config.getString(configPath + "/action/@ref", this.getActionRef()));

	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void update(float tpf) {
		super.update(tpf);

		// Perform action
		DirectoryInjector.inject(this);
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
	@DirectoryRef (field="actionRef")
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * @return the actionRef
	 */
	public String getActionRef() {
		return actionRef;
	}

	/**
	 * @param actionRef the actionRef to set
	 */
	public void setActionRef(String actionRef) {
		this.actionRef = actionRef;
	}



}
