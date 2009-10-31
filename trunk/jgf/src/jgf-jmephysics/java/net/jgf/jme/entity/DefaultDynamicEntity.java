
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

	private CameraControllerSet cameras;

	private ReferenceSet references;

	public DefaultDynamicEntity() {
		this(null);
	}

	public DefaultDynamicEntity(String id) {
		super(id);
	}
	
	/* (non-Javadoc)
	 * @see net.jgf.refs.HasReferences#getReferences()
	 */
	@Override
	public ReferenceSet getReferences() {
		if (references == null) references = new ReferenceSet();
		return references;
	}

	/* (non-Javadoc)
	 * @see net.jgf.camera.HasCameras#getCameraControllers()
	 */
	@Override
	public CameraControllerSet getCameraControllers() {
		if (cameras == null) cameras = new CameraControllerSet();
		return cameras;
	}


}
