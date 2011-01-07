
package net.jgf.logic;

import org.apache.log4j.Logger;

import net.jgf.config.Configurable;
import net.jgf.core.state.BaseState;
import net.jgf.core.state.StateLifecycleEvent;
import net.jgf.core.state.StateObserver;
import net.jgf.core.state.StateLifecycleEvent.LifecycleEventType;


/**
 *
 */
@Configurable
public class ExclusiveLogicNode extends BaseLogicStateNode implements StateObserver {

    /**
     * Class logger
     */
    private static final Logger logger = Logger.getLogger(ExclusiveLogicNode.class);
    
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

	    logger.debug (this + " processing " + evt.getType() + " from " + evt.getSource());
	    
		if (evt.getType() == LifecycleEventType.Activate) {
		    
			// Count number of active children
			int activeCount = 0;
			for (LogicState state : children) {
				if (state.isActive()) activeCount++;
			}

			// If more than one, disable all but the originating state
			if (activeCount > 1)  {
				for (LogicState state : children) {
					if ((state.isActive()) && (state != evt.getSource())) state.deactivate();
				}
			}

		}

	}

}
