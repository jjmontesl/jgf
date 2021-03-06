package net.jgf.jme.view.devel;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

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
	public void doRender(float tpf) {
		gameState.render(tpf);
		// TODO: Is this advisable or needed?
		//DisplaySystem.getDisplaySystem().getRenderer().renderQueue();
	}

	/**
	 * Simply calls the underlying GameState update method (if this state is active and loaded).
	 * @see net.jgf.core.state.State#update(float)
	 */
	@Override
	public void doUpdate(float tpf) {

		gameState.update(tpf);

	}

	/**
	 * Calls the underlying GameState cleanup method and unloads this GameStateWrapperView.
	 * @see net.jgf.core.state.State#doUnload()
	 */
	@Override
	public void doUnload() {
		// TODO: unload logging should be done by container??
		if (gameState != null) gameState.cleanup();
		gameState = null;
		super.doUnload();
	}

	/**
	 * <p>Creates and activates the JME GameState that is wrapped by this State.</p>
	 */
	@Override
	public void doLoad() {

		super.doLoad();

		// TODO: Parameterize StatisticsGameState from config
		gameState = new StatisticsGameState(this.getId() + "-sgs", graphWidth, graphHeight, graphAlpha, true);

	}

	
	
	@Override
	public void doActivate() {
		super.doActivate();
		gameState.setActive(true);
	}

	@Override
	public void doDeactivate() {
		super.deactivate();
		gameState.setActive(false);
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

		graphWidth = config.getFloat(configPath + "/graphWidth", graphWidth);
		graphHeight = config.getFloat(configPath + "/graphHeight", graphHeight);
		graphAlpha = config.getFloat(configPath + "/graphAlpha", graphAlpha);

	}


}
