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

/**
 * <p>
 * This serves as base class for objects of type {@link Component}. A high number
 * of JGF classes are components, including all view states, entities, logic
 * states, services and more. Components are identified by an id string.
 * </p>
 * <p>
 * This base class provides a XML config file reading capabilities, which will
 * set the component id (as defined in the XML node through the "id" attribute).
 * </p>
 * <p>
 * This class has been designed to be extended, although users often will prefer
 * to extend one of its children classes (depending on the type of component
 * that needs to be created). If you need to create a component (a View State,
 * Entity, Logic State or other), check the API first for an appropriate base
 * class as one probably exists.
 * </p>
 * <p>When extending BaseComponent or any of its derivated classes,
 * remember to call <tt>super.readConfig(...)</tt> at the beginning of your
 * readConfig() method to ensure that all attributes are processed.</p>
 * 
 * @see Component
 * @version 1.0
 * @author Jose Juan Montes
 */
@Configurable
public abstract class BaseComponent implements Component {

    /**
     * Component id.
     */
    private String id;
    
    /**
     * Builds a new Component with a null id.
     */
    public BaseComponent() {
        super();
    }

    /**
     * Builds a new Component with the given id.
     * @param id The component id.
     */
    public BaseComponent(String id) {
        super();
        this.id = id;
    }

    /**
     * Returns the component's id.
     * @return The component id.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the component's id.
     * @param id The component id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * <p>Configures this object from JGF XML configuration.</p>
     * <p>If you extend BaseComponent or any of its derivated classes,
     * remember to always call <tt>super.readConfig(...)</tt> to ensure
     * that common attributes are read from config.
     * </p>
     * 
     * @param config The JGF configuration object to read from.
     * @param configPath The XML XPath reference (in the config) to the object being configured.
     * @see Configurable
     */
    public void readConfig(Config config, String configPath) {

        this.id = config.getString(configPath + "/@id");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + id + "]";
    }

}