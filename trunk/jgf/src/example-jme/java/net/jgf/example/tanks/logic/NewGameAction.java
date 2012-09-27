
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Player;
import net.jgf.example.tanks.logic.flow.FreeForAllLogic;
import net.jgf.example.tanks.logic.flow.MissionLogic;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.settings.StringSetting;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class NewGameAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(NewGameAction.class);

    @Register (ref = "logic/root/flow/mission")
    private MissionLogic missionLogic;
    
    @Register (ref = "logic/root/flow/ffa")
    private FreeForAllLogic ffaLogic;
    
    @Register (ref = "entity/root/players")
    private EntityGroup players;
    
    @Register (ref = "settings/game/mode")
    private StringSetting gamemodeSetting;
	
	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

		logger.info ("Starting new tanks game");
		
        // Player
        Player player = new Player();
        player.setId("entity/root/players/player1");
        players.attachChild(player);
        Jgf.getDirectory().addObject(player.getId(), player);
        Jgf.getDirectory().addObject("entity/root/links/self", player);
		
        StateHelper.loadAndActivate("logic/root/flow");
        
		GameMode gameMode = GameMode.valueOf(gamemodeSetting.getValue());
		if (gameMode == GameMode.Missions) {
		    StateHelper.loadAndActivate(missionLogic);
		} else if (gameMode == GameMode.FreeForAll) {
		    StateHelper.loadAndActivate(ffaLogic);
		}
	    
	}
	
}
