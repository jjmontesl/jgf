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

package net.jgf.engine;

import net.jgf.logic.LogicManager;
import net.jgf.view.ViewManager;


/**
 * <p>The Engine interface defines a service that runs the main game loop. It is responsible
 * for updating the LogicManager and the ViewManager services. An Engine must spawn a thread
 * to run the main game loop.</p>
 * <p>Currently, only a single engine implementation is provided ({@link JMEEngine}). Most users
 * will not need to extend or modify the engine subsystem.</p>
 * <p>Note: Engines need to honour the 'dedicatedServer' setting and avoid creating graphical windows
 * when running in that mode. At least, an UnsupportedOperationException should be thrown
 * if dedicatedServer is set for an Engine that doesn't support it.</p>
 * <p>See {@link JMEEngine} for an example of an implementation.</p>
 * @see BaseEngine
 * @see JMEEngine
 * @author jjmontes
 */
public interface Engine {

	/**
	 * Starts the main game loop, and thus the game.
	 */
	public void start();

	/**
	 * Returns a reference to the LogicManager.
	 */
	public LogicManager getLogicManager();

	/**
	 * Returns a reference to the ViewManager.
	 */
	public ViewManager getViewManager();

}

