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
public class StateHelper {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(StateHelper.class);

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

	public static void loadAndActivate (String stateId) {
		State state = Jgf.getDirectory().getObjectAs(stateId, State.class);
		state.load();
		state.activate();
	}

	public static void deactivateAndUnload(String stateId) {
		State state = Jgf.getDirectory().getObjectAs(stateId, State.class);
		state.deactivate();
		state.unload();
	}


}
