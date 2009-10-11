/*
 * JGF - Java Game Framework
 * $Id: BaseConsole.java 72 2008-11-25 20:54:28Z jjmontes $
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


package net.jgf.scene;


/**
 * <p>SceneManager manages a scene used by the game.</p>
 * <p>SceneManager is not designed to manage more than one scene at a time. If you 
 * need to manage multiple scenes, you are advised to create more than one
 * SceneManager.</p>
 * <p>A default scene manager is provided: ({@link SimpleSceneManager}).</p>
 * @author jjmontes
 * @see SimpleSceneManager
 */
public interface SceneManager {

	/**
	 * Updates the scene or scenes managed by this SceneManager.
	 * @param tpf time elapsed since last update
	 */
	public abstract void update(float tpf);
	
	/**
	 * Returns the scene managed by this SceneManager.
	 * @return the scene managed by this SceneManager
	 */
	public abstract Scene getScene();
	
	/**
	 * Sets the scene managed by this SceneManager.
	 * @param scene
	 */
	public abstract void setScene(Scene scene);
	
}