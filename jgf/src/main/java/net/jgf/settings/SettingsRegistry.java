/*
 * JGF - Java Game Framework
 * $Id: Registry.java 171 2009-10-31 22:52:02Z jjmontes $
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

package net.jgf.settings;

import java.lang.ref.WeakReference;
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
 * The Registry holds object registrations with the settings manager, and updates
 * registered objects about changes done to settings.
 * </p>
 * 
 * @author jjmontes
 * @version 1.0
 */
final class SettingsRegistry {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(SettingsRegistry.class);

    /**
     * Initial default estimated registry size. This is the initial capacity of
     * the underlying map.
     */
    private static final int DEFAULT_REGISTRY_SIZE = 256;
    
    private SettingsManager manager;
    
    /**
     * A Registry Setter entry, which includes a field name and a WeakReference
     * to the object where the value resolved from Settings will be set.
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
     * A registry entry, which maps a setting id with a number of setters
     * defined for different objects, which will be updated when the setting
     * is updated.
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
     * Map of entries stored in the registry. This maps setting ids with
     * RegistryEntries.
     */
    private Map<String, RegistryEntry> entries = new HashMap<String, RegistryEntry>(
            DEFAULT_REGISTRY_SIZE);

    /**
     * Builds a Registry instance.
     */
    SettingsRegistry(SettingsManager manager) {
        this.manager = manager;
    }

    /**
     * Registers an object and field  with this settings registry.
     * @see Settings#register(String, Object, String)
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
                        "Tried to register an already registered Setting dependency on " + "id '"
                                + id + "' for object " + target + " and field '" + field + "'");
            }
        }

        // Provide an initial update (only if the setting exists already)
        if (manager.containsKey(id)) {
            updateObject(setter, manager.getSetting(id).getValue());
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

            String setterName = "set" + String.valueOf(field.charAt(0)).toUpperCase();
            if (field.length() > 1)
                setterName += field.substring(1);

            try {
                logger.debug("Injecting field '" + field + "'=" + value + " of object " + target);
                Method[] methods = target.getClass().getMethods();

                // Find a setter with the appropriate parameter type
                boolean found = false;
                for (Method method : methods) {
                    if (method.getName().equals(setterName)) {
                        if ((method.getParameterTypes().length == 1)
                                && (method.getReturnType() == void.class)) {
                            if ((method.getParameterTypes()[0].isInstance(value))
                                    || (value == null)) {
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
                    throw new ConfigException("No setter found (of correct type) '"
                            + setterName + "' found in object " + target
                            + " when setting value for settings resolved reference: " + value);
                }

            } catch (IllegalAccessException e) {
                throw new ConfigException("No accessible setter (of correct type) '" + setterName
                        + "' found in object " + target
                        + " when setting value for settings resolved reference " + value, e);
            } catch (InvocationTargetException e) {
                throw new ConfigException("Error calling setter '" + setterName
                        + "' found in object " + target
                        + " when setting value for settings resolved reference " + value, e);
            }

        } else {
            // Everything should be unregistered
            throw new ConfigException(
                    "Null reference for target object when injecting a Setting value "
                            + value + " into field '" + field
                            + "' (registered object no longer exists but wasn't unregistered, registered object was: "
                            + setter.targetToString + ")");
        }

    }

}
