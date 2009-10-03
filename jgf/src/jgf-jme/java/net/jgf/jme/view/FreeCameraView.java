package net.jgf.jme.view;




import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.entity.Spectator;
import net.jgf.scene.Scene;
import net.jgf.scene.SceneManager;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;

/**
 * This represents the spectator state.
 */
@Configurable
public class FreeCameraView extends BaseViewState {

	private final static Logger logger = Logger.getLogger(FreeCameraView.class);

	protected Spectator spectator = null;

	protected InputHandler inputHandler;

	protected SceneManager sceneManager;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		// TODO: Maybe these two ones should be optional / null (study carefully)
		String sceneManagerRef = config.getString(configPath + "/sceneManager/@ref");
		if (sceneManagerRef != null) net.jgf.system.Jgf.getDirectory().register(this, "sceneManager", sceneManagerRef);

	}

	/**
	 * Process input
	 */
	public class SpectatorKeyInputAction extends InputAction {
		public void performAction(InputActionEvent evt) {

			if (evt.getTriggerIndex() == KeyInput.KEY_UP) spectator.forward(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == KeyInput.KEY_DOWN) spectator.backward(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == KeyInput.KEY_LEFT) spectator.left(evt.getTriggerPressed());
			if (evt.getTriggerIndex() == KeyInput.KEY_RIGHT) spectator.right(evt.getTriggerPressed());

		}
	}

	/**
	 * Process input
	 */
	public class SpectatorMouseInputAction extends InputAction {
		public void performAction(InputActionEvent evt) {

			if (evt.getTriggerIndex() == 0) spectator.addDeltaX(evt.getTriggerDelta());
			if (evt.getTriggerIndex() == 1) spectator.addDeltaY(evt.getTriggerDelta());

		}
	}


	/* (non-Javadoc)
	 * @see com.jmex.game.state.GameState#setActive(boolean)
	 */
	@Override
	public void activate() {

		super.activate();

		if (spectator == null) {
			this.spectator = new Spectator("!" + this + "/spectator");
			inputHandler = new InputHandler();
			inputHandler.addAction(new SpectatorKeyInputAction(), InputHandler.DEVICE_KEYBOARD, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);
			inputHandler.addAction(new SpectatorMouseInputAction(), InputHandler.DEVICE_MOUSE, InputHandler.BUTTON_ALL, InputHandler.AXIS_ALL, false);
		}

		setAsSceneCamera();

	}

	public void setAsSceneCamera() {
		setAsSceneCamera(sceneManager.getScene());
	}

	public void setAsSceneCamera(Scene scene) {
		scene.setCamera(spectator.getDefaultCameraController());
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#deactivate()
	 */
	@Override
	public void deactivate() {
		super.deactivate();
	}

	@Override
	public void render(float tpf) {

	}



	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#input(float)
	 */
	@Override
	public void input(float tpf) {
		super.input(tpf);
		inputHandler.update(tpf);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		spectator.update(tpf);
	}

	/**
	 * @return the sceneManager
	 */
	public SceneManager getSceneManager() {
		return sceneManager;
	}

	/**
	 * @param sceneManager the sceneManager to set
	 */
	public void setSceneManager(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}



}
