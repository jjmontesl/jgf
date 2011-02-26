
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.example.tanks.entity.Tank;
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

    @Register (ref = "logic/root/ingame/mission")
    private MissionLogic missionLogic;
    
    @Register (ref = "logic/action/newgame/init")
    private NewGameAction newGameAction;
    
    @Register (ref = "settings/game/map")
    private StringSetting mapSetting;
    
	/* (non-Javadoc)
	 * @see net.jgf.logic.BaseLogicState#activate()
	 */
	@Override
	public void perform(Object arg) {

		// Increment mission number
		missionLogic.setMission(missionLogic.getMission() + 1);
		mapSetting.setValue("mission" + missionLogic.getMission());
		
	}

}
