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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.jgf.config.ConfigException;
import net.jgf.system.Jgf;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>This class is not intended to be used by final framework users.</p>
 * <p>Note that this class is not synchronized, but only one Registry exists per Directory and the
 * Registry itself is never made accessible outside the Directory.</p>
 * @author jjmontes
 */
// TODO: There is a of room for improvement
final class Registry {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(Registry.class);

	/**
	 * A Registry Setter, which includes a field name and a WeakReference to the object where
	 * the value resolved from the Directory will be set.
	 */
	private class RegistrySetter {

		WeakReference<Object> target;
		String field;

		public RegistrySetter(Object target, String fieldName) {
			this.target = new WeakReference<Object>(target);
			this.field = fieldName;
		}

	}

	private class RegistryEntry {

		String id;

		ArrayList<RegistrySetter> setters = new ArrayList<RegistrySetter>();

		public RegistryEntry(String id) {
			this.id = id;
		}

		public RegistrySetter getSetter(Object object, String fieldName) {
			for (RegistrySetter setter : setters) {
				if ((object == setter.target) && (fieldName == setter.field)) {
					return setter;
				}
			}
			return null;
		}

	}

	/**
	 * Initial default estimated registry size. This is the initial capacity of the underlying map.
	 */
	private static final int DEFAULT_REGISTRY_SIZE = 256;

	/**
	 * Map of entries stored in the registry. This maps directory names with
	 * RegistryEntries.
	 */
	private Map<String, RegistryEntry> entries = new HashMap<String, RegistryEntry>(DEFAULT_REGISTRY_SIZE);


	/**
	 * See {@link Directory#register(String, Object, String)}
	 * @see Directory#register(String, Object, String)
	 */
	void register(Object target, String field, String id) {

		if (StringUtils.isBlank(field)) {
			throw new ConfigException("Tried to register object " + target + " for id '" + id + "' for a null or blank field");
		}

		// Add the entry
		RegistryEntry entry = entries.get(id);
		if (entry == null) {
			entry = new RegistryEntry(id);
			RegistrySetter setter = new RegistrySetter(target, field);
			entry.setters.add(setter);
			entries.put(id, entry);
		} else {
			RegistrySetter setter = entry.getSetter(target, field);
			if (setter == null) {
				setter = new RegistrySetter(target, field);
				entry.setters.add(setter);
			} else {
				throw new ConfigException("Tried to register an already registered Directory dependency on " +
						"id '" + id + "' for object " + target + " and field '" + field + "'");
			}
		}

		// Provide an initial update (even if it is null)
		if (Jgf.getDirectory().containsObject(id)) {
			updateObject(target, field,  Jgf.getDirectory().getObjectAs(id, Object.class));
		} else {
			updateObject(target, field,  null);
		}

	}

	void update(String id, Object value) {

		RegistryEntry entry = entries.get(id);
		if (entry != null) {
			for (RegistrySetter setter : entry.setters) {
				Object target = setter.target.get();
				updateObject(target, setter.field, value);
			}
		}

	}

	private void updateObject(Object target, String field, Object value) {

		if (target != null) {

			String setterName = "set" + String.valueOf(field.charAt(0)).toUpperCase();
			if (field.length() > 1) setterName += field.substring(1);

			try {
				logger.debug("Injecting field '" + field + "'=" + value + " of object " + target);
				Method[] methods = target.getClass().getMethods();

				// Find a setter with the appropriate parameter type
				boolean found = false;
				for (Method method : methods) {
					if (method.getName().equals(setterName)) {
						if ((method.getParameterTypes().length == 1) && (method.getReturnType() == void.class)) {
				    	if ((method.getParameterTypes()[0].isInstance(value)) || (value==null)) {
				    		found = true;
				    		// Call the setter
				    		// TODO: Cache the method when the object is registered
				    		method.invoke(target, value);
				    		break;
				    	}
				    }
					}
				}

				if (!found) {
					throw new ConfigException ("No accessible setter (of correct type) '" + setterName + "' found in object " + target + " when setting value for directory resolved reference " + value);
				}

			} catch (IllegalAccessException e) {
				throw new ConfigException("No accessible setter (of correct type) '" + setterName + "' found in object " + target + " when setting value for directory resolved reference " + value, e);
			} catch (InvocationTargetException e) {
				throw new ConfigException("Error calling setter '" + setterName + "' found in object " + target + " when setting value for directory resolved reference " + value, e);
			}

		} else {
			// We should never find a null in a weak reference: everything should be unregistered first
			throw new ConfigException("Null reference for target object when injecting a Directory object " + value + " into field '" + field + "' (the registered object no longer exists but it wasn't unregistered)");
		}

	}

}
