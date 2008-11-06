package net.jgf.entity;

import net.jgf.core.state.State;


/**
 * Each one of the game entities which are not part of the static map.
 */
public interface Entity extends State {


	/**
	 * Updates the entity, processing the entity's queue of commands
	 * and the dynamic part if needed.
	 */
	public abstract void update(float tpf);

}
