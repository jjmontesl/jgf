/**
 * $Id$
 * Java Game Framework
 */

package net.jgf.core.state;

import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

/**
 * This GameStateNode is a GameStateNode.
 * from a properties file.
 */
public class StateUtil {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(StateUtil.class);

	public static void registerState (State state) {
		Jgf.getDirectory().addObject(state.getId(), state);
	}

	public static void loadAndActivate (State state) {
		state.load();
		state.activate();
	}

	public static void deactivateAndUnload(State state) {
		state.deactivate();
		state.unload();
	}

}
