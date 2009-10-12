
package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.entity.EntityManager;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.jme.view.display.DisplayItemsView;
import net.jgf.jme.view.display.TextItem;
import net.jgf.logic.BaseLogicState;
import net.jgf.scene.SceneManager;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;


/**
 *
 */
@Configurable
public class InGameLogic extends BaseLogicState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(InGameLogic.class);

	private SceneManager sceneManager;

	private EntityManager entityManager;
	
	
	DisplayItemsView osdItemsView;
	
	TextItem killsTextItem;
	
	TextItem timeTextItem;
	
	float timeElapsed;
	
	float gameTime;

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {
		super.load();
		gameTime = 0;
		entityManager = Jgf.getDirectory().getObjectAs("entity", EntityManager.class);
		sceneManager = Jgf.getDirectory().getObjectAs("scene/manager", SceneManager.class);
		killsTextItem = Jgf.getDirectory().getObjectAs("view/root/level/osd/kills", TextItem.class);
		timeTextItem = Jgf.getDirectory().getObjectAs("view/root/level/osd/time", TextItem.class);
		osdItemsView = Jgf.getDirectory().getObjectAs("view/root/level/osd", DisplayItemsView.class);
	}

	public void updateOsd(float tpf) {
		
		timeElapsed += tpf;
		if (timeElapsed > 1.0f) {
			
			timeElapsed = 0;
			if (Jgf.getDirectory().containsObject("entity/root/players/player1")) {
				
				PlayerTank player = Jgf.getDirectory().getObjectAs("entity/root/players/player1", PlayerTank.class);
				killsTextItem.setText("Hits: " +  player.getKills());
				
				int timeMinutes = (int) (gameTime / 60);
				int timeSeconds = ((int) (gameTime)) % 60;
				String timeString = String.format("%02d:%02d", (Object[]) new Integer[] {timeMinutes, timeSeconds} );
				timeTextItem.setText("Time: " +  timeString);
	
				killsTextItem.refreshNode(osdItemsView.getRootNode());
				timeTextItem.refreshNode(osdItemsView.getRootNode());
				
			}
		}
	}
	
	@Override
	public void update(float tpf) {

		// Order matters
		gameTime += tpf;
		entityManager.update(tpf);
		sceneManager.update(tpf);
		updateOsd(tpf);
	}


}
