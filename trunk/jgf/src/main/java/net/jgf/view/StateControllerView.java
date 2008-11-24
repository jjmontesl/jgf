
package net.jgf.view;

import java.util.ArrayList;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.state.State;
import net.jgf.core.state.StateUtil;
import net.jgf.system.Jgf;


/**
 *
 */
@Configurable
public class StateControllerView extends BaseViewState {

	public enum StateActionType {
		loadAndActivate,
		deactivateAndUnload,
		load,
		unload,
		activate,
		deactivate
	}

	public class StateControllerAction {
		public StateActionType actionType;
		public String targetRef;

		public StateControllerAction(StateActionType actionType, String targetRef) {
			super();
			this.actionType = actionType;
			this.targetRef = targetRef;
		}

	}

	protected ArrayList<StateControllerAction> actions = new ArrayList<StateControllerAction>();

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		List<String> actions = config.getList(configPath + "/action/@ref");
		for (int i = 0; i < actions.size(); i++) {
			String actionString = config.getString(configPath + "/action[" + (i+1) + "]/@type");
			StateActionType action = StateActionType.valueOf(actionString);
			addAction(action, actions.get(i));
		}

	}

	/**
	 * <p>Performs the list of actions defined in this ControllerView. This ControlleView
	 * automatically disables itself after the first execution. This is a one-go view.
	 * The ControllerView can be activated again so its actions are performed again
	 * in the next update (note that actions are performed during the View update,
	 * and not at activation).</p>
	 * @see net.jgf.view.BaseViewState#update(float)
	 */
	@Override
	public void update(float tpf) {
		super.update(tpf);

		// Perform action
		for (StateControllerAction action : actions) {

			State state = Jgf.getDirectory().getObjectAs(action.targetRef, State.class);

			if (action.actionType == StateActionType.loadAndActivate) {
				StateUtil.loadAndActivate(state);
			} else if (action.actionType == StateActionType.deactivateAndUnload) {
				StateUtil.deactivateAndUnload(state);
			} else if (action.actionType == StateActionType.activate) {
				state.activate();
			} else if (action.actionType == StateActionType.deactivate) {
				state.deactivate();
			} else if (action.actionType == StateActionType.load) {
				state.load();
			} else if (action.actionType == StateActionType.unload) {
				state.unload();
			}
		}

		this.deactivate();
	}

	public void addAction(StateActionType actionType, String targetRef) {
		StateControllerAction action = new StateControllerAction(actionType, targetRef);
		actions.add(action);
	}

}
