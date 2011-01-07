/**
 * $Id: BaseState.java 203 2010-12-10 03:06:18Z jjmontes $
 * Java Game Framework
 */

package net.jgf.core.state;

import java.util.ArrayList;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.core.component.BaseComponent;
import net.jgf.core.state.StateLifecycleEvent.LifecycleEventType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
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
		this(null);
	}

	public BaseState(String id) {
		super(id);
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
	public final void activate() {
		
		if (!this.loaded) throw new IllegalStateException("Activating unloaded state " + this);
		
		if (this.active) throw new IllegalStateException("Activating already active state " + this);
		
		BaseState.logger.debug("Activating " + this);

        this.active = true;
		
		this.doActivate();

		// Notify observers
		for (StateObserver observer : stateObservers) {
			observer.onStateLifecycle(new StateLifecycleEvent(LifecycleEventType.Activate, this));
		}

	}

	public void doActivate() {
	    
	}
	

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#deactivate()
	 */
	@Override
	public final void deactivate() {
		
		if (!this.loaded) throw new IllegalStateException("Deactivating unloaded state " + this);
		
		if (this.isActive()) {
		
		    logger.debug("Deactivating " + this);
		    
            this.active = false;
    		
    		this.doDeactivate();
    
    		// Notify observers
    		for (StateObserver observer : stateObservers) {
    			observer.onStateLifecycle(new StateLifecycleEvent(LifecycleEventType.Deactivate, this));
    		}
		} else {
		    logger.debug("Skipping deactivation of already deactivated state " + this);
		}
	}
	
	public void doDeactivate() {
	    
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.autoLoad = config.getBoolean(configPath + "/@autoLoad", this.autoLoad);
		this.autoActivate = config.getBoolean(configPath + "/@autoActivate", this.autoActivate);

	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#isLoaded()
	 */
	@Override
	public final boolean isLoaded() {
		return loaded;
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#load()
	 */
	@Override
	public final void load() {

		if (! this.isLoaded()) {
		    
		    BaseState.logger.debug("Loading " + this);
    	      
		    if (StringUtils.isBlank(this.getId())) {
		        throw new ConfigException("Illegal blank id in state " + this + " (detected at State.load())");
    	    }
		
		    this.loaded = true;
    		
    		this.doLoad();
    		
    	      // Notify observers
            for (StateObserver observer : stateObservers) {
                observer.onStateLifecycle(new StateLifecycleEvent(LifecycleEventType.Load, this));
            }
		
		} else {
		    logger.debug("Skipping state load of already loaded state " + this);
		}
	}

	public void doLoad() {
	    
	}
	
	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#unload()
	 */
	// TODO: Should automatically deactivate on unload? yes! state lifecycle!
	@Override
	public final void unload() {
		
		// TODO: Document this behaviour
		if (!this.loaded) {
		    logger.debug("Skipping unloading of already unloaded state " + this);
		    return;
		}
		
		BaseState.logger.debug("Unloading " + this);
		
		// TODO: Document this behaviour
		if (this.active) this.deactivate();
		
		this.loaded = false;
		
		this.doUnload();
	     
		// Notify observers
        for (StateObserver observer : stateObservers) {
            observer.onStateLifecycle(new StateLifecycleEvent(LifecycleEventType.Unload, this));
        }
	}
	
	public void doUnload() {
	    
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[id=" + this.getId() + ",loaded=" + this.loaded + ",active=" + this.active + "]";
	}



	/**
	 * @return the autoLoad
	 */
	@Override
	public final boolean isAutoLoad() {
		return autoLoad;
	}

	/**
	 * @param autoLoad the autoLoad to set
	 */
	public final void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
	}

	/* (non-Javadoc)
	 * @see net.jgf.logic.LogicState#addObserver(net.jgf.logic.LogicStateObserver)
	 */
	@Override
	public final void addStateObserver(StateObserver observer) {
		this.stateObservers.add(observer);
	}

	/* (non-Javadoc)
	 * @see net.jgf.logic.LogicState#removeObserver(net.jgf.logic.LogicStateObserver)
	 */
	@Override
	public final void removeStateObserver(StateObserver observer) {
		this.stateObservers.remove(observer);
	}

	@Override
	public final void clearStateObservers() {
		this.stateObservers.clear();
	}
	
	/**
	 * @return the autoActivate
	 */
	public final boolean isAutoActivate() {
		return autoActivate;
	}

	/**
	 * @param autoActivate the autoActivate to set
	 */
	public final void setAutoActivate(boolean autoActivate) {
		this.autoActivate = autoActivate;
	}



}
