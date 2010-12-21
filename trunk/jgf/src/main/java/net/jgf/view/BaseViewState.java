
package net.jgf.view;



import net.jgf.core.state.BaseState;

import org.apache.log4j.Logger;

/**
 *
 */
public abstract class BaseViewState extends BaseState implements ViewState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseViewState.class);

	@Override
	public void render(float tpf) {
	}

	@Override
	public void update(float tpf) {
	}

	@Override
	public void input(float tpf) {
	}

}
