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

import java.net.URL;
import java.util.List;

import net.jgf.jme.config.JmeConfigHelper;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.log4j.Logger;

import com.jme.util.resource.ResourceLocatorTool;


/**
 * <p>Holds JGF configuration, providing access to the XML configuration values.
 * Configuration and the configuration file (which is an XML file) is to JGF a central resource,
 * because all initialization and services are loaded from the configuration file.</p>
 * <p>Provides methods to read common primitive types. If a default value is not specified
 * and the configuration key is missing, a {@link ConfigException} is thrown.</p>
 * <p>This class relies on Apache Commons Configuration to do its job. The library is
 * configured to use the XPath resolver, and thus configuration elements are accessed
 * using XPath notation. More information can be found in the documentation of
 * {@link XMLConfiguration} and Apache Commons COnfiguration in general.</p>
 * </p>Some modules provide helper classes that expand this class functionality. For
 * example, the JME module provides {@link JmeConfigHelper}, which provides methods
 * to read more specific types.</p>
 * <p>This class allows variable interpolation, which means that it is possible to
 * use expressions like <tt>${application/debug}</tt> to make a reference to a value
 * contained in a different configuration element (this variable interpolation is
 * provided by Commons Configuration).</p>
 * <p>This class is thread safe: Commons Config XMLConfiguration is thread
 * safe only for read only operations. As Config doesn't allow writing, it is thread safe.</p>
 *
 * @author jjmontes
 * @version $Revision$
 */
public final class Config  {

	/**
	 * Class logger.
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Config.class);

	/**
	 * The Apache Commons Configuration object used to process the XML configuration file.
	 */
	private XMLConfiguration config;

	/**
	 * The path to the resource from where configuration was loaded.
	 */
	private String configResourcePath;

	/**
	 * <p>Initializes a configuration object from the XML file located by the path provided.</p>
	 * @param configResourcePath
	 * @throws ConfigException
	 */
	public Config(String configResourcePath) throws ConfigException {

		this.configResourcePath = configResourcePath;
		URL configURL = ResourceLocatorTool.locateResource("config", configResourcePath);

		try {
			config = new XMLConfiguration(configURL);
			config.setExpressionEngine(new XPathExpressionEngine());
		} catch (ConfigurationException e) {
			throw new ConfigException ("Could not read configuration from '" + configResourcePath + "'", e);
		}

		// TODO: Process includes recursively (Commons Config composite configuration) or leave it up to XML include?

	}

	/**
	 * Returns the path to the resource from where this configuration was loaded.
	 * @return the path to the resource from where this configuration was loaded
	 */
	public String getConfigResourcePath() {
		return configResourcePath;
	}

	/**
	 * Retrieves a value from configuration and returns it as a float.
	 * @param key the configuration element path (XPath expression)
	 * @param defaultValue the value to be returned if the configuration element is not found
	 * @return the resulting value
	 * @see org.apache.commons.configuration.AbstractConfiguration#getFloat(java.lang.String, float)
	 */
	public float getFloat(String key, float defaultValue) {
		return config.getFloat(key, defaultValue);
	}

	/**
	 * Retrieves a value from configuration and returns it as a float. If the specified configuration
	 * element is not found, an {@link ConfigException} is thrown.
	 * @param key the configuration element path (XPath expression)
	 * @return the resulting value
	 * @see org.apache.commons.configuration.AbstractConfiguration#getFloat(java.lang.String)
	 */
	public float getFloat(String key) {
		if (! config.containsKey(key)) {
			throw new ConfigException("Configuration value missing at " + key);
		}
		return config.getFloat(key);
	}

	/**
	 * Retrieves a value from configuration and returns it as an integer.
	 * @param key the configuration element path (XPath expression)
	 * @param defaultValue the value to be returned if the configuration element is not found
	 * @return the resulting value
	 * @see org.apache.commons.configuration.AbstractConfiguration#getInt(java.lang.String, int)
	 */
	public int getInt(String key, int defaultValue) {
		return config.getInt(key, defaultValue);
	}

	/**
	 * Retrieves a value from configuration and returns it as an integer. If the specified configuration
	 * element is not found, an {@link ConfigException} is thrown.
	 * @param key the configuration element path (XPath expression)
	 * @return the resulting value
	 * @see org.apache.commons.configuration.AbstractConfiguration#getInt(java.lang.String)
	 */
	public int getInt(String key) {
		if (! config.containsKey(key)) {
			throw new ConfigException("Configuration value missing at " + key);
		}
		return config.getInt(key);
	}

	/**
	 * Retrieves a value from configuration and returns it as a String.
	 * @param key the configuration element path (XPath expression)
	 * @param defaultValue the value to be returned if the configuration element is not found
	 * @return the resulting value
	 * @see org.apache.commons.configuration.AbstractConfiguration#getString(java.lang.String, java.lang.String)
	 */
	public String getString(String key, String defaultValue) {
		return config.getString(key, defaultValue);
	}

	/**
	 * Retrieves a value from configuration and returns it as a String. If the specified configuration
	 * element is not found, an {@link ConfigException} is thrown.
	 * @param key the configuration element path (XPath expression)
	 * @return the resulting value
	 * @see org.apache.commons.configuration.AbstractConfiguration#getString(java.lang.String)
	 */
	public String getString(String key) {
		if (! config.containsKey(key)) {
			throw new ConfigException("Configuration value missing at " + key);
		}
		return config.getString(key);
	}



	/**
	 * Retrieves a value from configuration and returns it as a boolean value.
	 * @param key the configuration element path (XPath expression)
	 * @param defaultValue the value to be returned if the configuration element is not found
	 * @return the resulting value
	 * @see org.apache.commons.configuration.AbstractConfiguration#getBoolean(java.lang.String, boolean)
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		return config.getBoolean(key, defaultValue);
	}

	/**
	 * Retrieves a value from configuration and returns it as a boolean. If the specified configuration
	 * element is not found, an {@link ConfigException} is thrown.
	 * @param key the configuration element path (XPath expression)
	 * @return the resulting value
	 * @see org.apache.commons.configuration.AbstractConfiguration#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String key) {
		if (! config.containsKey(key)) {
			throw new ConfigException("Configuration value missing at " + key);
		}
		return config.getBoolean(key);
	}

	/**
	 * Returns a list of the string values that correspond to the XPath expression entered as key.
	 * @param key the configuration element path (XPath expression)
	 * @return the list of values
	 * @see org.apache.commons.configuration.AbstractConfiguration#getList(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<String> getList(String key) {
		return config.getList(key);
	}

	/**
	 * Tells whether a particular configuration element exists.
	 * @param key the configuration element path (XPath expression)
	 * @return true if the configuration element specified exists.
	 * @see org.apache.commons.configuration.AbstractHierarchicalFileConfiguration#containsKey(java.lang.String)
	 */
	public boolean containsKey(String key) {
		return config.containsKey(key);
	}

}
