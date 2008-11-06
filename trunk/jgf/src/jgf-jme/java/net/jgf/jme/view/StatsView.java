package net.jgf.jme.view;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.input.KeyBindingManager;
import com.jmex.game.state.GameState;
import com.jmex.game.state.StatisticsGameState;

/**
 */
@Configurable
public final class StatsView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(StatsView.class);

	/**
	 * The associated GameState, once it is loaded.
	 */
	private StatisticsGameState gameState;

	/**
	 * The KeyInput to bind, if any, in order to activate and deactivate the statistics.
	 */
	private int activationKey;

	/**
	 * The graph width scale factor.
	 */
	private float graphWidth = 1.0f;

	/**
	 * The graph height scale factor.
	 */
	private float graphHeight = 0.5f;

	/**
	 * The graph float scale factor.
	 */
	private float graphAlpha = 0.5f;


	/**
	 * Simply calls the underlying GameState render method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#render(float)
	 */
	@Override
	public void render(float tpf) {
		if (this.isActive() && this.isLoaded() && gameState.isActive()) gameState.render(tpf);
	}

	/**
	 * Simply calls the underlying GameState update method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#update(float)
	 */
	@Override
	public void update(float tpf) {

		if (this.isActive() && this.isLoaded()) {

	    if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_stats", false)) {
	    		if (gameState.isActive())  {
	    			logger.debug("Hiding render statistics");
	    		} else  {
	    			logger.debug("Showing render statistics");
	    		}
	        gameState.setActive(!gameState.isActive());
	    }

	    if (gameState.isActive()) gameState.update(tpf);
		}

	}

	/**
	 * Calls the underlying GameState cleanup method and unloads this GameStateWrapperView.
	 * @see net.jgf.core.state.BaseState#unload()
	 */
	@Override
	public void unload() {
		// TODO: unload logging should be done by container??
		if (gameState != null) gameState.cleanup();
		gameState = null;
		super.unload();
	}

	/**
	 * <p>Creates and activates the JME GameState that is wrapped by this State.</p>
	 */
	@Override
	public void load() {

		super.load();

		// TODO: Parameterize StatisticsGameState from config
		gameState = new StatisticsGameState(this.id + "-sgs", graphWidth, graphHeight, graphAlpha, true);

		// KeyBindings
		if (activationKey != 0)  KeyBindingManager.getKeyBindingManager().set("toggle_stats", activationKey);


	}

	/**
	 * Returns the GameState wrapped by this GameStateWrapperView.
	 * @return the gameState (may be null if this state hasn't been loaded yet)
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

		activationKey = JmeConfigHelper.getKeyInput(config, configPath + "/activationKey", 0);
		graphWidth = config.getFloat(configPath + "/graphWidth", graphWidth);
		graphHeight = config.getFloat(configPath + "/graphHeight", graphHeight);
		graphAlpha = config.getFloat(configPath + "/graphAlpha", graphAlpha);

	}


}
