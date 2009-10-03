/**
 * $Id: SpatialEntity.java 114 2008-12-05 20:47:10Z jjmontes $
 * Java Game Framework
 */

package net.jgf.jme.entity;



import net.jgf.camera.CameraControllerSet;
import net.jgf.camera.HasCameras;
import net.jgf.refs.HasReferences;
import net.jgf.refs.ReferenceSet;

/**
 * An airplane.
 */
public abstract class DefaultEntity extends SpatialEntity implements HasReferences, HasCameras {

	CameraControllerSet cameras;

	ReferenceSet references;

	public DefaultEntity() {
		this(null);
	}

	public DefaultEntity(String id) {
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
