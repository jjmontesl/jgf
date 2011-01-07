
package net.jgf.logic;

import net.jgf.core.state.BaseState;

import org.apache.log4j.Logger;

/**
 */
public abstract class BaseLogicState extends BaseState implements LogicState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseLogicState.class);

	@Override
	public final void update(float tpf) {
	    if (this.active) doUpdate(tpf);
	}
	
	public void doUpdate(float tpf) {
	    
	}

}
