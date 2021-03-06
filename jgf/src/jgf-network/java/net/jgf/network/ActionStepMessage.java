/*
 * JGF - Java Game Framework
 * $Id: Fader.java 35 2008-11-09 04:40:40Z jjmontes $
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


package net.jgf.network;

import net.jgf.logic.action.control.ActionStep;
import net.jgf.logic.action.control.ActionStepType;
import net.jgf.logic.action.control.ControllerAction;

import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializable;

/**
 * 
 */
@Serializable(id = 9)
public class ActionStepMessage extends Message {
    
    public String stepType;
    
    public String stepRef;
    
    public ActionStepMessage() {
        
    }
    
    public ActionStepMessage(ActionStep step) {
        stepType = step.getType().name();
        stepRef = step.getRef();
    }
    
    public void perform(Object object) {
        ActionStep step = new ActionStep(ActionStepType.valueOf(stepType), stepRef);
        ControllerAction controller = new ControllerAction();
        controller.getSteps().add(step);
        controller.perform(object);
    }
    
}

