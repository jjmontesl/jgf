/**
 * $Id: DynamicEntity.java,v 1.3 2008/02/09 22:21:19 jjmontes Exp $
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
public abstract class SceneEntity extends BaseEntity {

	protected Spatial spatial;

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
		this.setId(parentEntity.getId() + "/" + this.getId());
		net.jgf.system.System.getDirectory().addObject(this.getId(), this);

		parentNode.attachChild(this.getSpatial());
		if (location != null) getSpatial().setLocalTranslation(location);
	}

	public void exclude(EntityGroup parentEntity, Node parentNode) {

		parentNode.detachChild(this.getSpatial());
		net.jgf.system.System.getDirectory().removeObject(this.getId());
		parentEntity.dettachChild(this);
	}

}