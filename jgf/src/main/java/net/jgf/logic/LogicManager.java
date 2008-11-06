
package net.jgf.logic;

import net.jgf.core.state.State;



/**
 *
 */
public interface LogicManager {

	public void update(float tpf);

	/**
	 * @return the rootState
	 */
	public State getRootState();

	/**
	 * @param rootState the rootState to set
	 */
	public void setRootState(LogicState rootState);


}
