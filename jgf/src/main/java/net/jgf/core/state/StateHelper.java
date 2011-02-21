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
		if (!state.isLoaded()) state.load();
		if (!state.isActive()) state.activate();
	}
	
	public static void deactivateAndUnload(State state) {
		if (state.isActive()) state.deactivate();
		if (state.isLoaded()) state.unload();
	}

	public static void loadAndActivate (String stateId) {
		State state = Jgf.getDirectory().getObjectAs(stateId, State.class);
		StateHelper.loadAndActivate(state);
	}

	public static void deactivateAndUnload(String stateId) {
		State state = Jgf.getDirectory().getObjectAs(stateId, State.class);
		StateHelper.deactivateAndUnload(state);
	}
	
	public static void activate(State state) {
	    if (!state.isActive()) state.activate();
	}
	
	public static void deactivate(State state) {
	    if (state.isActive()) state.deactivate();
	}
	
}
