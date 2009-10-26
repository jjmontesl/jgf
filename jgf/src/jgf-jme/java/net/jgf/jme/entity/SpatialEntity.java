/**
 * $Id$
 * Java Game Framework
 */

package net.jgf.jme.entity;



import net.jgf.entity.BaseEntity;
import net.jgf.entity.EntityGroup;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * An airplane.
 */
public abstract class SpatialEntity extends BaseEntity {

	protected Spatial spatial;

	public SpatialEntity() {
		super();
	}

	public SpatialEntity(String id) {
		super (id);
	}

	/**
	 * @return the spatial
	 */
	public Spatial getSpatial() {
		return spatial;
	}

	/**
	 * @param spatial the spatial to set
	 */
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	public void integrate(EntityGroup parentEntity, Node parentNode) {
		integrate(parentEntity, parentNode, null);
	}

	/**
	 *
	 * @param parentEntity
	 * @param parentNode
	 * @param location
	 */
	public void integrate(EntityGroup parentEntity, Node parentNode, Vector3f location) {
		parentEntity.attachChild(this);
		// If entity name starts with !, we don't add it to the directory
		this.setId( (this.getId().startsWith("!") ? "!" : "") + parentEntity.getId() + "/" + this.getId());
		net.jgf.system.Jgf.getDirectory().addObject(this.getId(), this);

		parentNode.attachChild(this.getSpatial());
		if (location != null) getSpatial().getLocalTranslation().set(location);
	}

	public void withdraw(EntityGroup parentEntity, Node parentNode) {

		parentNode.detachChild(this.getSpatial());
		net.jgf.system.Jgf.getDirectory().removeObject(this.getId());
		parentEntity.dettachChild(this);
	}

}
