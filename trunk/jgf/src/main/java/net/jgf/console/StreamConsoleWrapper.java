/**
 * $Id: StreamConsoleWrapper.java,v 1.2 2008/02/09 22:21:20 jjmontes Exp $
 * Java Game Framework
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
 * <p>This is a wrapper for the StandardGameEngine that is able to accept one character at a time.</p>
 * <p>It can be used for viewStates that are not line based. It manages a current command line.</p>
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
