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

package net.jgf.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * <p>This helper class provides static methods to instantiate objects from {@link Config}.</p>
 * <p>Note that this class doesn't require objects to be components in order to be created. All it
 * requires then is to be annotated as {@link Configurable}, and thus to provide a
 * <tt>readConfig(..)</tt> method.</p>
 * @see Configurable
 * @see Config
 * @author jjmontes
 */
public final class ConfigurableFactory {

	/**
	 * Class logger.
	 */
	private static final Logger logger = Logger.getLogger(ConfigurableFactory.class);

	/**
	 * The constructor is private to Avoid instantiation.
	 */
	private ConfigurableFactory() {
	}

	/**
	 * Creates a new list of objects based on a repetition of configuration elements in the configuration file.
	 * @param <T>
	 * @param config the {@link Config} to read from
	 * @param configPath the XPath expression of the root element of the list of elements to be read
	 * @param expectedClass the class of the expected type of the elements of the list created
	 * @return a {@link List} of objects of the expected class
	 */
	public static <T> List<T> newListFromConfig(Config config, String configPath, Class<T> expectedClass) {

		logger.trace("Creating list of objects from configuration at " + configPath);

		List<String> elementIds = config.getList(configPath + "/@id");

		List<T> elements = new ArrayList<T>();
		for (String elementId : elementIds) {
			T element = ConfigurableFactory.newFromConfig(config, configPath + "[@id='" + elementId + "']", expectedClass);
			elements.add(element);
		}

		return elements;

	}

	/**
	 * <p>Creates a component loading parameters from configuration, and if it is {@link Configurable},
	 * configures it recursively.</p>
	 * @param config the {@link Config} to read from
	 * @param configPath the XPath expression of the root element of the list of elements to be read
	 * @param expectedClass the class of the expected type of the elements of the list created
	 * @return the object created
	 */
	public static <T> T newFromConfig(Config config, String configPath, Class<T> expectedClass) {

		logger.trace ("Creating object from configuration at '" + configPath + "'");

		// Read class
		String className = config.getString(configPath + "/@class");
		if (className == null) {
			throw new ConfigException("Attribute 'class' missing in configuration object at node '" + configPath + "'");
		}

		Class<?> classType = null;
		try {
			classType = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ConfigException("Class not found '" + className + "' when creating object with prefix '" + configPath + "'", e);
		} catch (Throwable e) {
			throw new ConfigException("Class search error '" + className + "' when creating object with prefix '" + configPath + "'", e);
		}

		// Check if the class is the expected class, or throw an exception
		if (! expectedClass.isAssignableFrom(classType)) {
			throw new ConfigException("Element at " + configPath + " is not a " + expectedClass.getCanonicalName() + ", but a " + classType.getCanonicalName());
		}

		// Cast securely, has we have checked type above
		@SuppressWarnings("unchecked") Class<T> boundedClassType = (Class<T>) classType;

		// Create the object
		T newInstance = null;
		try {
			newInstance = boundedClassType.newInstance();
		} catch (InstantiationException e) {
			throw new ConfigException ("Element " + configPath + " could not be instantiated from config (tip: check it has a public void constructor, is not abstract...)", e);
		} catch (IllegalAccessException e) {
			throw new ConfigException ("Element " + configPath + " could not be instantiated from config (tip: check it has a public void constructor, is not abstract...)", e);
		}

		// Call configurable object factory
		// Check the class is Configurable, otherwise we can't instantiate from config
		if (classType.isAnnotationPresent(Configurable.class)) {

			try {
				Method factoryMethod = classType.getMethod(Configurable.READCONFIG_METHOD_NAME, Config.class, String.class);
				factoryMethod.invoke(newInstance, config, configPath);
			} catch (NoSuchMethodException e) {
				throw new ConfigException ("Element " + configPath + " is marked as @Configurable but doesn't provide a configuration method '" + Configurable.READCONFIG_METHOD_NAME + "'", e);
			} catch (IllegalAccessException e) {
				throw new ConfigException ("Element " + configPath + " could not be instantiated from config (tip: check it has a public void constructor, is not abstract...)", e);
			} catch (InvocationTargetException e) {
				throw new ConfigException ("Element " + configPath + " could not be created", e);
			}

		} else {
			throw new ConfigException ("Element " + configPath + " of class " + classType.getCanonicalName() + " is not a @Configurable type");
		}

		return newInstance;

	}

}
