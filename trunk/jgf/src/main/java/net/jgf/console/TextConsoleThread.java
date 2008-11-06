/**
 * $Id$
 * Java Game Framework
 */

package net.jgf.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import org.apache.log4j.Logger;


public class TextConsoleThread extends Thread {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(TextConsoleThread.class);

	/**
	 * The console implementation that is served by this wrapper.
	 */
	private Console console;

	/**
	 * Constructor
	 * @param console
	 */
	public TextConsoleThread(Console console) {
		super("TextConsoleThread");
		this.console = console;
	}

	/**
	 * Text Mode
	 */
	@Override
	public void run() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(java.lang.System.in));

		String prompt = null;

		try {
			// TODO: Use a proper exit condition
			while (true) {
				prompt = reader.readLine();
				// TODO: Maybe use the GL-Thread to process the Command? Option? Leave that up for particular commands? Is the underlying console thread safe?
				console.processCommand(prompt);
			}
		} catch (IOException e) {
			logger.error ("IOException while reading the console. Text console dying.", e);
		}

	}




}
