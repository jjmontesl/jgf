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

package net.jgf.console;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.jgf.core.service.BaseService;

import org.apache.log4j.Logger;

/**
 * <p>This is a default implementation of #{@link net.jgf.console.Console}
 * that takes care of the line buffer and of Console observers. Derived classes must
 * implement the command processing and functionality.</p>
 *
 * @author jjmontes
 * @version $Revision$
 */
// TODO: The buffer should be part of a DefaultConsoleObserver
public abstract class BaseConsole extends BaseService implements Console {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(BaseConsole.class);

	/**
	 * Initial listener list size.
	 */
	protected static final int INITIAL_OBSERVER_SIZE = 4;
	/**
	 * The console buffer.
	 */
	protected LinkedList<String> buffer;
	/**
	 * The list of StandardGameEngine Listeners.
	 */
	protected ArrayList<ConsoleObserver> observers;
	/**
	 * Command history.
	 */
	protected LinkedList<String> history;

	/**
	 * Class constructor.
	 */
	public BaseConsole() {
		super();
		buffer = new LinkedList<String>();
		history = new LinkedList<String>();
		observers = new ArrayList<ConsoleObserver>(INITIAL_OBSERVER_SIZE);
	}

	/* (non-Javadoc)
	 * @see net.jgf.console.Console#addLine(java.lang.String)
	 */
	@Override
	public void addLine(String line) {

		// Add the line to the buffer
		buffer.add(line);

		// TODO: Considerate where this should be logged
		logger.info(line);

		// Notify observers
		for (ConsoleObserver listener : observers) {
			listener.lineAdded(this, line);
		}

	}

	/* (non-Javadoc)
	 * @see net.jgf.console.Console#getLastLines(int)
	 */
	@Override
	public List<String> getLastLines(int lines) {
		return getLastLines(lines, 0);
	}

	/* (non-Javadoc)
	 * @see net.jgf.console.Console#getLastLines(int, int)
	 */
	@Override
	public List<String> getLastLines(int lines, int offset) {
		if (lines > buffer.size()) lines = buffer.size();
		return buffer.subList(buffer.size() - lines - offset, buffer.size() - offset);
	}

	/* (non-Javadoc)
	 * @see net.jgf.console.Console#addObserver(net.jgf.console.ConsoleObserver)
	 */
	@Override
	public void addConsoleObserver(ConsoleObserver listener) {
		this.observers.add(listener);
	}

	/* (non-Javadoc)
	 * @see net.jgf.console.Console#getHistory(int)
	 */
	@Override
	public String getHistory(int offset) {
		return buffer.get(buffer.size() - offset);
	}

	/* (non-Javadoc)
	 * @see net.jgf.console.Console#processCommand(java.lang.String)
	 */
	@Override
	public void processCommand(String string) {
		// TODO: This addline doesn't fit well here... should be optional or solved better
		addLine("> " + string);
		history.add(string);
	}

}