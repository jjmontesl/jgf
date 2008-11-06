/**
 * $Id$
 * Java Game Framework
 */

package net.jgf.core.state;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.jgf.config.Config;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.service.ServiceException;

import org.apache.log4j.Logger;


/**
 *
 */
public abstract class BaseStateNode<T extends State> extends BaseState implements StateNode<T> {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(BaseStateNode.class);

	/**
	 * Game logic root
	 */
	//protected ArrayList<T> children = new ArrayList<T>();

	protected List<T> children = new CopyOnWriteArrayList<T>();


	/* (non-Javadoc)
	 * @see net.jgf.logic.StateNode#addChild(net.jgf.logic.LogicState)
	 */
	public void attachChild(T state) {
		logger.debug("Adding child state " + state);
		if (state == null) {
			throw new ServiceException("Trying to add a null children state to " + this);
		}
		if (state.getId() == null) {
			throw new ServiceException("Trying to add a children state with 'null' id to " + this);
		}
		children.add(state);
	}

	public void dettachChild(T state) {
		boolean removed = children.remove(state);
		if (!removed) {
			throw new ServiceException("Trying to dettach an unexistent children " + state + " from " + this);
		}
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#setActive(boolean)
	 */
	// TODO: Where to check autoactivation
	@Override
	public void activate() {
		super.activate();
		for (T state : children) {
			if (state.isAutoActivate()) state.activate();
		}
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.ViewState#load()
	 */
	// TODO: Where to check autoloading
	@Override
	public void load() {
		super.load();
		for (T state : children) {
			if (state.isAutoLoad()) state.load();
		}
	}

	/**
	 * <p>Also unloads all children unconditionally, even if they are not loaded.</p>
	 */
	// TODO: Check unloading policy
	@Override
	public void unload() {
		super.unload();
		for (T state : children) {
			state.unload();
		}
	}

	/**
	 * Configures this object from Config.
	 */
	public void readConfig(Config config, String configPath, String childElementName, Class<T> childElementClass) {

		super.readConfig(config, configPath);

		List<T> childStates = ConfigurableFactory.newListFromConfig(config, configPath + "/" + childElementName, childElementClass);
		for (T state : childStates) {
			StateUtil.registerState(state);
			this.attachChild(state);
		}

	}

}
