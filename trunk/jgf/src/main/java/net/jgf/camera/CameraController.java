
package net.jgf.camera;


import net.jgf.core.component.BaseComponent;

/**
 * A camera attached to a Viewable entity.
 */
// TODO:
public abstract class CameraController extends BaseComponent {

	/**
	 * Updates the camera using its controller
	 */
	public abstract void update(float tpf);

	public CameraController() {
		super();
	}

	public CameraController(String id)  {
		super(id);
	}

}
