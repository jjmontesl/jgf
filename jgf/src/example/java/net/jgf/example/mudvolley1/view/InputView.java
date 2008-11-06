
package net.jgf.example.mudvolley1.view;



import net.jgf.config.Configurable;
import net.jgf.example.mudvolley1.entity.PlayerEntity;
import net.jgf.system.System;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jmex.game.state.GameState;

/**
 */
@Configurable
public class InputView extends BaseViewState {

	private PlayerEntity player1;

	private PlayerEntity player2;

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


			if (evt.getTriggerIndex() == KeyInput.KEY_A) player1.setWalkLeft(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == KeyInput.KEY_D) player1.setWalkRight(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == KeyInput.KEY_W) player1.setJump(evt.getTriggerPressed());

			if (evt.getTriggerIndex() == KeyInput.KEY_LEFT) player2.setWalkLeft(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == KeyInput.KEY_RIGHT) player2.setWalkRight(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == KeyInput.KEY_UP) player2.setJump(evt.getTriggerPressed());

		}

	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {

		super.load();

		player1 = System.getDirectory().getObjectAs("entity/root/player1", PlayerEntity.class);
		player2 = System.getDirectory().getObjectAs("entity/root/player2", PlayerEntity.class);

		inputHandler = new InputHandler();
		inputHandler.addAction(new KeyInputAction(), InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);

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
