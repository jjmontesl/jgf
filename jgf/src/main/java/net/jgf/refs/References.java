
package net.jgf.refs;

import java.util.HashMap;

import net.jgf.config.ConfigException;


/**
 */
public class References {

	/**
	 * The set of cameras hold by this CameraManager.
	 */
	protected HashMap<String, Reference> references;

	/**
	 * Class constructor.
	 */
	public References() {
		this.references = new HashMap<String, Reference>();
	}

	/**
	 * Adds a camera to this CameraManager
	 */
	public void addReference(Reference reference) {
		if (references.containsKey(reference.getName())) {
			throw new ConfigException("A reference with name '" +  reference.getName() + "' already exists in this references" );
		}
		references.put(reference.getName(), reference);
	}

	/**
	 * Returns a camera.
	 */
	public Reference getReference(String name) {
		return references.get(name);
	}

}