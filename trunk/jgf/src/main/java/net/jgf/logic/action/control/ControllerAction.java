
package net.jgf.logic.action.control;

import java.util.ArrayList;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.state.State;
import net.jgf.core.state.StateHelper;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.logic.action.LogicAction;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;


/**
 *
 */
// TODO: Register the steps so the references are always resolved for performance reasons
@Configurable
public class ControllerAction extends BaseLogicAction {

	/**
	 * Class logger.
	 */
	private static final Logger logger = Logger.getLogger(ControllerAction.class);

	protected List<ActionStep> steps = new ArrayList<ActionStep>(1);

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		int index = 1;
		while (config.containsKey(configPath + "/step[" + index + "]/@type")) {
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
	public void perform(Object arg) {

		logger.debug("Performing action " + this);

		for (ActionStep step : steps) {

			if (step.getType() == ActionStepType.runAction) {
				LogicAction ref = Jgf.getDirectory().getObjectAs(step.getRef(), LogicAction.class);
				ref.perform(arg);
			} else if (step.getType() == ActionStepType.loadAndActivate) {
				State state = Jgf.getDirectory().getObjectAs(step.getRef(), State.class);
				StateHelper.loadAndActivate(state);
			} else if (step.getType() == ActionStepType.deactivateAndUnload) {
				State state = Jgf.getDirectory().getObjectAs(step.getRef(), State.class);
				StateHelper.deactivateAndUnload(state);
			} else if (step.getType() == ActionStepType.activate) {
				State state = Jgf.getDirectory().getObjectAs(step.getRef(), State.class);
				state.activate();
			} else if (step.getType() == ActionStepType.deactivate) {
				State state = Jgf.getDirectory().getObjectAs(step.getRef(), State.class);
				state.deactivate();
			} else if (step.getType() == ActionStepType.load) {
				State state = Jgf.getDirectory().getObjectAs(step.getRef(), State.class);
				state.load();
			} else if (step.getType() == ActionStepType.unload) {
				State state = Jgf.getDirectory().getObjectAs(step.getRef(), State.class);
				state.unload();
			}

		}
	}


}