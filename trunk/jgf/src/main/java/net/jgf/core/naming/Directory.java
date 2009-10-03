/*
 * JGF - Java Game Framework
 * $Id$
 *
 * Copyright (c) 2008, JGF - Java Game Framework
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     * Neither the name of the 'JGF - Java Game Framework' nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <copyright holder> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <copyright holder> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.jgf.core.naming;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import net.jgf.config.ConfigException;
import net.jgf.core.service.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>Manages a directory of objects, which are accessed by id. Two objects
 * with the same id cannot exist in the Directory, and exceptions will be thrown
 * if user attempts to do so.</p>
 * <p>Objects are stored using WeakReferences, so they will be garbage collected and
 * disappear from the directory if they are not referenced from somewhere else. You
 * need to keep your own references to objects that you include in the Directory.</p>
 * <p>This class is thread safe. All accesses to fields are synchronized. Reference fields
 * are never returned.</p>
 *
 * @author jjmontes
 */
public final class Directory {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(Directory.class);

	/**
	 * Initial default estimated directory size. This is the initial capacity of the underlying map.
	 */
	private static final int DIRECTORY_DEFAULT_SIZE = 256;

	private static final char DIRECTORY_EXCLUDING_PREFIX = '!';

	/**
	 * Number of objects that were retrieved from this directory.
	 */
	private int retrievalCount;

	/**
	 * Max number of objects stored during the Directory life.
	 */
	private int peakSize;

	/**
	 * Map of objects stored in the directory. They are stored as {@link WeakReference} references.
	 */
	private Map<String, WeakReference<Object>> objects;

	/**
	 * Directory users registry, which sets references to objects that need them.
	 */
	private Registry registry;

	/**
	 * Constructor.
	 */
	public Directory() {
		peakSize = 0;
		retrievalCount = 0;
		registry = new Registry();
		objects = new Hashtable<String, WeakReference<Object>>(DIRECTORY_DEFAULT_SIZE, 0.6f);
	}

	/**
	 * Remove an object from the directory. A runtime ConfigException will be thrown
	 * if the object doesn't exist in the directory.
	 */
	public synchronized Object removeObject (String id) {

		if (StringUtils.isBlank(id)) {
			throw new ConfigException("Cannot remove object with a blank name: '" + id + "'");
		}
		if (id.charAt(0) == DIRECTORY_EXCLUDING_PREFIX) return null;

		WeakReference<Object> reference = objects.remove(id);
		if (reference == null) {
			throw new ConfigException("Tried to remove a non existent object named '" + id + "' from " + this);
		}
		return reference.get();

	}

	/**
	 * Adds an object to the directory. Exceptions will be thrown if an object with that name
	 * already exists in the repository, or if the name provided is blank or null.
	 */
	public synchronized void addObject (String id, Object object) throws ConfigException {

		WeakReference<Object> ref = new WeakReference<Object>(object);

		logger.trace("Adding object " + object + " to " + this);

		if (object == null) {
			throw new ConfigException("Trying to add a null object to " + this);
		}

		if (StringUtils.isBlank(id)) {
			throw new ConfigException("Cannot add object " + object + " with a blank name: '" + id + "'");
		}

		if (id.charAt(0) == DIRECTORY_EXCLUDING_PREFIX) {
			logger.debug("Not adding object with id '" + id + "' to the directory as it starts with '" + DIRECTORY_EXCLUDING_PREFIX + "'");
			return;
		}

		// Check if the object is duplicated and if so throw an exception
		if (objects.containsKey(id)) {
			throw new ConfigException("Cannot add object with name " + id + " because an object with that name already exists");
		}

		objects.put(id, ref);

		if (objects.size() > peakSize) peakSize = objects.size();

		// Update the registered objects
		registry.update(id, object);

	}

	/**
	 * Returns a object given its name, casted to the desired type.
	 * <p>Note that users can also register themselves with the
	 * Directory registry, so they receive updated references.</p>
	 * <p>An exception is thrown if the object is not present.</p>
	 * @see Directory#register(String, Object, String)
	 */
	public synchronized <T> T getObjectAs (String id, Class<T> expectedClass) throws ServiceException {

		retrievalCount++;

		if (id==null) {
			throw new ServiceException("Trying to retrieve object with name 'null'");
		}

		// TODO: This souldn't be a configexception. Review exceptions.
		WeakReference<Object> ref = objects.get(id);
		if (ref == null) {
			throw new ConfigException("No object found when resolving reference '" + id + "'");
		}

		Object o = ref.get();
		if (o == null) {
			throw new ConfigException("Trying to retrieve a garbage collected object '" + id + "' (the object does not exist anymore but it was not removed from the directory)");
		}

		if (! expectedClass.isAssignableFrom(o.getClass())) {
			throw new ConfigException("Object retrieved " + o + " is not an instance of the expected class " + expectedClass);
		}

		// We have confirmed that this cast is legal
		@SuppressWarnings("unchecked") T objectAs = (T) o;

		return objectAs;
	}

	/**
	 * <p>Evaluates if the directory contains an object with a given name. This is an internal function
	 * used by the Registry and should not be called by users.</p>
	 */
	public synchronized boolean containsObject (String id) {

		if (id==null) {
			throw new ServiceException("Trying to evaluate if directory contains an object with name 'null'");
		}

		WeakReference<Object> ref = objects.get(id);
		if (ref == null) return false;

		if (ref.get() == null) {
			throw new ConfigException("Detected a garbage collected object when evaluating if directory contains an object with name '" + id + "' (the object does not exist anymore but it was not removed from the directory)");
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public synchronized String toString() {
		return "Directory[#items=" + this.getSize() + "]";
	}

	/**
	 * <p>Returns the current number of retrieval attempts made to this directory.</p>
	 */
	public synchronized int getRetrievalCount() {
		return retrievalCount;
	}

	/**
	 * @return the peakSize
	 */
	public synchronized int getPeakSize() {
		return peakSize;
	}

	/**
	 * Returns the current directory size.
	 */
	public synchronized int getSize() {
		return objects.size();
	}

	public synchronized Set<String> getKeys() {
		return objects.keySet();
	}

	/**
	 * <p>Registers an object to be updated when the Directory entry named as the
	 * 'id' argument changes. The object will also be receive an initial update
	 * to synchronize it with the value in the repository. If the 'id' doesn't
	 * exist in the Directory, null is injected.</p>
	 * <p>The object resolved will be injected into the 'field' attribute using
	 * a public setter.</p>
	 */
	public synchronized void register(Object object, String field, String id) {
		registry.register(object, field, id);
	}

}
