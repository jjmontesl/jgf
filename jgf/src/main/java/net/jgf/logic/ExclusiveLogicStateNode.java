
package net.jgf.logic;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.state.ExclusiveStateNode;


/**
 *
 */
@Configurable
public class ExclusiveLogicStateNode extends ExclusiveStateNode<LogicState> implements LogicState {

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath, "logic", LogicState.class);

	}

	@Override
	public void update(float tpf) {
		for (LogicState logic : children) {
			if (logic.isActive()) logic.update(tpf);
		}
	}



}
