
package net.jgf.view;



import net.jgf.config.Config;
import net.jgf.core.state.BaseStateNode;

import org.apache.log4j.Logger;

/**
 *
 */
public abstract class BaseViewStateNode extends BaseStateNode<ViewState> implements ViewState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseViewStateNode.class);

	@Override
	public void render(float tpf) {
		for (ViewState viewState : children) {
			if (viewState.isActive()) viewState.render(tpf);
		}
	}


	@Override
	public void update(float tpf) {
		for (ViewState viewState : children) {
			if (viewState.isActive()) viewState.update(tpf);
		}
	}

	@Override
	public void input(float tpf) {
		for (ViewState viewState : children) {
			if (viewState.isActive()) viewState.input(tpf);
		}
	}



	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath, "view", ViewState.class);


	}

}
