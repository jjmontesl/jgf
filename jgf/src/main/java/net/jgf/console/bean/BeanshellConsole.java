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

import java.util.List;
import java.util.Map.Entry;

import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.core.UnsupportedOperationException;

import org.apache.log4j.Logger;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * <p>BeanshellConsole implements a Console that interprets Beanshell commands
 * (BeanShell, also called <i>bsh</i>, is a scripting language for Java).</p>
 * <p>BeanshellConsole is a {@link BaseBeanConsole}), which means that it
 * manages a set of beans that provide the actual console commands./p>
 * <p>This console accepts commands with the following syntax:</p>
 * <p><tt>tools.showSceneMonitor()</tt><br />
 * <tt>game.start("map")</tt></p>
 * <p>Note: For information about Beanshell please refer
 * <a href="http://www.beanshell.org/">http://www.beanshell.org/</a>.
 *
 * @see BaseBeanConsole
 * @author jjmontes
 */
@Configurable
public class BeanshellConsole extends BaseBeanConsole {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(BeanshellConsole.class);

	/**
	 * The beanshell interpreter
	 */
	protected Interpreter interpreter;

	/* (non-Javadoc)
	 * @see net.jgf.console.Console#processString(java.lang.String)
	 */
	@Override
	public void processCommand (String string) {

		// Remember to call parent processCommand
		super.processCommand(string);

		// Process the command
		try {
			Object result = interpreter.eval(string);
			if (result != null) {
				this.addLine(result.toString());
			}
		} catch (EvalError e) {
			this.addLine(e.toString());
		}
	}

	/* (non-Javadoc)
	 * @see net.jgf.console.Console#complete(java.lang.String)
	 */
	@Override
	public List<String> complete(String expression) throws UnsupportedOperationException{
		throw new UnsupportedOperationException("BeanshellConsole doesn't support completing expressions.");
	}


	/* (non-Javadoc)
	 * @see net.jgf.core.service.BaseService#initialize()
	 */
	@Override
	public void initialize() {
		interpreter = new Interpreter();
		for (Entry<String, Object> bean : beans.entrySet()) {
			registerBean(bean.getKey(), bean.getValue());
		}
	}

	/**
	 * Registers a bean with the interpreter.
	 */
	protected void registerBean(String id, Object bean) {
		try {
		    logger.debug("setting bean with id " + id);
			interpreter.set(id, bean);
		} catch (EvalError e) {
			throw new ConfigException("Could not set the bean " + id + " to the console beanshell intepreter", e);
		}
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.service.BaseService#dispose()
	 */
	@Override
	public void dispose() {
		interpreter = null;
		super.dispose();
	}

}
