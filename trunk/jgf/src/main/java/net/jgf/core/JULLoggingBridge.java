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

package net.jgf.core;

import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import org.apache.log4j.Logger;

/**
 * <p>
 * JUL bridge/router for log4j. Since jMonkeyEngine uses JUL (Java Unified
 * Logging), and JGF uses log4j, this bridge is installed so logging statements
 * sent to JUL are handled by JGF logging system.
 * </p>
 * 
 * @author Christian Stein
 * @author jjmontes
 */
public class JULLoggingBridge extends Handler {

    protected final boolean classname;

    protected final boolean format;

    /**
     * Resets the entire JUL logging system and adds a single JulLoggingBridge
     * handler. instance to the root logger.
     */
    public static void install() {
        install(new JULLoggingBridge(true, true));
    }

    /**
     * Resets the entire JUL logging system and adds the JULLoggingBridge
     * instance to the root logger.
     */
    public static void install(JULLoggingBridge handler) {
        LogManager.getLogManager().reset();
        LogManager.getLogManager().getLogger("").addHandler(handler);
    }

    /**
     * Rereads the JUL configuration.
     */
    public static void uninstall() throws Exception {
        LogManager.getLogManager().readConfiguration();
    }

    /**
     * Initialize this handler.
     * 
     * @param classname
     *            Use the source class name provided by the LogRecord to get the
     *            log4j Logger name. If <code>false</code>, the raw name of the
     *            JUL logger is used.
     * @param format
     *            If <code>true</code>, use the attached formatter if available.
     *            If <code>false</code> the formatter is ignored.
     */
    public JULLoggingBridge(boolean classname, boolean format) {
        this.classname = classname;
        this.format = format;
    }

    /**
     * No-op implementation.
     */
    @Override
    public void close() {
        // Empty
    }

    /**
     * No-op implementation.
     */
    @Override
    public void flush() {
        // Empty
    }

    /**
     * Return the Logger instance that will be used for logging.
     */
    protected Logger getPublisher(LogRecord record) {
        String name = null;
        if (classname) {
            name = record.getSourceClassName();
        } else {
            name = record.getLoggerName();
            if (name == null) {
                name = JULLoggingBridge.class.getName();
            }
        }
        return Logger.getLogger(name);
    }

    /**
     * Returns {@code Level.ALL} as this cares about discarding log statements.
     */
    @Override
    public final synchronized Level getLevel() {
        return Level.ALL;
    }

    /**
     * <p>
     * Publish a LogRecord.
     * </p>
     * <p>
     * The logging request was made initially to a Logger object, which
     * initialized the LogRecord and forwarded it here.
     * </p>
     * <p>
     * This handler ignores the Level attached to the LogRecord, as this cares
     * about discarding log statements.
     * </p>
     * 
     * @param record
     *            Description of the log event. A null record is silently
     *            ignored and is not published.
     */
    @Override
    public void publish(LogRecord record) {
        /*
         * Silently ignore null records.
         */
        if (record == null) {
            return;
        }
        /*
         * Get our logger for publishing the record.
         */
        Logger publisher = getPublisher(record);
        Throwable thrown = record.getThrown(); // can be null!
        String message = record.getMessage(); // can be null!
        if (format && getFormatter() != null) {
            try {
                message = getFormatter().format(record);
            } catch (Exception ex) {
                reportError(null, ex, ErrorManager.FORMAT_FAILURE);
                return;
            }
        }
        if (message == null) {
            return;
        }
        /*
         * TRACE
         */
        if (record.getLevel().intValue() <= Level.FINEST.intValue()) {
            publisher.trace(message, thrown);
            return;
        }
        /*
         * DEBUG
         */
        if (record.getLevel() == Level.FINER) {
            publisher.debug(message, thrown);
            return;
        }
        if (record.getLevel() == Level.FINE) {
            publisher.debug(message, thrown);
            return;
        }
        /*
         * INFO
         */
        if (record.getLevel() == Level.CONFIG) {
            publisher.info(message, thrown);
            return;
        }
        if (record.getLevel() == Level.INFO) {
            publisher.info(message, thrown);
            return;
        }
        /*
         * WARN
         */
        if (record.getLevel() == Level.WARNING) {
            publisher.warn(message);
            return;
        }
        /*
         * ERROR
         */
        if (record.getLevel().intValue() >= Level.SEVERE.intValue()) {
            publisher.error(message, thrown);
            return;
        }

        /*
         * Still here? Fallback and out.
         */
        publishFallback(record, publisher);
    }

    /**
     * <p>
     * Called by publish if no level value matched.
     * </p>
     * <p>
     * This implementation uses log4j DEBUG level.
     * </p>
     * 
     * @param record
     *            to publish
     * @param publisher
     *            who logs out
     */
    protected void publishFallback(LogRecord record, Logger publisher) {
        publisher.debug(record.getMessage(), record.getThrown());
    }

}
