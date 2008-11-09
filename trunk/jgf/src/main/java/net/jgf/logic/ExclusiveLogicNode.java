
package net.jgf.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateLifecycleEvent;
import net.jgf.core.state.StateObserver;
import net.jgf.core.state.StateLifecycleEvent.LifecycleEventType;


/**
 *
 */
@Configurable
public class ExclusiveLogicNode extends BaseLogicStateNode implements StateObserver {

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseStateNode#attachChild(net.jgf.core.state.State)
	 */
	@Override
	public void attachChild(LogicState state) {
		super.attachChild(state);
		state.addStateObserver(this);
	}

	@Override
	public void onStateLifecycle(StateLifecycleEvent evt) {

		if (evt.getType() == LifecycleEventType.Activate) {

			// Count number of active children
			int activeCount = 0;
			for (LogicState state : children) {
				if (state.isActive()) activeCount++;
			}

			// If more than one, disable all but last
			if (activeCount > 1)  {
				for (LogicState state : children) {
					if ((state.isActive()) && (state != evt.getSource())) state.deactivate();
				}
			}

		}

	}

}
