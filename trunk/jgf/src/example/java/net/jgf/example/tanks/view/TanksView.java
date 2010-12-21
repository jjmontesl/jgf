package net.jgf.example.tanks.view;

import net.jgf.config.Configurable;
import net.jgf.core.state.StateLifecycleEvent;
import net.jgf.core.state.StateObserver;
import net.jgf.core.state.StateLifecycleEvent.LifecycleEventType;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.jme.gui.NiftyGuiView;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.math.FastMath;

import de.lessvoid.nifty.elements.render.TextRenderer;

/**
 */
@Configurable
public class TanksView extends BaseViewState implements StateObserver {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(TanksView.class);

	protected TextRenderer hitsText;
	
	protected TextRenderer timeText;
	
	protected PlayerTank player;
	
	protected NiftyGuiView niftyView;
	
	// TODO: This should be game data
	protected float gameTime;
	
	protected float timeElapsed;
	
	@Override
	public void load() {
		super.load();

		// Hook the activate event of the Nifty menu
		niftyView = Jgf.getDirectory().getObjectAs("view/root/level/osd",  NiftyGuiView.class);
		niftyView.addStateObserver(this);

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
		
		if (timeElapsed > 0.09f) {
		
			timeElapsed = 0.0f;
			
			if (player != null) {
				
				int timeMinutes = (int) (gameTime / 60);
				int timeSeconds = ((int) (gameTime)) % 60;
				String timeString = String.format("%02d:%02d.%1d", (Object[]) new Integer[] {timeMinutes, timeSeconds, (int) ((gameTime - FastMath.floor(gameTime)) * 10)} );

				timeText.setText("Time: " +  timeString);
				hitsText.setText("Hits: " +  player.getKills());
			}
			
		}
		
	}
	

	public PlayerTank getPlayer() {
		return player;
	}



	public void setPlayer(PlayerTank player) {
		this.player = player;
	}

    @Override
    public void onStateLifecycle(StateLifecycleEvent evt) {
        if (evt.getType() == LifecycleEventType.Activate) {
            timeText = niftyView.getNifty().getCurrentScreen().findElementByName("labelTime").getRenderer(TextRenderer.class);
            hitsText = niftyView.getNifty().getCurrentScreen().findElementByName("labelHits").getRenderer(TextRenderer.class);
        }
    }
	
}
