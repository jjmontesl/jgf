
package net.jgf.example.mudvolley5.view;



import net.jgf.config.Configurable;
import net.jgf.example.mudvolley1.entity.PlayerEntity;
import net.jgf.jme.settings.KeySetting;
import net.jgf.settings.Settings;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jmex.game.state.GameState;

/**
 */
@Configurable
public class InputView extends BaseViewState {

	private PlayerEntity player1;

	private PlayerEntity player2;

	private Settings settings;
	
	private KeySetting p1leftKeySetting;
	private KeySetting p1rightKeySetting;
	private KeySetting p1upKeySetting;
	private KeySetting p2leftKeySetting;
    private KeySetting p2rightKeySetting;
    private KeySetting p2upKeySetting;
    
	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(InputView.class);

	InputHandler inputHandler;

	/**
	 * Key action
	 */
	public class KeyInputAction extends InputAction {

		public void performAction(InputActionEvent evt) {

			//logger.info("Key pressed (index=" + evt.getTriggerIndex() + ",time=" + evt.getTime() + ",press=" + evt.getTriggerPressed() + ")");
			if (evt.getTriggerIndex() == settings.getSetting("settings/settings/input/key/p1left", KeySetting.class).getValue()) player1.setWalkLeft(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == settings.getSetting("settings/settings/input/key/p1right", KeySetting.class).getValue()) player1.setWalkRight(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == settings.getSetting("settings/settings/input/key/p1up", KeySetting.class).getValue()) player1.setJump(evt.getTriggerPressed());

			if (evt.getTriggerIndex() == settings.getSetting("settings/settings/input/key/p2left", KeySetting.class).getValue()) player2.setWalkLeft(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == settings.getSetting("settings/settings/input/key/p2right", KeySetting.class).getValue()) player2.setWalkRight(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == settings.getSetting("settings/settings/input/key/p2up", KeySetting.class).getValue()) player2.setJump(evt.getTriggerPressed());

		}

	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {

		super.load();

		player1 = Jgf.getDirectory().getObjectAs("entity/root/player1", PlayerEntity.class);
		player2 = Jgf.getDirectory().getObjectAs("entity/root/player2", PlayerEntity.class);

		
		inputHandler = new InputHandler();
		inputHandler.addAction(new KeyInputAction(), InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);

        settings = Jgf.getDirectory().getObjectAs("settings", Settings.class);
		
	}

	/**
	 * Client update per frame.
	 * @see GameState#update(float)
	 */
	@Override
	public void input(float tpf) {

		inputHandler.update(tpf);

	}

}
