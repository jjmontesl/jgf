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

package net.jgf.system;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.core.Globals;
import net.jgf.core.JULLoggingBridge;
import net.jgf.core.naming.Directory;
import net.jgf.core.service.Service;
import net.jgf.core.service.ServiceException;
import net.jgf.engine.Engine;
import net.jgf.settings.Settings;
import net.jgf.settings.SettingsManager;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.LevelRangeFilter;

import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

/**
 * <p>
 * This class represents the main Java Game Framework application, and that
 * handles the application startup.
 * </p>
 * <p>
 * Users are expected to create an instance of this class and just call its
 * start method.
 * </p>
 * <p>
 * <pre>
 * public static void main(String[] args) throws Exception {
 * 
 * 	    Application app = new Application(&quot;path/config.xml&quot;, args);
 * 	    app.start();
 * }
 * </pre>
 * </p>
 * <p>
 * This object requires a reference to the Engine service, which is started at
 * the end of the startup process. This class also holds the list of services
 * configured and the component directory.
 * </p>
 * <p>
 * Once the framework is started, this object can always be accessed through the
 * static method {@link net.jgf.system.Jgf#getApp()}.
 * </p>
 * 
 * @see Jgf
 * @author jjmontes
 */
public final class Application {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(Application.class);

    /**
     * Initial size of the services list.
     */
    private static final int DEFAULT_SERVICES_SIZE = 32;

    /**
     * Application key name (i.e. lowercase simple project name usable in filesystem paths)
     */
    private String key;
    
    /**
     * Application name.
     */
    private String name;

    /**
     * Application version, defaults to "1.0". This is not the same as the JGF
     * version.
     * 
     * @see Globals#JGF_VERSION
     */
    private String version = "1.0";

    /**
     * JGF running in debug mode.
     */
    private boolean debug;

    /**
     * JGF Components of the application.
     */
    private Map<String, Service> services;

    /**
     * Global directory of components and objects.
     */
    private Directory directory;

    /**
     * The command line arguments.
     */
    private String[] args;

    /**
     * The URL to the configuration file.
     */
    private String configUrl;

    /**
     * The Engine used.
     */
    private Engine engine;
    
    /**
     * Flat that defines if the application running in dedicated server process
     */
    private boolean dedicatedServer = false;

    /**
     * Reference to the Settings Manager used.
     */
    private Settings settings;
    
    /**
     * Creates a new Application object from the given configuration URL and
     * command line arguments.
     * @param configUrl the location of the JGF configuration file to read.
     * @param args the command line arguments. 
     */
    public Application(String configUrl, String[] args) {
        this.args = args;
        this.configUrl = configUrl;
        this.services = new LinkedHashMap<String, Service>(Application.DEFAULT_SERVICES_SIZE);
    }

    /**
     * Initializes the logging system.
     */
    private void initLogging() {

        // Init log4j

        // Using internal logging config (no log4j.xml file)
        LevelRangeFilter filter = new LevelRangeFilter();
        filter.setLevelMax(Level.FATAL);
        // TODO: Only way of overriding this will be log4j.xml? maybe --debug at
        // command line? later in the component
        filter.setLevelMin(Level.DEBUG);

        // Logging pattern
        // TODO: Accept a log pattern from environment variable
        String logPattern = Globals.LOG_PATTERN;

        // Logging to console
        Appender consoleAppender = new ConsoleAppender(new PatternLayout(logPattern));
        consoleAppender.addFilter(filter);
        BasicConfigurator.configure(consoleAppender);

        // Installing JUL to Log4j Bridge
        JULLoggingBridge.install();

        // Filter some verbose categories
        /*
         * Logger.getLogger("org.apache.commons.digester.Digester").setLevel(Level
         * .FATAL);
         * Logger.getLogger("org.apache.commons.digester.Digester.sax").
         * setLevel(Level.INFO);
         * Logger.getLogger("org.apache.commons.beanutils.MethodUtils"
         * ).setLevel(Level.INFO);
         * Logger.getLogger("org.apache.commons.beanutils.BeanUtils"
         * ).setLevel(Level.INFO);
         * Logger.getLogger("org.apache.commons.beanutils.ConvertUtils"
         * ).setLevel(Level.INFO);
         * Logger.getLogger("org.apache.commons.vfs.impl.StandardFileSystemManager"
         * ).setLevel(Level.INFO);
         */
    }

    /**
     * Initializes the resource locator.
     */
    private void initResourceLocator() {

        // Find the appropriate classloader
        Object dummy = new Object() { 
            public String toString() { return super.toString(); }
        };
        
        // Initialize the resource locator
        try {
            
            //URL baseUrl = dummy.getClass().getResource("/");
            URL baseUrl = Thread.currentThread().getContextClassLoader().getResource("data/");
            
            if (baseUrl == null) {
                logger.warn("Could not setup the resources directory");
            }
            
            ResourceLocatorTool.addResourceLocator("config", 
                    new SimpleResourceLocator(baseUrl));
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE,
                    new SimpleResourceLocator(baseUrl));
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_MODEL,
                    new SimpleResourceLocator(baseUrl));
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_AUDIO,
                    new SimpleResourceLocator(baseUrl));

        } catch (URISyntaxException e) {
            throw new ServiceException("Could not set resource locator for properties.", e);
        }
    }

    /**
     * Starts a JGF application reading configuration from the configuration
     * file.
     */
    private void bootApplication() {

        // Initialize JGF
        Jgf.app = this;

        // Init logging
        initLogging();

        // Init resource management
        logger.debug("Initializing resource locator tool");
        initResourceLocator();

        // Init directory
        logger.debug("Initializing naming");
        directory = new Directory();

        // Configure
        logger.debug("Processing configuration");
        logger.info("Reading configuration file " + configUrl);

        Config config = new Config(configUrl);
        readConfig(config);

        logger.info(this.name + " " + this.version + " starting (" + Globals.JGF_TITLE + " "
                + Globals.JGF_VERSION + ")");

        if (this.isDebug()) {
            logger.info("JGF Framework is configured in DEBUG mode");
        }

        // Initialize components
        logger.debug("Initializing services");
        for (Service service : services.values()) {
            // TODO: When to initialize? Dependencies...?
            logger.debug("Initializing service " + service);
            service.initialize();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finalize() {
        this.dispose();
    }

    /**
     * <p>
     * Prepares the application to be closed, triggering shutdown, which calls
     * "dispose" on all services.
     * </p>
     */
    public void dispose() {
        // Initialize components
        logger.info("Finalizing JGF application");
        for (Service service : services.values()) {
            // TODO: When to dispose? Dependencies...?
            logger.debug("Disposing service " + service);
            service.dispose();
        }
        logger.debug("Directory retrievals: " + directory.getRetrievalCount() + " Peak size: "
                + directory.getPeakSize());
    }

    /**
     * <p>Starts the application.</p>
     * <p>
     * This is the method that users are expected to call in order to initialize
     * the game.
     * </p>
     */
    public void start() {

        // Init framework
        bootApplication();

        // Start
        logger.info("Starting engine");
        if (engine == null) {
            throw new ConfigException("No engine defined for the JGF application");
        }
        engine.start();

        logger.debug("Directory contains " + directory);

        logger.info("Bootup process in thread " + Thread.currentThread() + " finished");

    }

    /**
     * Configures this object from the configuration.
     * 
     * @param config the configuration object to be processed.
     * @see Configurable
     */
    public void readConfig(Config config) {

        this.setName(config.getString("application/name"));
        this.setKey(config.getString("application/key"));
        this.setVersion(config.getString("application/version", this.version));
        this.setDebug(config.getBoolean("application/debug", false));
        this.setDedicatedServer(config.getBoolean("application/dedicatedServer", false));

        if (config.containsKey("application/settings/@ref")) {
            Jgf.getDirectory().register(this, "settings", config.getString("application/settings/@ref"));
        }
        Jgf.getDirectory().register(this, "engine", config.getString("application/engine/@ref"));

        // Build and register services
        // Firstly they are added to the directory
        List<String> elementIds = config.getList("service" + "/@id");
        for (String elementId : elementIds) {
            Service element = ConfigurableFactory.newFromConfig(config, "service" + "[@id='" + elementId
                    + "']", Service.class);
            this.addService(element);
        }

    }

    /**
     * Retrieves and returns the Engine component using the config element
     * "application/engine[@ref]".
     * 
     * @return the Engine component.
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Returns this application name.
     * @return the application name (usually defined in the configuration file).
     */
    public String getName() {
        return name;
    }

    /**
     * Indicates whether JGF is running in debug mode.
     * @return true if the framework runs in debug mode.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * <p>
     * Returns the application version (which is not the same as the JGF
     * version).
     * </p>
     * <p>
     * Application version defaults to 1.0.
     * </p>
     * 
     * @see Globals#JGF_VERSION
     * @return the application version (usually defined in the configuration file).
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>
     * Sets the application version (which is not the same as the JGF version).
     * </p>
     * <p>
     * Application version defaults to 1.0.
     * </p>
     * 
     * @see Globals#JGF_VERSION
     * @param version the application version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Sets the application name.
     * @param name the application name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Sets the debug mode for JGF. This is only read at initialization time by some services,
     * so it needs to be configured in JGF configuration file. Behaviour is undefined if
     * this is changed after.</p>
     * @param debug flag that indicates if JGF runs in debug mode.
     */
    public void setDebug(boolean debug) {
        this.debug = true;
    }

    /**
     * Adds a service to the application services.
     * 
     * @see net.jgf.core.naming.Directory#addService(net.jgf.core.service.Service)
     * @param service the framework service to add.
     */
    public void addService(Service service) {
        services.put(service.getId(), service);
        directory.addObject(service.getId(), service);
    }

    /**
     * <p>
     * Returns the naming directory used by this application (note that it is
     * easier to access this through the {@link Jgf#getDirectory()}) static
     * method.
     * </p>
     * 
     * @see Directory
     * @see Jgf#getDirectory()
     * @return the naming directory.
     */
    public Directory getDirectory() {
        return directory;
    }

    /**
     * Returns the command line arguments that were passed to this application.
     * 
     * @see CommandLineArgumentsService
     * @return an array of strings containing the program command line arguments.
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Sets the reference to the JGF Engine to use (i.e. JMonkeyEngine binding).
     * @param engine
     *            the engine to set
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public boolean isDedicatedServer() {
        return dedicatedServer;
    }

    public void setDedicatedServer(boolean dedicatedServer) {
        if ((this.dedicatedServer == true) && (dedicatedServer == false)) {
            throw new ConfigException("Cannot change dedicated server setting from true to false.");
        }
        this.dedicatedServer = dedicatedServer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }


}
