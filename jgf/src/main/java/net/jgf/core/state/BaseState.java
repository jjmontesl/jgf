/**
 * $Id$
 * Java Game Framework
 */

package net.jgf.core.state;

import java.util.ArrayList;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.core.component.BaseComponent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * This GameStateNode is a GameStateNode.
 * from a properties file.
 */
public abstract class BaseState extends BaseComponent implements State {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(BaseState.class);

	protected boolean active;

	protected boolean loaded;

	protected boolean autoLoad;

	protected boolean autoActivate;

	protected ArrayList<StateObserver> stateObservers;

	public BaseState() {
		stateObservers = new ArrayList<StateObserver>();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#isActive()
	 */
	@Override
	public boolean isActive() {
		return active;
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#setActive(boolean)
	 */
	@Override
	public void activate() {
		if (!this.loaded) throw new IllegalStateException("Activating unloaded state " + this);
		BaseState.logger.debug("Activating " + this);
		this.active = true;

		// Notify observers
		for (StateObserver observer : stateObservers) {
			observer.onActivated(this);
		}

	}


	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#deactivate()
	 */
	@Override
	public void deactivate() {
		if (!this.loaded) throw new IllegalStateException("Deactivating unloaded " + this);
		this.active = false;

		BaseState.logger.debug("Deactivating " + this);

		// Notify observers
		for (StateObserver observer : stateObservers) {
			observer.onDeactivated(this);
		}
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.autoLoad = config.getBoolean(configPath + "/autoLoad", this.autoLoad);
		this.autoActivate = config.getBoolean(configPath + "/autoActivate", this.autoActivate);

	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#isLoaded()
	 */
	@Override
	public boolean isLoaded() {
		return loaded;
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#load()
	 */
	@Override
	public void load() {
		BaseState.logger.debug("Loading " + this);
		if (StringUtils.isBlank(this.getId())) {
			throw new ConfigException("Illegal blank id in state " + this + " (detected at State.load())");
		}
		this.loaded = true;
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#unload()
	 */
	// TODO: Should automatically deactivate on unload?
	@Override
	public void unload() {
		BaseState.logger.debug("Unloading " + this);
		this.loaded = false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[id=" + this.id + ",loaded=" + this.loaded + ",active=" + this.active + "]";
	}



	/**
	 * @return the autoLoad
	 */
	@Override
	public boolean isAutoLoad() {
		return autoLoad;
	}

	/**
	 * @param autoLoad the autoLoad to set
	 */
	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
	}

	/* (non-Javadoc)
	 * @see net.jgf.logic.LogicState#addObserver(net.jgf.logic.LogicStateObserver)
	 */
	@Override
	public void addStateObserver(StateObserver observer) {
		this.stateObservers.add(observer);
	}

	/* (non-Javadoc)
	 * @see net.jgf.logic.LogicState#removeObserver(net.jgf.logic.LogicStateObserver)
	 */
	@Override
	public void removeStateObserver(StateObserver observer) {
		this.stateObservers.remove(observer);
	}

	/**
	 * @return the autoActivate
	 */
	public boolean isAutoActivate() {
		return autoActivate;
	}

	/**
	 * @param autoActivate the autoActivate to set
	 */
	public void setAutoActivate(boolean autoActivate) {
		this.autoActivate = autoActivate;
	}



}
