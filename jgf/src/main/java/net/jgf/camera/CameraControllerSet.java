
package net.jgf.camera;

import java.util.HashMap;

import net.jgf.config.ConfigException;
import net.jgf.system.Jgf;

import org.apache.commons.lang.StringUtils;


/**
 * CameraControllerSet manages the camera controllers attached to an entity or other component.
 * 
 * @see CameraController
 */
public class CameraControllerSet {

	/**
	 * The set of cameras hold by this CameraManager.
	 */
	protected HashMap<String, CameraController> cameraControllers;

	/**
	 * Class constructor.
	 */
	public CameraControllerSet() {
		this.cameraControllers = new HashMap<String, CameraController>();
	}

	/**
	 * Adds a camera to this CameraManager
	 */
	public void addCameraController(CameraController cameraController) {
		// TODO: Check for not null/empty ids
		if (Jgf.getApp().isDebug()) {
			if (StringUtils.isBlank(cameraController.getId())) {
				throw new ConfigException("Cannot add a CameraController with a null id to a CameraControllerSet");
			}
		}
		cameraControllers.put(cameraController.getId(), cameraController);
	}

	/**
	 * Returns a camera.
	 */
	public CameraController getCameraController(String id) {
		return cameraControllers.get(id);
	}

}
