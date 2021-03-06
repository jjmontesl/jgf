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

package net.jgf.system;

import net.jgf.core.naming.Directory;
import net.jgf.settings.Settings;

import org.apache.log4j.Logger;

/**
 * <p>
 * This class is a singleton used to globalize the main application objects. It
 * holds references to framework core objects.
 * </p>
 * <p>
 * The most used method of this class is {@link Jgf#getDirectory()}, which
 * provides access to the directory that holds references to the framework
 * components that applications will be using.
 * </p>
 * 
 * @version 1.0
 * @author jjmontes
 */
public final class Jgf {

    /**
     * Class logger.
     */
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(Application.class);

    /**
     * Reference to the JGF {@link Application}.
     */
    static Application app;

    /**
     * Private constructor in order to avoid instantiation.
     */
    private Jgf() {
        // Constructor should do nothing
    }

    /**
     * Returns the JGF Application object.
     * 
     * @return the application.
     * @see Application
     */
    public static Application getApp() {
        return app;
    }

    /**
     * <p>
     * Returns the JGF Directory of the application. The Directory provides
     * access to the framework components and services.
     * </p>
     * 
     * @return the directory.
     * @see Directory
     */
    public static Directory getDirectory() {
        return app.getDirectory();
    }
    
    /**
     * <p>Returns the Settings component for the application.
     * Settings are values that can change over application life.</p>
     *
     * @return the Settings reference.
     * @see Settings
     */
    public static Settings getSettings() {
        return app.getSettings();
    }

}
