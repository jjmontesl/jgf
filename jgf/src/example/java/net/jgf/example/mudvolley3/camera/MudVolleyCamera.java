
package net.jgf.example.mudvolley3.camera;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.camera.CameraController;
import net.jgf.jme.config.JmeConfigHelper;

import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 */
@Configurable
public class MudVolleyCamera extends CameraController {


	protected Vector3f location = new Vector3f();

	protected Spatial target;

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

		getCamera().setLocation(new Vector3f(target.getLocalTranslation().x * 0.15f, 8, 23));
		getCamera().lookAt(new Vector3f(target.getLocalTranslation().x * 0.3f, 3.5f, 0), Vector3f.UNIT_Y);
		getCamera().update();

	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.location = JmeConfigHelper.getVector3f(config, configPath + "/location", new Vector3f(0, 5, -10));

	}

	/**
	 * @return the target
	 */
	public Spatial getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Spatial target) {
		this.target = target;
	}



}
