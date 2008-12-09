
package net.jgf.camera;

import java.util.HashMap;


/**
 * CameraControllerSet manages the cameras attached to an entity or other component.
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
		cameraControllers.put(cameraController.getId(), cameraController);
	}

	/**
	 * Returns a camera.
	 */
	public CameraController getCameraController(String id) {
		return cameraControllers.get(id);
	}

}
