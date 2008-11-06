/**
 * $Id$
 * Java Game Framework
 */

package net.jgf.core.state;

/**
 * 
 */
public interface StateObserver {
	
	public void onActivated(State state);
	
	public void onDeactivated(State state);
	
	public void onLoaded(State state);
	
	public void onUnloaded(State state);
	
}
