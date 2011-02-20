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

import net.jgf.core.naming.DirectoryRegistry.RegistryInjectionMethod;
import net.jgf.core.service.ServiceException;
import net.jgf.system.Jgf;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * <p><b>Directory</b></p>
 * <p>
 * Manages a directory of objects, which are accessed by id. Two objects with
 * the same id cannot exist in the Directory. Adding an object with an Id that
 * already exists in the Directory will replace the current object.
 * </p>
 * <p>
 * Objects are stored using weak references, so they will be garbage collected
 * and disappear from the directory if they are not referenced from somewhere
 * else. Therefore, user  need to keep their own references to objects that they
 * manually include in the Directory.
 * </p>
 * <p>
 * The directory will silently discard objects with names beginning with
 * character '!', so they are not added to the directory. You can use
 * this whenever don't want a component to be automatically added to the
 * Directory by JGF or your custom classes.
 * </p>
 * <p>
 * Only one Directory should exist per JGF application. Users shouldn't need 
 * to create Directory instances. The Directory can be accessed statically through 
 * the Jgf class (see {@link Jgf#getDirectory()}). 
 * </p>
 * <p><b>Usage</b></p>
 * <p>Entries can be added to the Directory using the {@link Directory#addObject(String, Object)}
 * method. Normally, JGF out-of-the-box classes add all components created to the Directory
 * (if you don't wish to do so, you can use ids starting with an exclamation mark '!').</p>
 * <p>Objects can be obtained from the directory in two different ways.</p>
 * <p>The method {@link Directory#getObjectAs(String, Class)} will resolve an id and
 * return the object stored in the Directory. Applications should be using it when they need a
 * reference immediately, although they may keep the reference for later usage if they will 
 * be using it repeatedly.</p>
 * <p>Alternatively, objects can register with the Directory so they are updated whenever
 * a particular directory entry is modified (using {@link Directory#register(Object, String, String)}).
 * This is a convenient way of ensuring that an object field contains a reference to a defined
 * directory entry, even if that entry hasn't yet been added to the Directory.</p>
 * <p>Numerous examples of both types of usage are spread along JGF examples.</p>  
 * <p>
 * This class is thread safe. All accesses to fields are synchronized. Reference
 * fields are never returned.
 * </p>
 * 
 * @author jjmontes
 * @version 1.0
 */
public final class Directory {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(Directory.class);

    /**
     * Initial default estimated directory size. This is the initial capacity of
     * the underlying map.
     */
    private static final int DIRECTORY_DEFAULT_SIZE = 256;

    /**
     * <p>
     * The directory will silently discard objects with names beginning with
     * character '!'.
     * </p>
     */
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
     * Map of objects stored in the directory. They are stored as
     * {@link WeakReference} references.
     */
    private Map<String, WeakReference<Object>> objects;

    /**
     * Directory registry, which sets references to objects that have asked for
     * them.
     */
    private DirectoryRegistry registry;

    /**
     * <p>Builds a directory. JGF build the Directory internally: users shouldn't need
     * to instantiate this class. Instead, access the directory statically through
     * {@link Jgf#getDirectory()}.</p>
     * @see Jgf#getDirectory()
     */
    public Directory() {
        peakSize = 0;
        retrievalCount = 0;
        registry = new DirectoryRegistry();
        objects = new Hashtable<String, WeakReference<Object>>(
                  DIRECTORY_DEFAULT_SIZE);
    }

    /**
     * <p>Remove an object from the directory. A runtime ConfigException will be
     * thrown if the object doesn't exist in the directory.</p>
     * @param id the Id of the object to remove.
     * @return a reference to the removed object (may be null if the object is no longer referenced elsewhere).
     */
    public synchronized Object removeObject(String id) {

        if (StringUtils.isBlank(id)) {
            throw new NamingException(
                    "Cannot remove object with a blank name: '" + id + "'");
        }
        if (id.charAt(0) == DIRECTORY_EXCLUDING_PREFIX) {
            return null;
        }

        final WeakReference<Object> reference = objects.remove(id);
        if (reference == null) {
            throw new NamingException(
                    "Tried to remove a non existent object named '" + id
                            + "' from " + this);
        }

        // Update the registered objects
        registry.update(id, null);

        return reference.get();

    }

    /**
     * <p>Adds an object to the directory. Exceptions will be thrown if an object
     * with that name already exists in the repository, or if the name provided
     * is blank or null.</p>
     * <p>Note that the object will not be stored if the <i>id</i> starts with the
     * Directory excluding prefix '!'.</p>.
     * 
     * @param id id that will be used to store and access the object.
     * @param object the object to add to Directory.
     */
    public synchronized void addObject(String id, Object object) {

        logger.trace("Adding object " + object + " to " + this);

        if (object == null) {
            throw new NamingException("Trying to add a null object to " + this);
        }

        if (StringUtils.isBlank(id)) {
            throw new NamingException("Cannot add object " + object
                    + " with a blank name: '" + id + "'");
        }

        if (id.charAt(0) == DIRECTORY_EXCLUDING_PREFIX) {
            logger.trace("Not adding object with id '" + id
                    + "' to the directory as it starts with '"
                    + DIRECTORY_EXCLUDING_PREFIX + "'");
            return;
        }

        // Check if the object is duplicated and if so throw an exception
        // TODO: Experimental: currently allowing overwriting, which seems to be better
        /*
        if (objects.containsKey(id)) {
            throw new NamingException("Cannot add object with name " + id 
                    + " because an object with that name already exists");
        }
        */
        
        WeakReference<Object> ref = new WeakReference<Object>(object);
        objects.put(id, ref);

        if (objects.size() > peakSize) {
            peakSize = objects.size();
        }

        // Update the registered objects
        registry.update(id, object);

    }

    /**
     * <p>Returns a object given its name, casted to the desired type.</p>
     * <p>
     * Note that users can also register themselves with the Directory registry,
     * so they receive updated references, using {@link Directory#register(Object, String, String)}
     * </p>
     * <p>
     * An exception is thrown if the object is not present in the Directory.
     * </p>
     * 
     * @see Directory#register(String, Object, String)
     * 
     * @param <T> the generic type used to ensure the correct type is returned.
     * @param id the id of the object to find.
     * @param expectedClass the expected type of the searched object.
     * @return the object retrieved from the directory, if found.
     */
    public synchronized <T> T getObjectAs(String id, Class<T> expectedClass) {

        retrievalCount++;

        /*
         * if (Jgf.getApp().isDebug()) {
         * logger.info("Retrieving object with id: " + id); }
         */

        if (id == null) {
            throw new ServiceException(
                    "Trying to retrieve object with name 'null'");
        }

        WeakReference<Object> ref = objects.get(id);
        if (ref == null) {
            throw new NamingException(
                    "No object found when resolving reference '" + id + "'");
        }

        Object o = ref.get();
        if (o == null) {
            throw new NamingException(
                    "Trying to retrieve a garbage collected object '"
                            + id
                            + "' (the object does not exist anymore but it was not removed from the directory)");
        }

        if (!expectedClass.isAssignableFrom(o.getClass())) {
            throw new NamingException("Object retrieved " + o
                    + " is not an instance of the expected class "
                    + expectedClass);
        }

        // We have confirmed that this cast is legal
        @SuppressWarnings("unchecked")
        T objectAs = (T) o;

        return objectAs;
    }

    /**
     * <p>
     * Evaluates if the directory contains an object with a given name users. Note that
     * this method checks that the object is still correctly referenced (i.e. it hasn't been
     * garbage collected).
     * </p>
     * 
     * @param id the id be checked for existence in the directory. 
     * @return true if the Directory contains an entry for the given id.
     */
    public synchronized boolean containsObject(String id) {

        if (id == null) {
            throw new ServiceException(
                    "Trying to evaluate if directory contains an object with name 'null'");
        }

        WeakReference<Object> ref = objects.get(id);
        if (ref == null) {
            return false;
        }

        if (ref.get() == null) {
            throw new NamingException(
                    "Detected a garbage collected object when evaluating if directory contains an object with name '"
                            + id
                            + "' (the object does not exist anymore but it was not removed from the directory)");
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String toString() {
        return "Directory[#items=" + this.getSize() + "]";
    }

    /**
     * <p>
     * Returns the current number of retrieval attempts made to this directory.
     * This doesn't include references resolved through registration, only
     * calls to {@link Directory#getObjectAs(String, Class)}.
     * </p>
     * @return current object retrieval count.
     */
    public synchronized int getRetrievalCount() {
        return retrievalCount;
    }

    /**
     * Returns directory peak size: highest count of objects stored at a moment in time.
     * @return directory peak size.
     */
    public synchronized int getPeakSize() {
        return peakSize;
    }

    /**
     * Returns the current directory size.
     * @return current Directory count of stored objects.
     */
    public synchronized int getSize() {
        return objects.size();
    }

    /**
     * Returns the set of the ids stored in the Directory. 
     * @return the set of the ids stored in the Directory.
     */
    public synchronized Set<String> getIds() {
        return objects.keySet();
    }

    /**
     * <p>
     * Registers an object to be updated when the Directory entry named as the
     * 'id' argument changes. The object will also be receive an initial update
     * to synchronize it with the value in the repository. If the 'id' doesn't
     * exist in the Directory, null is injected.
     * </p>
     * 
     * @param object the object on which the field will be set.
     * @param accesorName name of the field that will be set (a public setter needs to exist).
     * @param id id that will be registered.
     */
    public synchronized void register(Object object, String accesorName, String id) {
        registry.register(object, accesorName, id, RegistryInjectionMethod.ALL);
    }
    
    /**
     * <p>
     * Registers an object to be updated when the Directory entry named as the
     * 'id' argument changes. The object will also be receive an initial update
     * to synchronize it with the value in the repository. If the 'id' doesn't
     * exist in the Directory, null is injected.
     * </p>
     * 
     * @param object the object on which the field will be set.
     * @param accessorName name of the field that will be set (a public setter needs to exist).
     * @param id id that will be registered.
     */
    public synchronized void register(Object object, String accessorName, String id, RegistryInjectionMethod injectionMethod) {
        registry.register(object, accessorName, id, injectionMethod);
    }    

    /**
     * <p>Unregisters a pair object+field from the directory registry, so it won't receive any
     * further updates for the specified field.</p> 
     * @param object the object that won't receive more updates for the given field.
     * @param field the name of the object field that will be unregistered. 
     */
    public synchronized void unregister(Object object, String field) {
        registry.unregister(object, field);
    }

}
