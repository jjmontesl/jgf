/**
 * $Id$
 * Java Game Framework
 */

package net.jgf.core.state;

/**
 *
 */
public class StateLifecycleEvent {

	public enum LifecycleEventType {

		Activate,

		Deactivate,

		Load,

		Unload

	}



	protected LifecycleEventType type;

	protected State source;

	/**
	 * @return the type
	 */
	public LifecycleEventType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(LifecycleEventType type) {
		this.type = type;
	}

	/**
	 * @return the source
	 */
	public State getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(State source) {
		this.source = source;
	}

	public StateLifecycleEvent(LifecycleEventType type, State source) {
		super();
		this.type = type;
		this.source = source;
	}



}
