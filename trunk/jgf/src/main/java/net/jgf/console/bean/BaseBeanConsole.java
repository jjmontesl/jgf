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

package net.jgf.console.bean;

import java.util.Hashtable;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.ConfigurableFactory;
import net.jgf.console.BaseConsole;
import net.jgf.console.Console;
import net.jgf.core.component.Component;
import net.jgf.core.service.ServiceException;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.jme.view.ActionInputView.ActionInputKey;
import net.jgf.system.Jgf;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>BaseBeanConsole adds bean management functionality to the console.</p>
 * <p>In other words,
 * this console holds a list of beans and is able to read it from configuration.
 * These beans can then be exposed through the derived class command interpreter,
 * like {@link BeanshellConsole} does.</p>
 * <p>If you do not need this functionality, you can extend the
 * base class {@link BaseConsole}, which doesn't include this feature.</p>
 *
 * @see BeanshellConsole
 * @author jjmontes
 */
public abstract class BaseBeanConsole extends BaseConsole implements Console {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseBeanConsole.class);

	/**
	 * The set of beans that are published in this console
	 */
	protected Hashtable<String, Object> beans;


	/**
	 * Constructor.
	 */
	public BaseBeanConsole() throws ServiceException {
		beans = new Hashtable<String, Object>();
	}


	/**
	 * Returns a bean from the list of beans managed by this console.
	 */
	public Object getBean(String beanId) {
		Object result = beans.get(beanId);
		if (result == null) {
			throw new ConfigException("Tried to retrieve a non existing with id '" + beanId + "' from console " + this);
		}
		return result;
	}

	/**
	 * Adds a bean to the list of beans managed by this console, for it to be exposed
	 * by the console and therefore be accessible through the console commands.
	 */
	public void addBean(String id, Object bean) {
		if (bean == null) {
			throw new ConfigException("Tried to add a null object to the bean list of console " + this);
		}
		if (StringUtils.isBlank(id)) {
			throw new ConfigException("Tried to add object (" + bean + ") with a blank id to the bean list of console " + this);
		}
		if (beans.containsKey(id)) {
			throw new ConfigException("Tried to add an already existin bean '" + bean + "' from console " + this);
		}
		beans.put(id, bean);
	}
	
	@Override
	public void readConfig(Config config, String configPath) {
		super.readConfig(config, configPath);
		
		int index = 1;
		while (config.containsKey(configPath + "/bean[" + index + "]/@name")) {
			
			String name = config.getString(configPath + "/bean[" + index + "]/@name");
			Object bean = ConfigurableFactory.newFromConfig(config, configPath + "/bean[" + index + "]", Object.class);
			addBean(name, bean);
			
			index++;
		}
		
	}

}
