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

package net.jgf.engine;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.core.service.BaseService;
import net.jgf.jme.engine.JMEEngine;
import net.jgf.logic.LogicManager;
import net.jgf.system.System;
import net.jgf.view.ViewManager;

import org.apache.log4j.Logger;

/**
 * <p>This is a base implementation of a JGF {@link Engine}, which provides
 * the main game loop functionality.</p>
 * <p>This base implementation takes care of the references
 * to the {@link ViewManager} and the {@link LogicManager}.</p>
 * @see Engine
 * @see JMEEngine
 * @author jjmontes
 */
public abstract class BaseEngine extends BaseService implements Engine {

	/**
	 * Class logger.
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseEngine.class);

	/**
	 * Reference to the ViewManager.
	 */
	protected ViewManager viewManager;

	/**
	 * Named reference to the ViewManager.
	 */
	protected String viewManagerRef;

	/**
	 * Reference to the LogicManager.
	 */
	protected LogicManager logicManager;

	/**
	 * Named reference to the LogicManager.
	 */
	protected String logicManagerRef;

	/**
	 * Configures this object from the configuration.
	 * @see Configurable
	 */
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);
		this.viewManagerRef = config.getString(configPath + "/viewManager/@ref");
		this.logicManagerRef = config.getString(configPath + "/logicManager/@ref");
	}

	/* (non-Javadoc)
	 * @see net.jgf.engine.Engine#logic()
	 */
	@Override
	public LogicManager logicManager() {
		return logicManager;
	}


	/* (non-Javadoc)
	 * @see net.jgf.engine.Engine#states()
	 */
	@Override
	public ViewManager viewManager() {
		return viewManager;
	}

	/**
	 * Gets the named reference to the ViewManager
	 */
	public String getViewManagerRef() {
		return viewManagerRef;
	}

	/**
	 * Sets the named reference to the ViewManager.
	 */
	public void setViewManagerRef(String viewManagerRef) {
		this.viewManagerRef = viewManagerRef;
	}

	/**
	 * Gets the named reference to the LogicManager.
	 */
	public String getLogicManagerRef() {
		return logicManagerRef;
	}

	/**
	 * Sets the named reference to the LogicManager.
	 */
	public void setLogicManagerRef(String logicManagerRef) {
		this.logicManagerRef = logicManagerRef;
	}

	/**
	 * <p>On initialization, BaseEngine resolves the named references to
	 * the ViewManager and LogicManager.</p>
	 * @see net.jgf.core.service.BaseService#initialize()
	 */
	@Override
	public void initialize() {

		super.initialize();

		if (viewManagerRef == null) throw new ConfigException("No viewManager reference found in " + this);
		if (logicManagerRef == null) throw new ConfigException("No logicManager reference found in " + this);

		// Resolve references
		viewManager = System.getDirectory().getObjectAs(viewManagerRef, ViewManager.class);
		logicManager = System.getDirectory().getObjectAs(logicManagerRef, LogicManager.class);

	}



}
