
package net.jgf.view;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.state.StateHelper;
import net.jgf.core.state.StateLifecycleEvent;
import net.jgf.core.state.StateObserver;
import net.jgf.core.state.StateLifecycleEvent.LifecycleEventType;


/**
 *
 */
@Configurable
public class ViewSequencerNode extends BaseViewStateNode implements StateObserver {

	/**
	 * <p>Whether this sequencer node has to unload itself when the last child
	 * is deactivated.</p>
	 */
	protected boolean unloadOnFinish;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.setUnloadOnFinish(config.getBoolean(configPath + "/unloadOnFinish", isUnloadOnFinish()));

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
	 * @see net.jgf.core.state.BaseStateNode#doActivate()
	 */
	@Override
	public void doActivate() {
		super.doActivate();
		if (children.size() > 0) StateHelper.activate(children.get(0));
	}



	/* (non-Javadoc)
	 * @see net.jgf.core.state.StateObserver#onStateLifecycle(net.jgf.core.state.StateLifecycleEvent)
	 */
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
				} else {
					// Last state: cleanup
					this.deactivate();
					if (unloadOnFinish) {
						this.doUnload();
					}
				}
			}


		}

	}

	/**
	 * @return the unloadOnFinish
	 */
	public boolean isUnloadOnFinish() {
		return unloadOnFinish;
	}

	/**
	 * @param unloadOnFinish the unloadOnFinish to set
	 */
	public void setUnloadOnFinish(boolean unloadOnFinish) {
		this.unloadOnFinish = unloadOnFinish;
	}





}
