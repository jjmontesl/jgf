
package net.jgf.example.tanks.view;



import net.jgf.config.Configurable;
import net.jgf.example.tanks.entity.PlayerTank;
import net.jgf.jme.view.CursorRenderView;
import net.jgf.system.Jgf;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.Mouse;
import com.jme.input.MouseInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Plane;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameState;

/**
 */
@Configurable
public class InputView extends BaseViewState {

	private PlayerTank player1;

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(InputView.class);

	protected InputHandler inputHandler;

	/**
	 * Key action
	 */
	public class KeyInputAction extends InputAction {

		public void performAction(InputActionEvent evt) {

			//logger.info("Key pressed (index=" + evt.getTriggerIndex() + ",time=" + evt.getTime() + ",press=" + evt.getTriggerPressed() + ")");

			if (player1 == null) return;
			
			if (evt.getTriggerIndex() == KeyInput.KEY_A) player1.setWalkLeft(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == KeyInput.KEY_D) player1.setWalkRight(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == KeyInput.KEY_W) player1.setWalkUp(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == KeyInput.KEY_S) player1.setWalkDown(evt.getTriggerPressed());

		}

	}

	public class TankMouseInputAction extends InputAction {

		private Mouse mouse;

		private Vector2f screenPosition = new Vector2f();
		private Vector3f clickStartPos = new Vector3f();

		public TankMouseInputAction(Mouse mouse) {
			super();
			this.mouse = mouse;
		}

		public void performAction(InputActionEvent evt) {

			if (player1 == null) return;
			
			//logger.info("Key pressed (index=" + evt.getTriggerIndex() + ",time=" + evt.getTime() + ",press=" + evt.getTriggerPressed() + ")");
			mouse.getLocalTranslation();

			screenPosition.x = MouseInput.get().getXAbsolute();
			screenPosition.y = MouseInput.get().getYAbsolute();

			DisplaySystem.getDisplaySystem().getWorldCoordinates(screenPosition, 1f, clickStartPos);

			Ray ray = new Ray(DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation(),
					clickStartPos.subtractLocal(DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation()));
			ray.getDirection().normalizeLocal();

			Plane plane = new Plane();
			plane.setPlanePoints(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(10.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 10.0f));
			ray.intersectsWherePlane(plane, player1.getTarget());

			if (evt.getTriggerPressed()) player1.setFiring(true);

		}

	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#load()
	 */
	@Override
	public void load() {

		super.load();

		Jgf.getDirectory().register(this, "player", "entity/root/players/player1");

		inputHandler = new InputHandler();
		inputHandler.addAction(new KeyInputAction(), InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);

	}

	@Override
	public void activate() {
		super.activate();
		CursorRenderView cursorView = Jgf.getDirectory().getObjectAs("view/root/level/cursor", CursorRenderView.class);
		cursorView.getMouse().registerWithInputHandler( inputHandler );
		inputHandler.addAction(new TankMouseInputAction(cursorView.getMouse()), InputHandler.DEVICE_MOUSE, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);
	}

	/**
	 * Client update per frame.
	 * @see GameState#update(float)
	 */
	@Override
	public void input(float tpf) {

		inputHandler.update(tpf);

	}

	public PlayerTank getPlayer() {
		return player1;
	}

	public void setPlayer(PlayerTank player) {
		this.player1 = player;
	}
	
	

}
