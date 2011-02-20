
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
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

	protected PlayerTank player;
	
	protected float timeAfterDeath;
	
	protected State bannerFailed;
	
	protected EntityGroup enemies;
	
	protected boolean fighting;
	
	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#load()
	 */
	@Override
	public void doLoad() {
		super.doLoad();
		Jgf.getDirectory().register(this, "player", "entity/root/players/player1");
		Jgf.getDirectory().register(this, "enemies", "entity/root/enemy");
		bannerFailed = Jgf.getDirectory().getObjectAs("view/root/level/failed", State.class);
		
	}
	
	

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


	public PlayerTank getPlayer() {
		return player;
	}


	public void setPlayer(PlayerTank player) {
		this.player = player;
	}



    public EntityGroup getEnemies() {
        return enemies;
    }



    public void setEnemies(EntityGroup enemies) {
        this.enemies = enemies;
    }



    public boolean isFighting() {
        return fighting;
    }

}

