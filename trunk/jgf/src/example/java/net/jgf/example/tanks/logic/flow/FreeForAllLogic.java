
package net.jgf.example.tanks.logic.flow;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.State;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.Player;
import net.jgf.example.tanks.entity.Tank;
import net.jgf.example.tanks.logic.SpawnLogic;
import net.jgf.settings.StringSetting;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class FreeForAllLogic extends BaseFlowLogic {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(FreeForAllLogic.class);

	@Register (ref = "entity/root/tanks/player1")
	protected Tank player;
	
	@Register (ref = "view/root/ingame/failed")
	protected State stateFailed;

	@Register (ref = "view/root/ingame/victory")
    protected State stateVictory;
	
	@Register (ref = "entity/root/enemies")
	protected EntityGroup enemies;
	
    @Register (ref = "entity/root/bullets")
    protected EntityGroup bullets;
	
    @Register (ref = "logic/root/ingame/spawn")
    protected SpawnLogic spawnLogic;
    
    @Register (ref = "settings/game/map")
    private StringSetting mapSetting;
    
	protected boolean fighting;
	
    protected float gameTime;
    
	@Override
    public void doActivate() {
        super.doActivate();
        fighting = true;
        gameTime = 0;
    }

    @Override
	public void doUpdate(float tpf) {
		
        if (fighting) {
            gameTime += tpf;

            checkPlayerAlive(tpf);
    		
    		checkMissionFinished(tpf);
		
        }
		
	}
	
    
    
    
	private void checkPlayerAlive(float tpf) {
		
		if ((player == null) && (fighting == true)) {
			
		    logger.info("Player is not alive (null reference)");
		    
			// Player was removed from entities (must have died)
		    fighting = false;
		    StateHelper.loadAndActivate(stateFailed);
		
		}
		
	}
	
	private void checkMissionFinished(float tpf) {
        
	    if ((enemies.children().size() == 0) && (fighting == true)) {
            
	        logger.info("Mission Finished");
	        
            // All enemies destroyed
	        fighting = false;
	        
	        // Destroy all bullets
	        for (Entity entity : bullets.children()) {
	            Bullet bullet = (Bullet) entity;
	            spawnLogic.destroyBullet(bullet);
	        }
	        
	        // Enable victory screen
            StateHelper.loadAndActivate(stateVictory);
            
        }
        
    }

    @Override
    public void doDeactivate() {
        // TODO Auto-generated method stub
        fighting = false;
    }

    public boolean isFighting() {
        return fighting;
    }

    public float getGameTime() {
        return gameTime;
    }

    @Override
    public void newPlayer(Player player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void nextMap() {
        //
    }

    @Override
    public void setup() {
        mapSetting.setValue("dm1");
    }

}

