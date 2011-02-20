/*
 * JGF - Java Game Framework
 * $Id: ConfigException.java 176 2009-11-01 02:45:50Z jjmontes $
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

package net.jgf.core.naming;

import net.jgf.core.JgfRuntimeException;

/**
 * <p>
 * JGF Naming Exception, related to naming errors in the object directory.
 * </p>
 * <p>This is a runtime exception, thus users are not required to catch
 * this exception.</p>
 * 
 * @author jjmontes
 * @version 1.0
 */
public class NamingException extends JgfRuntimeException {

    /**
     * The serialVersionUID of this Exception.
     */
    private static final long serialVersionUID = -5576724432318221426L;

    /**
     * Builds a new exception using the specified message.
     * 
     * @param message
     *            the message to be associated with this Exception.
     */
    public NamingException(String message) {
        super(message);
    }

    /**
     * Builds a new exception with the specified message and nested exception.
     * 
     * @param message
     *            the message to be associated with this Exception.
     * @param throwable
     *            a nested Throwable that is the cause of this exception.
     */
    public NamingException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
