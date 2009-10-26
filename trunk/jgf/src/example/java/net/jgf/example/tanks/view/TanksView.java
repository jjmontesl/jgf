package net.jgf.example.tanks.view;

import net.jgf.config.Configurable;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.jme.view.display.DisplayItemsView;
import net.jgf.jme.view.display.TextItem;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.math.FastMath;

/**
 */
@Configurable
public class TanksView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(TanksView.class);

	protected DisplayItemsView osdItemsView;
	
	protected TextItem killsTextItem;
	
	protected TextItem timeTextItem;
	
	protected PlayerTank player;
	
	// TODO: This should be game data
	protected float gameTime;
	
	protected float timeElapsed;
	
	@Override
	public void load() {
		super.load();
		killsTextItem = Jgf.getDirectory().getObjectAs("view/root/level/osd/kills", TextItem.class);
		timeTextItem = Jgf.getDirectory().getObjectAs("view/root/level/osd/time", TextItem.class);
		osdItemsView = Jgf.getDirectory().getObjectAs("view/root/level/osd", DisplayItemsView.class);
		Jgf.getDirectory().register(this, "player", "entity/root/players/player1");
		gameTime = 0;
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		// Order matters
		gameTime += tpf;
		updateOsd(tpf);
	}

	public void updateOsd(float tpf) {
		
		timeElapsed += tpf;
		
		if (timeElapsed > 1.0f) {
		
			timeElapsed = 0.0f;
			
			if (player != null) {
				
				int timeMinutes = (int) (gameTime / 60);
				int timeSeconds = ((int) (gameTime)) % 60;
				String timeString = String.format("%02d:%02d.%1d", (Object[]) new Integer[] {timeMinutes, timeSeconds, (int) ((gameTime - FastMath.floor(gameTime)) * 10)} );
				timeTextItem.setText("Time: " +  timeString);
				timeTextItem.refreshNode(osdItemsView.getRootNode());
				
				killsTextItem.setText("Hits: " +  player.getKills());
				killsTextItem.refreshNode(osdItemsView.getRootNode());
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
