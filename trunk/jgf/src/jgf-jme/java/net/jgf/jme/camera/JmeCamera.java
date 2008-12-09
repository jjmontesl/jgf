
package net.jgf.jme.camera;



import net.jgf.camera.CameraController;
import net.jgf.config.Configurable;

import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;

/**
 * A camera attached to a Viewable entity.
 */
@Configurable
public abstract class JmeCamera extends CameraController {


	public JmeCamera() {
		super();
	}

	public JmeCamera(String id) {
		super(id);
	}

	// TODO: Camera should be configurable?? By default returning the default camera
	protected Camera getCamera() {
		return DisplaySystem.getDisplaySystem().getRenderer().getCamera();
	}

}
