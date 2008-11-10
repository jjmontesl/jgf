
package net.jgf.action.control;




/**
 *
 */
public class ActionStep {

	protected ActionStepType type;

	protected String ref;

	/**
	 * @return the type
	 */
	public ActionStepType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ActionStepType type) {
		this.type = type;
	}

	/**
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}

}