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

import org.apache.log4j.Logger;

/**
 * <p>This class is a singleton used to globalize the main application objects.
 * It holds references to objects but does not create any of them.</p>
 * <p>The most common method of this class is {@link System#getDirectory()},
 * which provides access to the directory that holds references to the
 * framework components that you will be using.</p>
 */
public class System {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Application.class);


	/**
	 * Reference to the {@link Application}.
	 */
	static Application app = null;

	/**
	 * Private constructor to avoid instantiation.
	 */
	private System() {
		// Constructor should do nothing
	}

	/**
	 * Returns the Application object.
	 * @return the app
	 */
	public static Application getApp() {
		return app;
	}

	/**
	 * Returns the JGF Directory of the application. The Directory provides access to the
	 * framework components and services.
	 * @return the Directory.
	 * @see Directory
	 */
	public static Directory getDirectory() {
		return app.getDirectory();
	}

}
