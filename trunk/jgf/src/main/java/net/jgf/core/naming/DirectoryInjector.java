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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.jgf.config.ConfigException;
import net.jgf.system.System;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @see Directory
 * @author jjmontes
 */
@Deprecated
public final class DirectoryInjector {

	/**
	 * Class logger.
	 */
	private static final Logger logger = Logger.getLogger(DirectoryInjector.class);

	/**
	 * The constructor is private to Avoid instantiation.
	 */
	private DirectoryInjector() {
	}

	public static void inject(Object obj) {
		// Iterate over object fields looking for DirectoryRef annotations
		Method[] methods = obj.getClass().getMethods();
		for (Method method : methods) {
			Boolean ignore = false;
			DirectoryRef annotation = method.getAnnotation(DirectoryRef.class);
			if (annotation != null) {
				// It is annotated: Inject dependency

				// Check if the field is already filled
				String getter = method.getName().substring(3);
				getter = String.valueOf(getter.toLowerCase().charAt(0)) + getter.substring(1);
				Object currentValue = null;
				try {
					currentValue = BeanUtils.getProperty(obj, getter);
				} catch (InvocationTargetException e) {
					throw new ConfigException("No correct accesor (" + getter + ") for setter " + method.getName() + " in object " + obj +
							" was found when injecting reference for field " + method.getName() , e);
				} catch (NoSuchMethodException e) {
					throw new ConfigException("No accesor (" + getter + ") for setter " + method.getName() + " in object " + obj +
							" was found when injecting reference for field " + method.getName() , e);
				} catch (IllegalAccessException e) {
					throw new ConfigException("No correct accesor (" + getter + ") for setter " + method.getName() + " in object " + obj +
							" was found when injecting reference for field " + method.getName() , e);
				}

				String id = annotation.ref();

				if (StringUtils.isNotBlank(annotation.field())) {
					// Retrieve the field that contains the reference
					if (StringUtils.isNotBlank(id)) {
						throw new ConfigException("Cannot define both 'ref' and 'field' in annotated field " + method.getName() + " of object " + obj);
					}
					try {
						String refFieldValue = BeanUtils.getProperty(obj, annotation.field());
						if (StringUtils.isBlank(refFieldValue)) {
							if (currentValue == null) {
								throw new ConfigException("No reference found in field " + annotation.field() + " of object " + obj +
										" when injecting reference for field " + method.getName() );
							}
							// Ignore this setter, as there is already a property setted
							ignore = true;
						}
						id = refFieldValue;
					} catch (InvocationTargetException e) {
						throw new ConfigException("No correct accesor (getter) for field " + annotation.field() + " in object " + obj +
								" was found when injecting reference for field " + method.getName() , e);
					} catch (NoSuchMethodException e) {
						throw new ConfigException("No accesor (getter) for field " + annotation.field() + " in object " + obj +
								" was found when injecting reference for field " + method.getName() , e);
					} catch (IllegalAccessException e) {
						throw new ConfigException("No correct accesor (getter) for field " + annotation.field() + " in object " + obj +
								" was found when injecting reference for field " + method.getName() , e);
					}
				}

				if (!ignore) {

					Object refObj = System.getDirectory().getObjectAs(id, method.getParameterTypes()[0]);

					try {
						method.invoke(obj, new Object[] {refObj});
					} catch (IllegalArgumentException e) {
						throw new ConfigException("Invalid object " + refObj + " injected for property " + method.getName() + " of object " + obj);
					} catch (IllegalAccessException e) {
						throw new ConfigException("Could not access setter for injected property " + method.getName() + " of object " + obj);
					} catch (InvocationTargetException e) {
						throw new ConfigException("Could not access setter for injecter property " + method.getName() + " of object " + obj);
					}

				}

			}
		}
	}

}
