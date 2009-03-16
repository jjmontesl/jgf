/*
 * JGF - Java Game Framework
 * $Id$
 *
 * Copyright (c) 2008, JGF - Java Game Framework
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     * Neither the name of the 'JGF - Java Game Framework' nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <copyright holder> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <copyright holder> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.jgf.jme.engine;

import java.util.concurrent.Callable;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.IllegalStateException;
import net.jgf.core.service.ServiceException;
import net.jgf.engine.BaseEngine;
import net.jgf.engine.Engine;
import net.jgf.jme.view.devel.StatsView;
import net.jgf.logic.LogicManager;
import net.jgf.system.Jgf;
import net.jgf.view.ViewManager;

import org.apache.log4j.Logger;

import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.StandardGame.GameType;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;


/**
 * <p>The JMEEngine plugs jMonkeyEngine into the Java Game Framework.</p>
 * <p>JGF requires an Engine during application startup, which manages
 * the main game loop (see {@link Engine} for more information).</p>
 * <p>This implementation creates a JME StandardGame and initializes
 * the main game state that will call other services (like
 * the {@link LogicManager} and the {@link ViewManager}.</p>
 *
 * @see BaseEngine
 * @see Engine
 * @author jjmontes
 */
// TODO: Review why in the log appears: [LWJGLTimer] Timer resolution: **1000??** ticks per second
@Configurable
public final class JMEEngine extends BaseEngine {

	/**
	 * Class logger.
	 */
	private static final Logger logger = Logger.getLogger(JMEEngine.class);

	/**
	 * Reference to the underlying JME StandardGame.
	 */
	private StandardGame game = null;

	/**
	 * Reference to the main loop GameState that integrates JGF with JME.
	 */
	private StandardGameState standardGameState = null;

	/**
	 * Whether JME Engine will collect rendering statistics.
	 */
	private boolean collectStats = false;

	/**
	 * <p>Maximum time interval allowed between frames. If the time elapsed is bigger, it is capped
	 * to this value. The default is 0.02 (50 FPS).</p>
	 */
	private float timerCap = 0.02f;

	/**
	 * This class is the GameState that JGF uses to integrate with JME.
	 * @author jjmontes
	 */
	private class StandardGameState extends GameState {

		/* (non-Javadoc)
		 * @see com.jmex.game.state.GameStateNode#render(float)
		 */
		@Override
		public void render(float tpf) {
			// TODO: Where to cap
			if (tpf > timerCap) tpf = timerCap;
			viewManager.render(tpf);
		}

		/* (non-Javadoc)
		 * @see com.jmex.game.state.GameStateNode#update(float)
		 */
		@Override
		public void update(float tpf) {
			// TODO: Where to cap
			if (tpf > timerCap) tpf = timerCap;
			viewManager.input(tpf);
			logicManager.update(tpf);
			viewManager.update(tpf);
		}

		/* (non-Javadoc)
		 * @see com.jmex.game.state.GameState#cleanup()
		 */
		@Override
		public void cleanup() {
			JMEEngine.logger.info("Cleaning up StandardGame");
			Jgf.getApp().dispose();
		}

	}

	/**
	 * <p>The StandardGameInitTask performs initialization that needs to be done in the OpenGL thread.
	 * This Callable is enqueued to be the first task performed in JME  UPDATE queue.</p>
	 * @author jjmontes
	 */
	private class StandardGameInitTask implements Callable<Object> {

		/**
		 * Initializes JGF LogicManager and ViewManager.
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Object call() throws Exception {

			JMEEngine.logger.debug("Starting engine initialization task of " + this);

			logicManager.getRootState().load();
			viewManager.getRootState().load();
			logicManager.getRootState().activate();
			viewManager.getRootState().activate();

			return null;

		}

	}

	/* (non-Javadoc)
	 * @see net.jgf.engine.Engine#start()
	 */
	@Override
	public void start() {

		JMEEngine.logger.debug("Creating StandardGame");

		// StandardGame statistics collection
		if (collectStats) {
			JMEEngine.logger.warn("JME is collecting engine statistics (turn this feature <collectStats> off for production)");
			java.lang.System.setProperty("jme.stats", "set");
		}

		// Create StandardGame

		// TODO: resolve dedicated from config, as per configuration
		//boolean dedicated = System.getComponents().getComponentAs("settings", Settings.class).getBoolean("network.dedicatedServer");
		boolean dedicated = false;
		game = new StandardGame(Jgf.getApp().getName(),
				dedicated ? GameType.HEADLESS : GameType.GRAPHICAL);
		//StandardGame game = new StandardGame(Globals.APPLICATION_NAME, GameType.GRAPHICAL);

		//game.setDialogBehaviour((System.getSettings().getServer().isDedicatedServer()) ? 0 : 2);
		try {
			if (! dedicated) GameSettingsPanel.prompt(game.getSettings());
		} catch (InterruptedException e) {
			throw new ServiceException("Settings dialog interrupted", e);
		}
		game.getSettings().setStencilBits(4);

		// Set the queues to execute all pending tasks at once
		// TODO: Allow configuration of these
		// TODO: What is an appropriate default? What impact this really has?
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).setExecuteAll(true);
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).setExecuteAll(true);

		standardGameState = new StandardGameState();
		standardGameState.setActive(true);

		// Start
		JMEEngine.logger.trace("Starting StandardGame");
		game.start();

		GameStateManager.getInstance().attachChild(standardGameState);

		// Initial tasks on the OpenGL
		Callable<Object> initTask = new StandardGameInitTask();
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).enqueue(initTask);

	}

	/**
	 * Configures this object from the configuration file.
	 */
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);
		this.timerCap = config.getFloat(configPath + "/timerCap", timerCap);
		this.collectStats = config.getBoolean(configPath + "/collectStats", collectStats);
	}

	/**
	 * Disposing this service causes the main loop to finish.
	 * @see net.jgf.core.service.BaseService#dispose()
	 */
	@Override
	public void dispose() {
		if (disposed == false) {
			super.dispose();
			game.finish();
			game = null;
		}
	}

	/**
	 * <p>Returns whether JME will collect rendering statistics.</p>
	 * @see StatsView
	 */
	public boolean isCollectStats() {
		return collectStats;
	}

	/**
	 * <p>Sets whether JME will collect rendering statistics.</p>
	 * @see StatsView
	 */
	public void setCollectStats(boolean collectStats) {
		if (game != null) {
			throw new IllegalStateException("Collection of statistics cannot be enabled or disabled after engine initialization");
		}
		this.collectStats = collectStats;
	}

	/**
	 * <p>Returns the maximum time interval allowed between frames. If the time elapsed is
	 * bigger, it is capped to this value. The default is 0.02 (50 FPS).</p>
	 */
	protected float getTimerCap() {
		return timerCap;
	}

	/**
	 * <p>Sets the maximum time interval allowed between frames. If the time elapsed is bigger, it is capped
	 * to this value. The default is 0.02 (50 FPS).</p>
	 */
	protected void setTimerCap(float timerCap) {
		this.timerCap = timerCap;
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.component.BaseComponent#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[id=" + id +",timerCap=" + timerCap + ",collecStats=" + collectStats + "]";
	}

}
