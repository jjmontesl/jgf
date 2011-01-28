
package net.jgf.jme.view;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.jme.settings.KeySetting;
import net.jgf.settings.Setting;
import net.jgf.settings.SettingHandler;
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
	private SettingHandler<Integer> key = new SettingHandler<Integer>(KeySetting.class);
	
	private boolean toggling;

	/**
	 * Simply calls the underlying View render method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#render(float)
	 */
	@Override
	public void doRender(float tpf) {
		if (view.isActive()) view.render(tpf);
	}

	/**
	 * Simply calls the underlying View update method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#update(float)
	 */
	@Override
	public void doUpdate(float tpf) {
		if (view.isActive()) view.update(tpf);
	}

	@Override
	public void doInput(float tpf) {

	    if ( (! toggling) && (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle[" + this.getId() + "]", false)) ) {
	            toggling = true;
	    		if (view.isActive())  {
	    			view.deactivate();
	    		} else  {
	    			view.activate();
	    		}
	    		
	    } else {
	        if (! (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle[" + this.getId() + "]", false))) {
	            toggling = false;
	        }
	    }
	    
	    view.input(tpf);

	}

	/**
	 * Calls the underlying View cleanup method and unloads this GameStateWrapperView.
	 * @see net.jgf.core.state.State#doUnload()
	 */
	@Override
	public void doUnload() {
		view.unload();
		super.doUnload();
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#load()
	 */
	@Override
	public void doLoad() {

		super.doLoad();
		if (view.isAutoLoad()) view.load();

		// KeyBindings
		if (key.getValue() == 0) {
			throw new ConfigException("No key was specified for " + this + " (detected on loading)");
		} else {
			KeyBindingManager.getKeyBindingManager().set("toggle[" + this.getId() + "]", key.getValue());
		}

	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#setActive(boolean)
	 */
	// TODO: Where to check autoactivation
	@Override
	public void doActivate() {
		super.doActivate();
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

		key.readValue(config.getString(configPath + "/key", "KEY_F10"));
		
	}

    public int getKey() {
        return this.key.getValue();
    }

    public void setKey(int key) {
        this.key.setValue(key);
    }
	

}
