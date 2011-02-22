/*
 *
 */

import net.jgf.system.Application;


/**
 * This is a simple entry point for the application.
 * This class needs to be moved to the appropriate package.
 */
public class Main {

	/**
	 * Entry point to the application. JGF applications should just create
	 * the Application object and call .start() on it.
	 * @see Application
	 */

	public static void main(String[] args) throws Exception {

		Application app = new Application("jgf.xml", args);
		app.start();

	}
}
