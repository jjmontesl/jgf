/**
 * $Id$
 * Java Game Framework
 */

package net.jgf.core.state;

/**
 *
 */
public interface StateObserver {

	public void onStateLifecycle(StateLifecycleEvent evt);

}
