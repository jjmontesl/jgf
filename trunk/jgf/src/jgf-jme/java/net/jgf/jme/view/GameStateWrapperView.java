
package net.jgf.jme.view;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.core.state.State;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jmex.game.state.GameState;

/**
 * <p>This state wraps a JME Game State ({@link com.jmex.game.state.GameState}) into a {@link State}, so
 * it can be used within the application.</p>
 * <p>See the example application "gamestatewrapper" for a reference of its configuration.</p>
 */
@Configurable
public final class GameStateWrapperView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(GameStateWrapperView.class);

	/**
	 * The class name of the associated GameState.
	 */
	protected String className;

	/**
	 * The associated GameState, once it is loaded.
	 */
	protected GameState gameState;

	/**
	 * The Class instance that represents the wrapped GameState type.
	 */
	transient protected Class<?> gamestateClass;



	/**
	 * Simply calls the underlying GameState render method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#render(float)
	 */
	@Override
	public void render(float tpf) {
		gameState.render(tpf);
	}

	/**
	 * Simply calls the underlying GameState update method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#update(float)
	 */
	@Override
	public void update(float tpf) {
		gameState.update(tpf);
	}

	/**
	 * Calls the underlying GameState cleanup method and unloads this GameStateWrapperView.
	 * @see net.jgf.core.state.BaseState#unload()
	 */
	@Override
	public void unload() {
		// TODO: unload logging should be done by container??
		gameState.cleanup();
		gameState = null;
		super.unload();
	}

	/**
	 * <p>Creates and activates the JME GameState that is wrapped by this State.</p>
	 */
	@Override
	public void load() {

		super.load();

		try {
			gamestateClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ConfigException ("Could not load GameState class '" + className + "'", e);
		}

		if (! GameState.class.isAssignableFrom(gamestateClass)) {
			throw new ConfigException ("Component " + this.getId() + " references a class " + className + " which is not a GameState");
		}

		try {
			gameState = (GameState) gamestateClass.newInstance();
		} catch (InstantiationException e) {
			throw new ConfigException ("Could not instantiate GameState class '" + className + "'", e);
		} catch (IllegalAccessException e) {
			throw new ConfigException ("Could not instantiate GameState class '" + className + "'", e);
		}

		gameState.setActive(true);

	}

	/**
	 * Returns the GameState wrapped by this GameStateWrapperView.
	 * @return the view (may be null if this state hasn't been loaded yet)
	 */
	public GameState getGameState() {
		return gameState;
	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);
		this.className = config.getString(configPath + "/gamestate");

	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}



}
