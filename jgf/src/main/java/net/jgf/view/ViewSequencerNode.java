
package net.jgf.view;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.state.StateLifecycleEvent;
import net.jgf.core.state.StateObserver;
import net.jgf.core.state.StateLifecycleEvent.LifecycleEventType;


/**
 *
 */
@Configurable
public class ViewSequencerNode extends BaseViewStateNode implements StateObserver {


	protected boolean unloadAfterLast;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.setUnloadAfterLast(config.getBoolean(configPath + "/unloadAfterLast", isUnloadAfterLast()));

	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseStateNode#attachChild(net.jgf.core.state.State)
	 */
	@Override
	public void attachChild(ViewState state) {
		super.attachChild(state);
		state.addStateObserver(this);
	}



	/**
	 * ViewSequencerNode activates the first child when it is activated, so the sequence starts.
	 * @see net.jgf.core.state.BaseStateNode#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		if (children.size() > 0) children.get(0).activate();
	}



	@Override
	public void onStateLifecycle(StateLifecycleEvent evt) {

		if (evt.getType() == LifecycleEventType.Deactivate) {

			// Find the state
			int found = -1;
			for (int i = 0; ((i < children.size()) && (found < 0)); i++) {
				if (children.get(i) == evt.getSource()) {
					found = i;
				}
			}

			// Enable the following state
			if (found >= 0) {
				if (children.size() > found + 1) {
					children.get(found+1).activate();
				}
			} else {
				// Last state: cleanup
				this.deactivate();
				if (unloadAfterLast) {
					this.unload();
				}
			}


		}

	}

	/**
	 * @return the unloadAfterLast
	 */
	public boolean isUnloadAfterLast() {
		return unloadAfterLast;
	}

	/**
	 * @param unloadAfterLast the unloadAfterLast to set
	 */
	public void setUnloadAfterLast(boolean unloadAfterLast) {
		this.unloadAfterLast = unloadAfterLast;
	}



}
