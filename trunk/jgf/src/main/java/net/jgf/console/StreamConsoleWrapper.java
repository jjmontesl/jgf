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

package net.jgf.console;

import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.console.bean.BeanshellConsole;
import net.jgf.core.UnsupportedOperationException;
import net.jgf.core.service.BaseService;

import org.apache.log4j.Logger;

/**
 * <p>This is a wrapper for the StandardGameEngine that is able to accept one character at a time.
 * It can be used when you need a character based console.</p>
 * <p>This wrappers manages the command line (current buffer, backspaces, enter) and
 * calls {@link Console#processCommand(String)} on the underlying console
 * when the user presses Enter.</p>
 * @see Console
 */
@Configurable
public class StreamConsoleWrapper extends BaseService implements Console {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(BeanshellConsole.class);

	/**
	 * Current command.
	 */
	private String command;

	/**
	 * StandardGameEngine wrapped by this StreamConsoleWrapper
	 */
	private Console console;


	/**
	 * Initializes this StreamConsoleWrapper.
	 */
	public StreamConsoleWrapper() {
		super();
		command = "";
	}


	/**
	 * Processes a single character, adding it to the current command.
	 */
	public void acceptChar(char character) {
		if (character == '\0') return;
		if (character == '\r') {
			processCommand(command);
			command = "";
		} else if (character == 8) {
			if (! command.equals("")) command = command.substring(0, command.length() - 1);
		} else if (character == '\t') {
			try {
				console.complete(command);
			} catch (UnsupportedOperationException e) {
				logger.debug("Console " + console + " doesn't support autocomplete.");
			}
		} else {
			command = command + character;
		}
	}


	/**
	 * Returns the current command.
	 */
	public String getCommand() {
		return command;
	}


	/**
	 * Sets the current command.
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Calls the corresponding method on the wrapped Console object.
	 * @see Console#addConsoleObserver(ConsoleObserver)
	 */
	@Override
	public void addConsoleObserver(ConsoleObserver listener) {
		console.addConsoleObserver(listener);

	}

	/**
	 * Calls the corresponding method on the wrapped Console object.
	 * @see Console#addLine(String)
	 */
	@Override
	public void addLine(String line) {
		console.addLine(line);
	}

	/**
	 * Calls the corresponding method on the wrapped Console object.
	 * @see Console#complete(String)
	 */
	@Override
	public List<String> complete(String expression) throws UnsupportedOperationException {
		return console.complete (expression);
	}

	/**
	 * Calls the corresponding method on the wrapped Console object.
	 * @see Console#getHistory(int)
	 */
	@Override
	public String getHistory(int offset) {
		return console.getHistory(offset);
	}

	/**
	 * Calls the corresponding method on the wrapped Console object.
	 * @see Console#getLastLines(int)
	 */
	@Override
	public List<String> getLastLines(int lines) {
		return console.getLastLines(lines);
	}

	/**
	 * Calls the corresponding method on the wrapped Console object.
	 * @see Console#getLastLines(int, int)
	 */
	@Override
	public List<String> getLastLines(int lines, int offset) {
		return console.getLastLines(lines, offset);
	}

	/**
	 * Calls the corresponding method on the wrapped Console object.
	 * @see Console#processCommand(String)
	 */
	@Override
	public void processCommand(String string) {
		console.processCommand(string);
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);
		console = ConfigurableFactory.newFromConfig(config, configPath + "/service", Console.class);

	}

	/**
	 * Disposing this Console wrapper first disposes the wrapped Console.
	 * @see net.jgf.core.service.BaseService#dispose()
	 */
	@Override
	public void dispose() {
		console.dispose();
		super.dispose();
	}

	/**
	 * Initializing this Console wrapper also initializes the wrapped Console.
	 * @see net.jgf.core.service.BaseService#initialize()
	 */
	@Override
	public void initialize() {
		super.initialize();
		console.initialize();
	}



}
