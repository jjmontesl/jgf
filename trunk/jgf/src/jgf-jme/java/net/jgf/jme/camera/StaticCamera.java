
package net.jgf.jme.camera;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.config.JmeConfigHelper;

import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * A camera attached to a Viewable entity.
 */
@Configurable
public class StaticCamera extends CameraController {

	protected Vector3f location = new Vector3f();

	protected Vector3f target = new Vector3f();

	public StaticCamera() {
		super();
	}

	public StaticCamera(String id, Vector3f location, Vector3f target) {
		super(id);
		this.location = location;
		this.target = target;
	}

	/**
	 * Process input
	 */
	@Override
	public void processMouseInput(InputActionEvent evt) {

	}

	/**
	 * Updates the camera using its controller
	 */
	@Override
	public void update(float tpf) {

		Camera camera = this.getCamera();
		camera.setLocation(location);
		camera.lookAt(target, Vector3f.UNIT_Y);
		camera.update();
	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.location = JmeConfigHelper.getVector3f(config, configPath + "/location", new Vector3f(0, 5, -10));
		this.target = JmeConfigHelper.getVector3f(config, configPath + "/target", new Vector3f(0, 0, 0));

	}



}
