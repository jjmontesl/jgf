
package net.jgf.example.mudvolley5.view;



import net.jgf.config.Configurable;
import net.jgf.example.mudvolley1.entity.PlayerEntity;
import net.jgf.jme.settings.KeySetting;
import net.jgf.settings.SettingHandler;
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

	private SettingHandler<Integer> p1leftKeySetting = new SettingHandler<Integer>(KeySetting.class, "#{settings/input/key/p1left}");
	private SettingHandler<Integer> p1rightKeySetting = new SettingHandler<Integer>(KeySetting.class, "#{settings/input/key/p1right}");
	private SettingHandler<Integer> p1upKeySetting = new SettingHandler<Integer>(KeySetting.class, "#{settings/input/key/p1up}");
	private SettingHandler<Integer> p2leftKeySetting = new SettingHandler<Integer>(KeySetting.class, "#{settings/input/key/p2left}");
    private SettingHandler<Integer> p2rightKeySetting = new SettingHandler<Integer>(KeySetting.class, "#{settings/input/key/p2right}");
    private SettingHandler<Integer> p2upKeySetting = new SettingHandler<Integer>(KeySetting.class, "#{settings/input/key/p2up}");
    
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
			if (evt.getTriggerIndex() == p1leftKeySetting.getValue()) player1.setWalkLeft(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == p1rightKeySetting.getValue()) player1.setWalkRight(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == p1upKeySetting.getValue()) player1.setJump(evt.getTriggerPressed());

			if (evt.getTriggerIndex() == p2leftKeySetting.getValue()) player2.setWalkLeft(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == p2rightKeySetting.getValue()) player2.setWalkRight(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == p2upKeySetting.getValue()) player2.setJump(evt.getTriggerPressed());

		}

	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.State#load()
	 */
	@Override
	public void doLoad() {

		super.doLoad();

		player1 = Jgf.getDirectory().getObjectAs("entity/root/player1", PlayerEntity.class);
		player2 = Jgf.getDirectory().getObjectAs("entity/root/player2", PlayerEntity.class);
		
		inputHandler = new InputHandler();
		inputHandler.addAction(new KeyInputAction(), InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);

	}

	/**
	 * Client update per frame.
	 * @see GameState#update(float)
	 */
	@Override
	public void doInput(float tpf) {

		inputHandler.update(tpf);

	}

}
