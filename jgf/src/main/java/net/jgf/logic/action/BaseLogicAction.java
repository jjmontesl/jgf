
package net.jgf.logic.action;

import net.jgf.logic.BaseLogicState;

import org.apache.log4j.Logger;

/**
 */
public abstract class BaseLogicAction extends BaseLogicState implements LogicAction {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseLogicAction.class);

	@Override
	public void update(float tpf) {

	}

	@Override
	public abstract void perform (String string);

}
