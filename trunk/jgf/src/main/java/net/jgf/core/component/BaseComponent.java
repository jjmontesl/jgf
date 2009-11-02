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

package net.jgf.core.component;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.jme.view.ActionInputView.ActionInputKey;
import net.jgf.settings.Settings;
import net.jgf.system.Jgf;

/**
 * <p>
 * This is the base class for objects of type {@link Component}.
 * </p>
 * 
 * @see Component
 * @author jjmontes
 */
public class BaseComponent implements Component {

    /**
     * Component id.
     */
    protected String id;

    /**
     * Builds a new Component with a null id.
     */
    public BaseComponent() {
        super();
    }

    /**
     * Builds a new Component with the given id.
     */
    public BaseComponent(String id) {
        super();
        this.id = id;
    }

    /**
     * Returns the component's id.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the component's id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Configures this object from Config.
     * 
     * @see Configurable
     */
    public void readConfig(Config config, String configPath) {

        this.id = config.getString(configPath + "/@id");

        // Al components provide settings injection defined at XML
        if (config.containsKey(configPath + "/settings/@ref")) {
            String settingsRef = config.getString(configPath + "/settings/@ref");
            Settings settings = Jgf.getDirectory().getObjectAs(settingsRef, Settings.class);

            int index = 1;
            while (config.containsKey(configPath + "/settings/setting[" + index + "]/@field")) {
                String field = config.getString(configPath + "/settings/setting[" + index
                        + "]/@field");
                String ref = config.getString(configPath + "/settings/setting[" + index + "]/@ref");
                settings.register(this, field, ref);

                index++;
            }

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + id + "]";
    }

}