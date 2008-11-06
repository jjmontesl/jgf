
package net.jgf.jme.refs;

import net.jgf.refs.BaseReference;

import com.jme.scene.Spatial;


/**
 */
public class SpatialReference extends BaseReference {

	protected Spatial spatial;

	public SpatialReference() {
		super();
	}



	public SpatialReference(String name, Spatial spatial) {
		super();
		this.name = name;
		this.spatial = spatial;
	}



	/**
	 * @return the node
	 */
	public Spatial getSpatial() {
		return spatial;
	}

	/**
	 * @param node the node to set
	 */
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}


}
