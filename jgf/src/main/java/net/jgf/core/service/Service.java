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

import net.jgf.core.component.Component;

/**
 * <p>A service provides a certain set of functionality to the rest of the
 * JGF components. See the hierarchy tree to inspect the descendants
 * of this class.</p>
 * <p>Services are normally defined in the configuration
 * file and loaded and initialized by the framework during
 * Application startup.</p>
 * <p>The Service interface (this class) provides a common set of methods
 * to manage a Service within a JGF Application. If you are creating a Service,
 * you probably want to use one of the base classes (see {@link BaseService}).</p>
 * <h3>Service lifecycle</h3>
 * <p>Services need to be added to the Application before it is started.</p>
 * <p>When the application is started, it initializes all services calling
 * their <tt>initialize</tt> method. Similarly, during shutdown all services
 * are disposed calling their <tt>dispose</tt> method.</p>
 * <p>During initialization, a service may typically resolve references and
 * prepare resources that will be needed during the service's life.</p>
 *
 * @see BaseService
 * @author jjmontes
 */
// TODO: Explain the service life cycle and include the diagram "service-lifecycle.dia"
public interface Service extends Component {

	/**
	 * Initializes the service. Users should not need to call this method directly.
	 */
	public void initialize();

	/**
	 * Cleans up service resources. Users should not need to call this method directly.
	 */
	public void dispose();

}
