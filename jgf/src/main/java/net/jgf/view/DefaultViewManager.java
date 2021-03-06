package net.jgf.view;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.BaseService;
import net.jgf.core.state.State;
import net.jgf.core.state.StateHelper;


/**
 *
 */
// TODO: There should be a BaseViewManager!!
@Configurable
public final class DefaultViewManager extends BaseService implements ViewManager {

	ViewState rootState = null;

	public DefaultViewManager() {
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewManager#update(float)
	 */
	public void update(float tpf) {
		if (rootState.isActive()) rootState.update(tpf);
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewManager#update(float)
	 */
	public void input(float tpf) {
		if (rootState.isActive()) rootState.input(tpf);
	}


	/* (non-Javadoc)
	 * @see net.jgf.view.ViewManager#render(float)
	 */
	public void render(float tpf) {
		if (rootState.isActive()) rootState.render(tpf);
	}

	@Override
	public void initialize() {
		super.initialize();
	}


	@Override
	public void dispose() {
		super.dispose();
		rootState.unload();
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.rootState = ConfigurableFactory.newFromConfig(config, configPath + "/view", ViewState.class);
		StateHelper.registerState(rootState);

	}


	/**
	 * @return
	 * @see net.jgf.core.state.State#isActive()
	 */
	public boolean isActive() {
		return rootState.isActive();
	}

	/**
	 * @return
	 * @see net.jgf.core.state.State#isLoaded()
	 */
	public boolean isLoaded() {
		return rootState.isLoaded();
	}

	/**
	 *
	 * @see net.jgf.view.ViewStateNode#doLoad()
	 */
	public void load() {
		rootState.load();
	}



	/**
	 *
	 * @see net.jgf.core.state.State#doActivate()
	 */
	public void activate() {
		rootState.activate();
	}

	/**
	 *
	 * @see net.jgf.core.state.State#deactivate()
	 */
	public void deactivate() {
		rootState.deactivate();
	}

	/**
	 *
	 * @see net.jgf.view.ViewStateNode#doUnload()
	 */
	public void unload() {
		rootState.unload();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewManager#getRootState()
	 */
	public State getRootState() {
		return rootState;
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewManager#setRootState(net.jgf.view.ViewState)
	 */
	public void setRootState(ViewState rootState) {
		this.rootState = rootState;
	}




}
