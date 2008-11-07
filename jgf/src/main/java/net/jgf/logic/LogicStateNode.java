
package net.jgf.logic;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.state.BaseStateNode;


/**
 *
 */
// TODO: This should be an interface???
@Configurable
public class LogicStateNode extends BaseStateNode<LogicState> implements LogicState {


	@Override
	public void update(float tpf) {
		for (LogicState logicState : children) {
			if (logicState.isActive()) logicState.update(tpf);
		}
	}



	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath, "logic", LogicState.class);


	}

}
