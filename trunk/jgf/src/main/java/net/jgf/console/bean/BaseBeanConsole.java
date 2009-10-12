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

import net.jgf.config.ConfigException;
import net.jgf.console.BaseConsole;
import net.jgf.console.Console;
import net.jgf.core.component.Component;
import net.jgf.core.service.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>BaseBeanConsole adds bean management functionality to the console. In other words,
 * this console holds a list of beans and is able to read it from configuration.</p>
 * <p>These beans can then be exposed through the derived class command interpreter,
 * like the {@link BeanshellConsole} does.</p>
 * <p>If you do not need this functionality, you can use the
 * parent class {@link BaseConsole}, which doesn't feature bean management,
 * however, you may consider to provide support for beans making them accessible
 * through your own console implementation, extending this class.</p>
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
	protected Hashtable<String, Component> beans;


	/**
	 * Constructor.
	 */
	public BaseBeanConsole() throws ServiceException {
		beans = new Hashtable<String, Component>();
	}


	/**
	 * Returns a bean from the list of beans managed by this console.
	 */
	public Component getBean(String beanId) {
		Component result = beans.get(beanId);
		if (result == null) {
			throw new ConfigException("Tried to retrieve a non existing with id '" + beanId + "' from console " + this);
		}
		return result;
	}

	/**
	 * Adds a bean to the list of beans managed by this console, for it to be exposed
	 * by the console and therefore be accessible through the console commands.
	 */
	public void addBean(Component component) {
		if (component == null) {
			throw new ConfigException("Tried to add a null component to the bean list of console " + this);
		}
		if (StringUtils.isBlank(component.getId())) {
			throw new ConfigException("Tried to add a component (" + component + ") with a blank id to the list of console " + this);
		}
		if (beans.containsKey(component.getId())) {
			throw new ConfigException("Tried to add an already existin bean '" + component + "' from console " + this);
		}
		beans.put(component.getId(), component);
	}

}
