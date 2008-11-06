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

import java.util.List;

import net.jgf.core.UnsupportedOperationException;
import net.jgf.core.service.Service;

/**
 * <p>The console component holds the console buffer and the input history.</p>
 * <p>It is line-based, and therefore it does not accept input as a
 * stream (one key at once), and it doesn't hold the current input line.</p>
 * <p>This interface is not related to the graphical aspect of the console.</p>
 * <p>Subclasses may implement different console services, like autocomplete,
 * different interpreters, etc...</p>
 * <p>Applications should avoid calling issuing
 * commands directly (as the actual console
 * language is not specified by this interface).</p>

 * @author jjmontes
 */
public interface Console extends Service {

	/**
	 * Adds a line to the output buffer.
	 */
	public void addLine(String line);

	/**
	 * Retrieves the last N lines from the output buffer.
	 */
	public List<String> getLastLines(int lines);

	/**
	 * Retrieves N lines from the output buffer, starting at
	 * the specified offset (offset starts at the buffer end)
	 */
	public List<String> getLastLines(int lines, int offset);

	/**
	 * Gets the line N from command history (offset starts at buffer end).
	 * @param offset
	 * @return the history line, null if the line doesn't exist
	 */
	public String getHistory(int offset);

	/**
	 * Processes a command.
	 */
	public void processCommand (String string);

	/**
	 * Adds a ConsoleObserver to the StandardGameEngine.
	 * @param listener listener to add
	 */
	public void addConsoleObserver(ConsoleObserver listener);

	/**
	 * Returns a list of candidate expressions that follow the given one.
	 * Implementing classes can throw a #{@link net.jgf.core.UnsupportedOperationException}
	 * if they don't support this functionality.
	 * @param expression expression to autocomplete
	 * @return a list of possible choices that complete the given expression
	 * @throws UnsupportedOperationException if the console implementation doesn't support autocomplete
	 */
	public List<String> complete (String expression) throws UnsupportedOperationException;

}
