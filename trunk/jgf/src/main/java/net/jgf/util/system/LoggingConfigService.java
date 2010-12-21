/*
 * JGF - Java Game Framework
 * $Id: SystemInfoService.java 161 2009-10-11 19:17:58Z jjmontes $
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

package net.jgf.util.system;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.core.service.BaseService;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * <p>This service can be used to configure the logging library without
 * the need to use a separate logging configuration file.</p>
 * <p>It only allows to manipulate a reduced set of configuration options.</p>
 *
 * @author jjmontes
 */
@Configurable
public final class LoggingConfigService extends BaseService {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(LoggingConfigService.class);

	/**
	 * Initializes this service. This creates the Timer that will schedule the Task that this
	 * service runs.
	 * @see net.jgf.core.service.BaseService#initialize()
	 */
	@Override
	public void initialize() {

		super.initialize();

		// Filter some verbose categories
		/*
		Logger.getLogger("org.apache.commons.digester.Digester").setLevel(Level.FATAL);
		Logger.getLogger("org.apache.commons.digester.Digester.sax").setLevel(Level.INFO);
		Logger.getLogger("org.apache.commons.beanutils.MethodUtils").setLevel(Level.INFO);
		Logger.getLogger("org.apache.commons.beanutils.BeanUtils").setLevel(Level.INFO);
		Logger.getLogger("org.apache.commons.beanutils.ConvertUtils").setLevel(Level.INFO);
		Logger.getLogger("org.apache.commons.vfs.impl.StandardFileSystemManager").setLevel(Level.INFO);
		*/

	}

	public void setLoggerLevel(String loggerName, Level level) {
		logger.debug("Setting logger " + loggerName + " to level " + level);
		Logger.getLogger(loggerName).setLevel(level);
	}
	

	/**
	 * Configures this object from Config.
	 * @see Configurable
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		int index = 1;
		while (config.containsKey(configPath + "/logger[" + index + "]/@name")) {
			String loggerName = config.getString(configPath + "/logger[" + index + "]/@name");
			String levelStr = config.getString(configPath + "/logger[" + index + "]/@level");
			if ("INFO".equals(levelStr.toUpperCase())) {
				setLoggerLevel(loggerName, Level.INFO);
			} else if ("FATAL".equals(levelStr.toUpperCase())) {
				setLoggerLevel(loggerName, Level.FATAL);
			} else if ("WARN".equals(levelStr.toUpperCase())) {
				setLoggerLevel(loggerName, Level.WARN);
			} else if ("ERROR".equals(levelStr.toUpperCase())) {
				setLoggerLevel(loggerName, Level.ERROR);
			} else if ("DEBUG".equals(levelStr.toUpperCase())) {
				setLoggerLevel(loggerName, Level.DEBUG);
			} else if ("TRACE".equals(levelStr.toUpperCase())) {
				setLoggerLevel(loggerName, Level.TRACE);
			} else {
				throw new ConfigException("Invalid logging level '" + levelStr + "' for logger '" + 
						loggerName + "' (must be one of TRACE, DEBUG, INFO, WARN, ERROR, FATAL)");
			}
			
			index++;
		}
		
	}



}
