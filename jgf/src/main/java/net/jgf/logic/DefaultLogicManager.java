
package net.jgf.logic;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.core.service.ServiceException;
import net.jgf.core.state.StateHelper;


/**
 *
 */
@Configurable
public class DefaultLogicManager extends BaseService implements LogicManager {

	LogicState rootState;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.rootState = ConfigurableFactory.newFromConfig(config, configPath + "/logic", LogicState.class);
		StateHelper.registerState(rootState);

	}


	@Override
	public void dispose() {
		super.dispose();
		StateHelper.deactivateAndUnload(rootState);
	}

	/**
	 * @param tpf
	 * @see net.jgf.logic.LogicState#update(float)
	 */
	@Override
	public void update(float tpf) {
		if (rootState.isActive()) rootState.update(tpf);
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.service.BaseService#initialize()
	 */
	@Override
	public void initialize() {
		super.initialize();
		if (rootState == null) throw new ServiceException("No LogicState defined for " + this);
	}

	/**
	 * @return the rootState
	 */
	public LogicState getRootState() {
		return rootState;
	}

	/**
	 * @param rootState the rootState to set
	 */
	public void setRootState(LogicState rootState) {
		this.rootState = rootState;
	}

}
