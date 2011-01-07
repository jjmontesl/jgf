package net.jgf.core.state;


import net.jgf.core.component.Component;
import net.jgf.entity.Entity;
import net.jgf.logic.LogicState;
import net.jgf.view.ViewState;

/**
 * <p>A State represents a piece of process that, when enabled, perform a task
 * along with other states that are also active. States are attached to a parent
 * state, forming a tree structure.</p>
 * <p>States are the core functionality for the state machines in JGF, which are
 * the LogicStates and the ViewStates. Entities are also states.</p>
 * <p>A <b>LogicStates</b> contain logic that is executed per frame...
 * (TODO: check about LogicStates after reviewing messaging)</p>
 * <p>A <b>ViewState</b> deals with the presentation part of the application.
 * A ViewState provides an update and a render method that are executed per frame.
 * The combination of active ViewStates provides the final appearance of the application.</p>
 *
 * (TODO: check about LogicStates after reviewing messaging)</p>
 *
 * <h3>Flow control: who updates the state trees</h3>
 *
 * <h3>State lifecycle</h3>
 *
 * <h3>Autoload and Autoactivate</h3>
 *
 * <h3>Repeated calls to methods</h3>
 *
 * <h3>Activation and Loading evaluation</h3>
 * <p>State methods are not called from the container if the state is not active. If you implement a
 * state that contains other states, you need to ensure you check if a state is active before calling
 * its update methods. Failure to do so may result in states performing tasks while inactive.</p>
 *
 * TODO: Study / document thread-safeness
 *
 * @see ViewState
 * @see LogicState
 * @see Entity
 * @author jjmontes
 */
public interface State extends Component {

	/**
	 * Loads resources and initializes this state.
	 */
	public void load();

	/**
	 * Unloads the resources used by this state. The state must be able to be recovered from this
	 * state by calling loaded again.
	 */
	public void unload();

	public boolean isLoaded();

	public void activate();

	public void deactivate();

	public boolean isActive();

	public void addStateObserver(StateObserver observer);

	public void removeStateObserver(StateObserver observer);
	
	public void clearStateObservers();

	public boolean isAutoActivate();

	public boolean isAutoLoad();

}