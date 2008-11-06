
package net.jgf.logic;

import net.jgf.core.state.State;

	/**
   *
   */
public interface LogicState extends State {

	/**
	 * Updates this LogicState.
	 * <p>This method is responsible of checking
	 * @param tpf seconds elapsed since last update
	 */
	public void update(float tpf);

}
