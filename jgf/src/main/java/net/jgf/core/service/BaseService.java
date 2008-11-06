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

package net.jgf.core.service;

import net.jgf.core.IllegalStateException;
import net.jgf.core.component.BaseComponent;

import org.apache.log4j.Logger;

/**
 * <p>Base {@link Service} class. Users creating their own services should extend this class or a
 * subclasses where appropriate.</p>
 *
 * @see Service
 * @author jjmontes
 */
public abstract class BaseService extends BaseComponent implements Service {

	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseService.class);

	/**
	 * Signals if the component is initialized currently.
	 */
	protected boolean initialized = false;

	/**
	 * Signals if the component has been disposed. When components are disposed they
	 * are no longer considered to be initialized, but Services are not
	 * reusable once they are disposed.
	 */
	protected  boolean disposed = false;

	/* (non-Javadoc)
	 * @see net.jgf.core.component.Component#initialize()
	 */
	@Override
	public void initialize() {
		if (disposed) {
			throw new IllegalStateException("Service " + this + " cannot be initialized because it has already been initialized and disposed.");
		} else if (initialized) {
			throw new IllegalStateException("Service " + this + " cannot be initialized because it is already initialized");
		} else {
			initialized = true;
		}
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.component.Component#initialize()
	 */
	@Override
	public void dispose() {
		initialized = false;
		disposed = true;
	}

}
