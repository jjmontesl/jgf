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

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jgf.config.ConfigException;
import net.jgf.system.Jgf;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * The DirectoryRegistry holds object registrations with the directory, and updates
 * registered objects about changes done to directory entries.
 * </p>
 * <p>
 * <p>
 * This class is not intended to be used directly JGF applications users. Check
 * the documentation for class {@link Directory}, which contains the interface
 * used to register objects with the directory registry (@link
 * {@link Directory#register(Object, String, String)} and
 * {@link Directory#unregister(Object, String)}.
 * </p>
 * </p>
 * <p>
 * Note that this class is not synchronized itself, but only one DirectoryRegistry exists
 * per Directory and the DirectoryRegistry itself is never made accessible outside the
 * Directory.
 * </p>
 * 
 * @author jjmontes
 * @version 1.0
 */
final class DirectoryRegistry {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(DirectoryRegistry.class);

    /**
     * A Registry Setter entry, which includes a field name and a WeakReference
     * to the object where the value resolved from the Directory will be set.
     */
    private final class RegistrySetterEntry {

        private WeakReference<Object> targetWeakRef;
        private String targetToString;
        private String field;

        private RegistrySetterEntry(Object target, String fieldName) {
            this.field = fieldName;
            this.targetWeakRef = new WeakReference<Object>(target);
            this.targetToString = target.toString();
        }

    }

    /**
     * A registry entry, which maps a directory id with a number of setters
     * defined for different objects, which will be updated when the directory
     * entry is updated.
     */
    private final class RegistryEntry {

        private List<RegistrySetterEntry> setters = new ArrayList<RegistrySetterEntry>();

        public RegistryEntry() {
        }

        public RegistrySetterEntry getSetter(Object object, String fieldName) {
            for (RegistrySetterEntry setter : setters) {
                if ((object == setter.targetWeakRef) && (fieldName == setter.field)) {
                    return setter;
                }
            }
            return null;
        }

    }

    /**
     * Initial default estimated registry size. This is the initial capacity of
     * the underlying map.
     */
    private static final int DEFAULT_REGISTRY_SIZE = 256;

    /**
     * Map of entries stored in the registry. This maps directory names with
     * RegistryEntries.
     */
    private Map<String, RegistryEntry> entries = new HashMap<String, RegistryEntry>(
            DEFAULT_REGISTRY_SIZE);

    /**
     * Builds a Registry instance.
     */
    DirectoryRegistry() {
        super();
    }

    /**
     * Registers an object and field  with the directory registry.
     * @see Directory#register(String, Object, String)
     */
    void register(Object target, String field, String id) {

        logger.debug("Registering field " + field + " for id " + id + " on object " + target);

        if (StringUtils.isBlank(field)) {
            throw new ConfigException("Tried to register object " + target + " for id '" + id
                    + "' for a null or blank field");
        }

        // Add the entry
        RegistrySetterEntry setter = null;
        RegistryEntry entry = entries.get(id);
        if (entry == null) {
            entry = new RegistryEntry();
            setter = new RegistrySetterEntry(target, field);
            entry.setters.add(setter);
            entries.put(id, entry);
        } else {
            setter = entry.getSetter(target, field);
            if (setter == null) {
                setter = new RegistrySetterEntry(target, field);
                entry.setters.add(setter);
            } else {
                throw new ConfigException(
                        "Tried to register an already registered Directory dependency on " + "id '"
                                + id + "' for object " + target + " and field '" + field + "'");
            }
        }

        // Provide an initial update (even if it is null)
        if (Jgf.getDirectory().containsObject(id)) {
            updateObject(setter, Jgf.getDirectory().getObjectAs(id, Object.class));
        } else {
            updateObject(setter, null);
        }

    }

    /**
     * <p>Removes an object and field from registry. That field won't be updated
     * again for that object.</p>
     * @param object
     * @param field
     */
    void unregister(Object object, String field) {

        logger.debug("Unregistering field " + field + " on object " + object);

        if (StringUtils.isBlank(field)) {
            throw new ConfigException("Tried to unregister object " + object
                    + " for a null or blank field");
        }

        // TODO: There should be a list of registered objects, instead of
        // walking the list. This algorithm works in case more than one
        // id was registered for the same object (which is anyway illegal).

        for (RegistryEntry entry : entries.values()) {
            Iterator<RegistrySetterEntry> it = entry.setters.iterator();
            while (it.hasNext()) {
                RegistrySetterEntry setter = (RegistrySetterEntry) it.next();
                if ((setter.field == field) && (setter.targetWeakRef.get() == object)) {
                    it.remove();
                }
            }
        }

    }

    /**
     * Updates the registered objects for a specific id.
     * @param id the id that has changed, and for which registered objects will be updated.
     * @param value the value to set.
     */
    void update(String id, Object value) {

        RegistryEntry entry = entries.get(id);
        if (entry != null) {
            for (RegistrySetterEntry setter : entry.setters) {
                updateObject(setter, value);
            }
        }

    }

    
    /**
     * <p>Updates an object field with the given value. This method searches
     * an appropriate setter exists. An exception will be thrown if the
     * operation fails.</p>
     * @param setter the registry setter entry that will be updated (including the target object and field name).
     * @param value the value to set.
     */
    void updateObject(RegistrySetterEntry setter, Object value) {

        Object target = setter.targetWeakRef.get();
        String field = setter.field;

        if (target != null) {

            if (!injectSetter(target, field, value)) {
                if (!injectField(target, field, value, target.getClass())) {
                    throw new NamingException("Field or setter for field '" + field + "' not found when injecting value on object " + target);
                }
            }
            

        } else {
            // Everything should be unregistered
            throw new ConfigException(
                    "Null reference for target object when injecting a Directory object "
                            + value + " into field '" + field
                            + "' (registered object no longer exists but wasn't unregistered, registered object was: "
                            + setter.targetToString + ")");
        }

    }
    
    private boolean injectField(Object target, String field, Object value, Class<?> targetClass) {
        
        boolean injected = false;
        
        Field f = null;
        Field[] fields = targetClass.getDeclaredFields();

        // TODO: Search and store field reference to avoid repeated searches
        for (Field tf : fields) {
            if (tf.getName().equals(field)) {
                f = tf;
            }
        }
        
        
        if (f == null) {
            // If field was not found: recursively try parent classes if they exist
            
            for (Class<?> interfaceClass : targetClass.getInterfaces()) {
                injected = injected | injectField(target, field, value, interfaceClass);
            }
            // If field was not found: recursively try parent classes if they exist
            if ((!injected) && (targetClass.getSuperclass() != null)) {
                injected = injected | injectField(target, field, value, targetClass.getSuperclass());
            } 
            
        } else {
        
            // If field was found:
            if (logger.isDebugEnabled()) {
                logger.debug("Injecting [field] field '" + field + "'=" + value + " on object " + target);
            }
        
            try {
                if (f.isAccessible()) {
                    f.set(target, value);
                } else {
                    f.setAccessible(true);
                    f.set(target, value);
                    f.setAccessible(false);
                }
                injected = true;
            } catch (IllegalAccessException e) {
                throw new ConfigException("Could not access field '" + field
                        + "' in object " + target
                        + " when setting value for directory resolved reference " + value, e);
            }
            
        }
        
        return injected;

    }
    
    private boolean injectSetter(Object target, String field, Object value) {
        
        boolean injected = false;
        
        String setterName = "set" + String.valueOf(field.charAt(0)).toUpperCase();
        if (field.length() > 1)
            setterName += field.substring(1);
        
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Injecting [setter] field '" + field + " with value " + value + " on object " + target);
            }
            Method[] methods = target.getClass().getMethods();

            // Find a setter with the appropriate parameter type
            for (Method method : methods) {
                if (method.getName().equals(setterName)) {
                    if ((method.getParameterTypes().length == 1)
                            && (method.getReturnType() == void.class)) {
                        if ((method.getParameterTypes()[0].isInstance(value))
                                || (value == null)) {
                            // Call the setter
                            // TODO: Cache the method when the object is registered
                            method.invoke(target, value);
                            injected = true;
                            break;
                        }
                    }
                }
            }

        } catch (IllegalAccessException e) {
            throw new ConfigException("Can't access setter '" + setterName
                    + "' found in object " + target
                    + " when setting value for directory resolved reference " + value, e);
        } catch (InvocationTargetException e) {
            throw new ConfigException("Error calling setter '" + setterName
                    + "' found in object " + target
                    + " when setting value for directory resolved reference " + value, e);
        }
        
        return injected;
    }

}
