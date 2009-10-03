/**
 * $Id: Spectator.java,v 1.1 2008/01/08 16:35:40 jjmontes Exp $
 * Java Game Framework
 */

package net.jgf.jme.entity;



import net.jgf.camera.CameraController;
import net.jgf.jme.camera.SpatialCamera;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * A default JME observer.
 */
public class Spectator extends DefaultEntity {

	public final static float MOVE_SPEED = 4.0f;

	public final static float ROTATION_SPEED = 2.0f;

	protected boolean forward;

	protected boolean backward;

	protected boolean left;

	protected boolean right;

	protected float deltaX;

	protected float deltaY;

	protected float yaw;

	protected float pitch;

	public static final String DEFAULT_CAMERA_ID = "spectatorDefaultCamera";

  // TODO: Sensitivity is part of the spectator!

	public Spectator() {
		this(null);
	}

	public Spectator(String id) {
		super(id);
		this.spatial = new Node("spectatorNode");
		this.cameras.addCameraController(new SpatialCamera(DEFAULT_CAMERA_ID, this.spatial));
	}

	/**
	 * Updates the entity
	 */
	@Override
	public void update(float tpf) {

		if (forward) {
			getSpatial().getLocalTranslation().addLocal(getSpatial().getLocalRotation().mult(Vector3f.UNIT_Z).mult(MOVE_SPEED * tpf));
		}
		if (backward) {
			getSpatial().getLocalTranslation().addLocal(getSpatial().getLocalRotation().mult(Vector3f.UNIT_Z).mult(- MOVE_SPEED * tpf));
		}
		if (left) {
			getSpatial().getLocalTranslation().addLocal(getSpatial().getLocalRotation().mult(Vector3f.UNIT_X).mult(MOVE_SPEED * tpf));
		}
		if (right) {
			getSpatial().getLocalTranslation().addLocal(getSpatial().getLocalRotation().mult(Vector3f.UNIT_X).mult(-MOVE_SPEED * tpf));
		}

		// Apply mouse input
		// TODO: This is ñapa: replace from settings
		yaw += (-ROTATION_SPEED * deltaX /* * Jgf.getSettings().getView().getMouseSensitivity() */);
		pitch += (-ROTATION_SPEED * deltaY /* * Jgf.getSettings().getView().getMouseSensitivity() */);

		//if (pitch > FastMath.HALF_PI - 100*FastMath.FLT_EPSILON) pitch = FastMath.HALF_PI - 100*FastMath.FLT_EPSILON;
		//if (pitch < (-FastMath.HALF_PI + 100*FastMath.FLT_EPSILON)) pitch = -FastMath.HALF_PI + 100*FastMath.FLT_EPSILON;
		if (pitch > FastMath.HALF_PI - FastMath.FLT_EPSILON) pitch = FastMath.HALF_PI - FastMath.FLT_EPSILON;
		if (pitch < (-FastMath.HALF_PI + FastMath.FLT_EPSILON)) pitch = -FastMath.HALF_PI + FastMath.FLT_EPSILON;

		// Clear deltas
		deltaX = 0;
    deltaY = 0;

    // Orient the spectator
		getSpatial().getLocalRotation().fromAngles(pitch, yaw, 0);

		// Movement

		Vector3f trans = getSpatial().getLocalTranslation();
		if (forward) {
			trans.addLocal(getSpatial().getLocalRotation().mult(Vector3f.UNIT_Z).normalizeLocal().mult(tpf * MOVE_SPEED));
		}
		if (backward) {
			trans.addLocal(getSpatial().getLocalRotation().mult(Vector3f.UNIT_Z).normalizeLocal().mult(-1 * tpf * MOVE_SPEED));
		}
		if (left) {
			trans.addLocal(getSpatial().getLocalRotation().mult(Vector3f.UNIT_X).normalizeLocal().mult(tpf * MOVE_SPEED));
		}
		if (right) {
			trans.addLocal(getSpatial().getLocalRotation().mult(Vector3f.UNIT_X).normalizeLocal().mult(-1 * tpf * MOVE_SPEED));
		}
		getSpatial().updateWorldVectors();

	}

	public void forward (boolean forward2) {
		this.forward = forward2;
	}

	public void backward (boolean backward2) {
		this.backward = backward2;
	}

	public void left (boolean left2) {
		this.left = left2;
	}

	public void right (boolean right2) {
		this.right = right2;
	}

	public void addDeltaY (float value) {
		this.deltaY += value;
	}

	public void addDeltaX (float value) {
		this.deltaX += value;
	}

	public CameraController getDefaultCameraController() {
		return cameras.getCameraController(DEFAULT_CAMERA_ID);
	}



}
