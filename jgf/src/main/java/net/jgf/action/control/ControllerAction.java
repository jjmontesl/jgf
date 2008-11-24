
package net.jgf.action.control;

import java.util.ArrayList;
import java.util.List;

import net.jgf.action.Action;
import net.jgf.action.BaseAction;
import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.system.Jgf;


/**
 *
 */
@Configurable
public class ControllerAction extends BaseAction {



	protected List<ActionStep> steps = new ArrayList<ActionStep>(1);

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		int index = 0;
		while (config.containsKey(configPath + "/step[" + index + "]")) {
			ActionStep step = new ActionStep();
			step.setType(ActionStepType.valueOf(config.getString(configPath + "/step[" + index + "]/@type")));
			step.setRef(config.getString(configPath + "/step[" + index + "]/@ref"));
			steps.add(step);
			index++;
		}

	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void perform() {

		for (ActionStep step : steps) {

			Object ref = Jgf.getDirectory().getObjectAs(step.getRef(), Object.class);

			if (step.getType() == ActionStepType.RunAction) {
				((Action) ref).perform();
			}

		}
	}


}
