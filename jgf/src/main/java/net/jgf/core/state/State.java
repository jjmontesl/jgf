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
 * the LogicStates and the ViewStates. Also, Entities are also considered states.</p>
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
 * <h3>Activaton and Loading Awareness</h3>
 * <p>States are responsible for managing their state (active and loaded) and
 * act accordingly. When the state tree is walked, the state methods must be called
 * regardless of whether the state is active or inactive. It is up to the state to
 * ignore the call if it is inactive.</p>
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

	public boolean isAutoActivate();

	public boolean isAutoLoad();

}