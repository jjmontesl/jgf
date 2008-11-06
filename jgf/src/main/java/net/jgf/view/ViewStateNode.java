
package net.jgf.view;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.state.BaseStateNode;

import org.apache.log4j.Logger;

/**
 *
 */
// TODO: This should be an interface????
@Configurable
public class ViewStateNode extends BaseStateNode<ViewState> implements ViewState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ViewStateNode.class);

	@Override
	public void render(float tpf) {
		if (this.isActive() && this.isLoaded()) {
			for (ViewState viewState : children) {
				viewState.render(tpf);
			}
		}

	}


	@Override
	public void update(float tpf) {
		if (this.isActive() && this.isLoaded()) {
			for (ViewState viewState : children) {
				viewState.update(tpf);
			}
		}
	}

	@Override
	public void input(float tpf) {
		if (this.isActive() && this.isLoaded()) {
			for (ViewState viewState : children) {
				viewState.input(tpf);
			}
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
