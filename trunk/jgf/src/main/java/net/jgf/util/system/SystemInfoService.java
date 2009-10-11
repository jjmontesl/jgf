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

package net.jgf.util.system;

import java.util.Timer;
import java.util.TimerTask;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.core.service.BaseService;

import org.apache.log4j.Logger;

/**
 * <p>This service logs information about the JVM on a timely manner.</p>
 * <p>Adding this service to an application causes it to periodically dump
 * a line to the log file that includes memory and threads information.</p>
 * <p>Information looks like the following example:</p>
 * <p><tt>Memory: 4690kB / 5852kB Free: 1161kB Max: 65088kB Threads: 4</tt></p>
 *
 * @author jjmontes
 */
@Configurable
public final class SystemInfoService extends BaseService {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(SystemInfoService.class);

	/**
	 * Default interval between reports, in milliseconds.
	 */
	public static int DEFAULT_REPORT_INTERVAL = 15000;

	/**
	 * Interval between reports, in milliseconds. Defaults to {@link #DEFAULT_REPORT_INTERVAL}.
	 */
	protected long reportInterval = DEFAULT_REPORT_INTERVAL;

	/**
	 * Task to run the report.
	 */
	private TimerTask infoTask;

	/**
	 * Timer to schedule the report task.
	 */
	private Timer timer;

	/**
	 * <p>Inner class that implements the task performed on a timely basis.</p>
	 * @author jjmontes
	 */
	private class SystemInfoRunnable extends TimerTask {

		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {

			logSystemInfo();
			timer.schedule(new SystemInfoRunnable(), reportInterval);

		}

	}

	/**
	 * Initializes this service. This creates the Timer that will schedule the Task that this
	 * service runs.
	 * @see net.jgf.core.service.BaseService#initialize()
	 */
	@Override
	public void initialize() {

		super.initialize();

		// Start reporting thread
		timer = new Timer(this.getId());
		infoTask = new SystemInfoRunnable();

		timer.schedule(infoTask, 0);

	}

	/* (non-Javadoc)
	 * @see net.jgf.core.service.BaseService#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		timer.cancel();
	}

	/**
	 * Configures this object from Config.
	 * @see Configurable
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.reportInterval = config.getInt(configPath + "/reportInterval", DEFAULT_REPORT_INTERVAL);

	}

	/**
	 * Sends the system information to the log.
	 * @see SystemInfoService#getSystemInfo()
	 */
	public void logSystemInfo() {
		logger.info(SystemInfoService.getSystemInfo());
	}

	/**
	 * <p>Returns a String containing the system information, like in the following example:</p>
	 * <p><tt>Memory: 4161kB / 5852kB Free: 1690kB Max: 65088kB Threads: 4</tt></p>
	 * @return
	 */
	public static String getSystemInfo() {
		return "Memory Used: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) >> 10) + "kB" + 
		" Free: " + (Runtime.getRuntime().freeMemory() >> 10) + "kB" +
		" Total: " + ((Runtime.getRuntime().totalMemory() >> 10)) + "kB" +
		" Max: " + (Runtime.getRuntime().maxMemory() >> 10) + "kB" +
		" Threads: " + Thread.activeCount();
	}

	/**
	 * Returns the interval for reporting, in milliseconds.
	 * @return the report interval time in milliseconds.
	 */
	public long getReportInterval() {
		return reportInterval;
	}

	/**
	 * Sets the report interval.
	 * @param reportInterval the report interval to set, in milliseconds
	 */
	public void setReportInterval(long reportInterval) {
		this.reportInterval = reportInterval;
	}



}
