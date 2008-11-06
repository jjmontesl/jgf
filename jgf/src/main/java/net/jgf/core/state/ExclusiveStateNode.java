/**
 * $Id: Scene.java,v 1.3 2008/02/09 22:21:20 jjmontes Exp $
 * Java Game Framework
 */

package net.jgf.core.state;

import net.jgf.config.Config;
import net.jgf.config.Configurable;


/**
 * 
 */
@Configurable
public class ExclusiveStateNode<T extends State> extends BaseStateNode<T> implements StateObserver {
	
	/**
	 * Configures this object from Config.
	 */
	public void readConfig(Config config, String configPath) {
		
		super.readConfig(config, configPath);
			
	}	

	@Override
	public void onActivated(State activatedState) {
		
		// Count number of active children
		int activeCount = 0;
		for (T state : children) {
			if (state.isActive()) activeCount++;
		}
		
		// If more than one, disable all but last
		if (activeCount > 1)  {
			for (T state : children) {
				if ((state.isActive()) && (state != activatedState)) state.deactivate();
			}
		}
		
		
	}

	@Override
	public void onDeactivated(State state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaded(State state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnloaded(State state) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
