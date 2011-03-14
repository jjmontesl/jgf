
package net.jgf.refs;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.map.MultiValueMap;


/**
 */
public class ReferenceSet {

	/**
	 * The set of cameras hold by this CameraManager.
	 */
	protected MultiValueMap references;

	/**
	 * Class constructor.
	 */
	public ReferenceSet() {
		this.references = new MultiValueMap();
	}

	/**
	 * Adds a camera to this CameraManager
	 */
	public void addReference(Reference reference) {
		//if (references.containsKey(reference.getName())) {
		//	throw new ConfigException("A reference with name '" +  reference.getName() + "' already exists in this references" );
		//}
		references.put(reference.getName(), reference);
	}

    public Collection<Reference> getReferences() {
        return references.values();
    }
	
	/**
	 * Returns a reference. If many with the same name exists, a random one is returned.
	 */
	public Reference getReference(String name) {
		Collection<Reference> filtered = references.getCollection(name);
		if (filtered == null) return null;
		if (filtered.size() > 1) {
		    int num = (int) Math.floor(Math.random() * ((double)filtered.size()));
		    Iterator<Reference> it = filtered.iterator();
		    for (int i = 0; i < num; i++) {
		        it.next();
		    }
		    return it.next();
		} else if (filtered.size() == 1) {
		    return filtered.iterator().next();
		} else {
		    return null;
		}
	}

}
