# JGF Frequently Asked Questions #



## General ##

### What is the current state of development? ###

JGF is alpha code. Changes to the API are to be expected  and  several features are poorly implemented or missing.

### Is jMonkeyEngine the only game engine usable with JGF ###

Currently it is. While writing implementations for other engines is _in theory_ possible, this is not a trivial task. Currently jMonkeyEngine 2 (JME2) is the only engine supported by JGF. Also, note that JGF itself doesn't require a 3D engine, and tries to be extensible to support 2D engines as well.

## Logging ##

### JME spatial classes like Node keep logging too much ###

The LoggigConfigService can filter particular loggers. Adding the following configuration at the beginning of a JGF configuration file will filter some common verbose loggers:

```
<!-- 
Logging configuration
 -->
<service id="logging" class="net.jgf.util.system.LoggingConfigService">
	<logger name="com.jme.scene.Node" level="WARN" />
	<logger name="com.jme.scene.Point" level="WARN" />
	<logger name="com.jme.scene.Line" level="WARN" />
	<logger name="net.jgf.loader.FileChainLoader" level="WARN" />				
</service>
```


---


[JGF Home](http://code.google.com/p/jgf) - [Features](Features.md) - [Overview](Overview.md) - [Getting Started](GettingStarted.md) - [Browse the Source](http://code.google.com/p/jgf/source/browse/#svn/trunk/jgf) - [FAQ](FAQ.md)