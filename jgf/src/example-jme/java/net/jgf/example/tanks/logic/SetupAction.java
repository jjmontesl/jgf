
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Player;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.example.tanks.loader.SceneReferencesProcessorLoader;
import net.jgf.example.tanks.logic.flow.FreeForAllLogic;
import net.jgf.example.tanks.logic.flow.MissionLogic;
import net.jgf.example.tanks.logic.network.ServerNetworkLogic;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.LoadProperties;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.scene.SimpleSceneManager;
import net.jgf.settings.StringSetting;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class SetupAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SetupAction.class);
	
    @Register (ref = "logic/root/network/server")
    protected ServerNetworkLogic serverLogic;
    
    @Register (ref = "logic/root/flow/mission")
    private MissionLogic missionLogic;
    
    @Register (ref = "logic/root/flow/ffa")
    private FreeForAllLogic ffaLogic;
    
    @Register (ref = "settings/game/mode")
    private StringSetting gamemodeSetting;

	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

        GameMode gameMode = GameMode.valueOf(gamemodeSetting.getValue());
        if (gameMode == GameMode.Missions) {
            missionLogic.setup();
        } else if (gameMode == GameMode.FreeForAll) {
            ffaLogic.setup();
        }
		
	}

}
