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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * @todo Review / test and document (untested: comes from other project)
 */
//TODO: Review / test and document (untested: comes from other project)
public class TextConsoleThread extends Thread {

    /**
     * Class logger
     */
    private static final Logger logger = Logger.getLogger(TextConsoleThread.class);

    /**
     * The console implementation that is served by this wrapper.
     */
    private Console console;

    /**
     * Constructor
     * 
     * @param console
     */
    public TextConsoleThread(Console console) {
        super("TextConsoleThread");
        this.console = console;
    }

    /**
     * Text Mode
     */
    @Override
    public void run() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(java.lang.System.in));

        String prompt = null;

        try {
            // TODO: Use a proper exit condition
            while (true) {
                prompt = reader.readLine();
                // TODO: Maybe use the GL-Thread to process the Command? Option?
                // Leave that up for particular commands? Is the underlying
                // console thread safe?
                console.processCommand(prompt);
            }
        } catch (IOException e) {
            logger.error("IOException while reading the console. Text console dying.", e);
        }

    }

}
