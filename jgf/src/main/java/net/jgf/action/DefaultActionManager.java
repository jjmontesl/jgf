
package net.jgf.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

/**
 */
@Configurable
public class DefaultActionManager extends BaseService implements ActionManager {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DefaultActionManager.class);

	private static final int INITIAL_ACTION_CAPACITY = 128;

	/**
	 * List of actions
	 */
	protected Map<String,Action> actions;



	public DefaultActionManager() {
		super();
		actions = new HashMap<String, Action>(INITIAL_ACTION_CAPACITY);
	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		List<Action> list = ConfigurableFactory.newListFromConfig(config, configPath + "/action", Action.class);
		for (Action action : list) {
			actions.put(action.getId(), action);
			Jgf.getDirectory().addObject(action.getId(), action);
		}

	}


	@Override
	public void dispose() {
		actions = null;
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.service.BaseService#initialize()
	 */
	@Override
	public void initialize() {
		super.initialize();
	}


	@Override
	public Action getAction(String name) {
		Action action = actions.get(name);
		if (action == null) {
			throw new ConfigException ("No action found with name '" + name + "' in " + this);
		}
		return action;
	}

}
