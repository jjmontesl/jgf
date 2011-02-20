
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.State;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.logic.BaseLogicState;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class MissionLogic extends BaseLogicState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(MissionLogic.class);

	@Register (ref = "entity/root/players/player1")
	protected PlayerTank player;
	
	@Register (ref = "view/root/level/failed")
	protected State bannerFailed;
	
	@Register (ref = "entity/root/enemy")
	protected EntityGroup enemies;
	
	protected boolean fighting;
	
	protected float timeAfterDeath;
	
	@Override
    public void doActivate() {
        super.doActivate();
        timeAfterDeath = 0;
        fighting = true;
    }

    @Override
	public void doUpdate(float tpf) {
		
		checkPlayerAlive(tpf);
		
		checkMissionFinished(tpf);
		
	}
	
	private void checkPlayerAlive(float tpf) {
		
		if (player == null) {
			
			// Player was removed from entities (must have died)
			if (!bannerFailed.isActive()) {
				timeAfterDeath += tpf;
				if (timeAfterDeath > 2.0f) {
					StateHelper.loadAndActivate(bannerFailed);
				}
			}
		
		}
		
	}
	
	private void checkMissionFinished(float tpf) {
        
	    if (enemies.children().size() == 0) {
            
            // All enemies destroyed
	        fighting = false;
	        
	        // Destroy all bullets
	        
	        
	        // Enable victory screen
            if (!bannerFailed.isActive()) {
                timeAfterDeath += tpf;
                if (timeAfterDeath > 2.0f) {
                    StateHelper.loadAndActivate(bannerFailed);
                }
            }
        
        }
        
    }

    public boolean isFighting() {
        return fighting;
    }

}

