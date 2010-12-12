/*
 * JGF - Java Game Framework
 * $Id: Engine.java 203 2010-12-10 03:06:18Z jjmontes $
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

package net.jgf.jme.engine;

import java.util.Date;


/**
 * <p>EngineStats holds information about the number of frames rendered.</p>
 * 
 * @author jjmontes
 */
public class EngineStats {

    protected long renderedFrames;
    
    protected long cappedFrames;
    
    protected Date engineStart;

    public long getRenderedFrames() {
        return renderedFrames;
    }

    public void setRenderedFrames(long renderedFrames) {
        this.renderedFrames = renderedFrames;
    }

    public long getCappedFrames() {
        return cappedFrames;
    }

    public void setCappedFrames(long cappedFrames) {
        this.cappedFrames = cappedFrames;
    }

    public Date getEngineStart() {
        return engineStart;
    }

    public void setEngineStart(Date engineStart) {
        this.engineStart = engineStart;
    }

    @Override
    public String toString() {
        double cappedRatio = (((double)this.getCappedFrames()) / ((double)this.getRenderedFrames()));
        String toString = "JMEEngine stats [renderedFrames=" + this.getRenderedFrames() + ",cappedFrames=" + this.getCappedFrames() + ",ratio=" + cappedRatio + "]";
        return toString;
    }
    
    
    
}

