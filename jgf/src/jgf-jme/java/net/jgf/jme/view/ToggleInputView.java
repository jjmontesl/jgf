
package net.jgf.jme.view;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;
import net.jgf.view.ViewState;

import org.apache.log4j.Logger;

import com.jme.input.KeyBindingManager;

/**
 */
@Configurable
public final class ToggleInputView extends BaseViewState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ToggleInputView.class);

	/**
	 * The wrapped ViewState
	 */
	protected ViewState view;

	/**
	 * The KeyInput to bind in order to activate and deactivate.
	 */
	private int key;

	/**
	 * Simply calls the underlying View render method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#render(float)
	 */
	@Override
	public void render(float tpf) {
		if (view.isActive()) view.render(tpf);
	}

	/**
	 * Simply calls the underlying View update method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#update(float)
	 */
	@Override
	public void update(float tpf) {
		if (view.isActive()) view.update(tpf);
	}

	@Override
	public void input(float tpf) {

    if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle[" + this.id + "]", false)) {
    		if (view.isActive())  {
    			view.deactivate();
    		} else  {
    			view.activate();
    		}
    }

	}

	/**
	 * Calls the underlying View cleanup method and unloads this GameStateWrapperView.
	 * @see net.jgf.core.state.BaseState#unload()
	 */
	@Override
	public void unload() {
		// TODO: unload logging should be done by container??
		view.unload();
		Jgf.getDirectory().removeObject(view.getId());
		view = null;
		super.unload();
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {

		super.load();
		if (view.isAutoLoad()) view.load();

		// KeyBindings
		if (key == 0) {
			throw new ConfigException("No key was specified for " + this + " (detected on loading)");
		} else {
			KeyBindingManager.getKeyBindingManager().set("toggle[" + this.id + "]", key);
		}

	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#setActive(boolean)
	 */
	// TODO: Where to check autoactivation
	@Override
	public void activate() {
		super.activate();
		if (view.isAutoActivate()) view.activate();
	}

	/**
	 * Returns the View wrapped by this ToggleInputView.
	 * @return the view (may be null if this state hasn't been loaded yet)
	 */
	public ViewState getView() {
		return view;
	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		view = ConfigurableFactory.newFromConfig(config, configPath + "/view", ViewState.class);
		Jgf.getDirectory().addObject(view.getId(), view);

		key = JmeConfigHelper.getKeyInput(config, configPath + "/key", 0);

	}


}
