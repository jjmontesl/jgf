
package net.jgf.jme.view;

import java.util.ArrayList;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.logic.action.LogicAction;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;

/**
 */
@Configurable
public final class ActionInputView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(ActionInputView.class);

	/**
	 * The KeyInput to bind in order to activate and deactivate.
	 */
	protected InputHandler inputHandler;
	

	
	public class ActionInputKey {
		
		public int key;
		
		public LogicAction action;

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public LogicAction getAction() {
			return action;
		}

		public void setAction(LogicAction action) {
			this.action = action;
		}
		
	}
	
	protected ArrayList<ActionInputKey> actions = new ArrayList<ActionInputKey>();

	/**
	 * Key action
	 */
	public class KeyInputAction extends InputAction {

		public void performAction(InputActionEvent evt) {

			//logger.info("Key pressed (index=" + evt.getTriggerIndex() + ",time=" + evt.getTime() + ",press=" + evt.getTriggerPressed() + ")");

			for (ActionInputKey aik : actions) {
				if ((evt.getTriggerIndex() == aik.key) && (evt.getTriggerPressed())) {
					if (aik.action == null) {
						logger.error ("No action was set for key event [" + aik.key  + "] at " + this);
					} else {
						logger.debug ("Performing action: " + aik.action);
						aik.action.perform(null);
					}
				}
			}
			
		}

	}
	
	/**
	 * Simply calls the underlying View render method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#render(float)
	 */
	@Override
	public void render(float tpf) {
		// Nothing to render
	}

	/**
	 * Simply calls the underlying View update method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#update(float)
	 */
	@Override
	public void update(float tpf) {
		// Nothing to do
	}

	@Override
	public void input(float tpf) {

		inputHandler.update(tpf);

	}

	/**
	 * Calls the underlying View cleanup method and unloads this GameStateWrapperView.
	 * @see net.jgf.core.state.BaseState#unload()
	 */
	@Override
	public void unload() {
		inputHandler.clearActions();
		inputHandler = null;
		super.unload();
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {

		super.load();
		inputHandler = new InputHandler();
		inputHandler.addAction(new KeyInputAction(), InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);

	}
	
	
	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		int index = 1;
		while (config.containsKey(configPath + "/action[" + index + "]/@key")) {
			ActionInputKey aik = new ActionInputKey();
			aik.key = JmeConfigHelper.getKeyInput(config, configPath + "/action[" + index + "]/@key", 0);
			String actionRef = config.getString(configPath + "/action[" + index + "]/@ref");
			Jgf.getDirectory().register(aik, "action", actionRef);
			actions.add(aik);
			index++;
		}
		
	}


}
