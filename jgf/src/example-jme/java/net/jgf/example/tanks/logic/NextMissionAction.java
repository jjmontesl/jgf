
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.example.tanks.entity.Tank;
import net.jgf.example.tanks.logic.flow.MissionLogic;
import net.jgf.example.tanks.view.ProjectedWaterView;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.FileChainLoader;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.scene.Scene;
import net.jgf.scene.SimpleSceneManager;
import net.jgf.settings.StringSetting;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.scene.Node;


/**
 *
 */
@Configurable
public class NextMissionAction extends BaseLogicAction {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(NextMissionAction.class);

    @Register (ref = "logic/root/flow/mission")
    private MissionLogic missionLogic;
    
    @Register (ref = "settings/game/map")
    private StringSetting mapSetting;
    
    @Register (ref = "settings/game/mode")
    private StringSetting gamemodeSetting;
    
	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

		GameMode gameMode = GameMode.valueOf(gamemodeSetting.getValue());
        if (gameMode == GameMode.Missions) {
            missionLogic.nextMap();
        } else if (gameMode == GameMode.FreeForAll) {
            //StateHelper.loadAndActivate(ffaLogic);
        }
		
	}

}
