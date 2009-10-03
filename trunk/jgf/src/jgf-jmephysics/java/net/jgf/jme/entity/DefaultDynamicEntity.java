
package net.jgf.jme.entity;

import net.jgf.camera.CameraControllerSet;
import net.jgf.camera.HasCameras;
import net.jgf.refs.HasReferences;
import net.jgf.refs.ReferenceSet;





/**
 * An airplane.
 */
public abstract class DefaultDynamicEntity extends DynamicEntity implements HasReferences, HasCameras {

	// 	TODO: Implement

	CameraControllerSet cameras;

	ReferenceSet references;

	public DefaultDynamicEntity() {
		this(null);
	}

	public DefaultDynamicEntity(String id) {
		super(id);
		cameras = new CameraControllerSet();
		references = new ReferenceSet();
	}

	/* (non-Javadoc)
	 * @see net.jgf.refs.HasReferences#getReferences()
	 */
	@Override
	public ReferenceSet getReferences() {
		return references;
	}

	/* (non-Javadoc)
	 * @see net.jgf.camera.HasCameras#getCameraControllers()
	 */
	@Override
	public CameraControllerSet getCameraControllers() {
		return cameras;
	}


}
