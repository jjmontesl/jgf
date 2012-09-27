
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
import net.jgf.logic.BaseLogicState;
import net.jgf.settings.StringSetting;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public abstract class BaseFlowLogic extends BaseLogicState {

    public abstract void setup();
    
    public abstract void newPlayer(Player player);
    
    public abstract void nextMap();
	
	
}

