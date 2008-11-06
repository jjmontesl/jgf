/**
 * $Id: Scene.java,v 1.3 2008/02/09 22:21:20 jjmontes Exp $
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
