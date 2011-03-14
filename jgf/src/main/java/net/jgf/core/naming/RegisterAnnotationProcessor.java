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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.jgf.core.naming.DirectoryRegistry.RegistryInjectionMethod;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

/**
 * <p>
 * This helper class processes a class {@link Register} annotations and registers every
 * annotated field or method with the Directory Registry. 
 * </p>
 * 
 * @see Register
 * @see DirectoryRegistry
 * @see Directory
 * @author jjmontes
 */
public final class RegisterAnnotationProcessor {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(RegisterAnnotationProcessor.class);

    /**
     * The constructor is private to Avoid instantiation.
     */
    private RegisterAnnotationProcessor() {
    }
    
    public static void processRegisterAnnotation(Object object) {
        processRegisterAnnotation(object, object.getClass());
    }

    private static void processRegisterAnnotation(Object object, Class<?> clazz) {
        
        // Recursively process parent classes
        if (clazz.getSuperclass() != null) {
            processRegisterAnnotation(object, clazz.getSuperclass());
        }
        
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            Register an = f.getAnnotation(Register.class);
            if (an != null) {
                logger.trace("Processing 'Register' field annotation for ref '" + an.ref() + "' on object " + object);
                Jgf.getDirectory().register(object, f.getName(), an.ref(), RegistryInjectionMethod.FIELD);
            }
        }
        
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            Register an = m.getAnnotation(Register.class);
            if (an != null) {
                logger.trace("Processing 'Register' method annotation for ref '" + an.ref() + "' on object " + object);
                Jgf.getDirectory().register(object, m.getName(), an.ref(), RegistryInjectionMethod.SETTER);
            }
        }
        
    }
    
}
