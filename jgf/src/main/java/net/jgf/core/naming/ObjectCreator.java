/*
 * JGF - Java Game Framework
 * $Id: ConfigurableFactory.java 176 2009-11-01 02:45:50Z jjmontes $
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

import net.jgf.config.ConfigException;

import org.apache.log4j.Logger;

/**
 * <p>
 * </p>
 * 
 * @see Register
 * @see DirectoryRegistry
 * @see Directory
 * @author jjmontes
 */
public final class ObjectCreator {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(ObjectCreator.class);

    /**
     * The constructor is private to Avoid instantiation.
     */
    private ObjectCreator() {
    }

    public static Object createObject(String className) {
        try {
            Class<?> clazz = (Class<?>) Class.forName(className);
            return createObject(clazz);
        } catch (ClassNotFoundException e) {
            throw new ConfigException("Invalid class '" + className + "' to be created.");
        } 
    }
    
    public static <T> T createObject(Class<T> expectedClass) {
        
        T newObject = null;
        
        try {
            newObject = expectedClass.newInstance();
        } catch (InstantiationException e) {
            throw new ConfigException("Could not create class '" + expectedClass + "'.", e);
        } catch (IllegalAccessException e) {
            throw new ConfigException("Could not create class '" + expectedClass + "'.", e);
        }
        
        RegisterAnnotationProcessor.processRegisterAnnotation(newObject);
        
        return newObject;

    }
    
}
