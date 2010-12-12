
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.state.State;
import net.jgf.core.state.StateHelper;
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
	
	
	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {
		super.load();
		Jgf.getDirectory().register(this, "player", "entity/root/players/player1");
		bannerFailed = Jgf.getDirectory().getObjectAs("view/root/level/failed", State.class);
		timeAfterDeath = 0;
	}
	

	@Override
	public void update(float tpf) {
		
		checkPlayerAlive(tpf);
		
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


	public PlayerTank getPlayer() {
		return player;
	}


	public void setPlayer(PlayerTank player) {
		this.player = player;
	}
	
}

