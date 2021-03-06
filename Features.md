# JGF Features #

**Core**

  * Logic and View states drive application logic and aspect.
  * Entities, Scene and other structures are managed by framework services.
  * Configuration is done in a descriptive XML configuration file.
  * Component injection (via annotations or API).
  * Easy overview of application parts.

**Render Engine**

  * JGF currently uses JME 2 (jMonkeyEngine) as 3D engine.

**GUI System**

  * A GUI system (NiftyGUI) is included out of the box.

**Settings System**

  * Settings management.
  * Settings storage.

  * Configuration Dialog for initial settings (_planned: currently using JME settings dialog_).
  * Command Line Options parsing (_planned_).

**Console System**

  * Extensible and replaceable console system. Functionality, aspect and syntax can be customized.
  * Basic graphical interface.

> ![http://jgf.googlecode.com/svn/trunk/jgf/doc/screenshots/sample-console-1.png](http://jgf.googlecode.com/svn/trunk/jgf/doc/screenshots/sample-console-1.png)

**Extensibility**

JGF is designed to be extensible. Base classes or complete implementations are provided for most of the components needed to make a game. You can always provide new functionality writing your custom components if you need.

**Development**

JGF includes and provides components that easily allow you to add development tools to your game:

  * JME Scene Debugger (bounding volumes viewing, normals viewing, wireframe mode...).
  * Physics Debugger.
  * Scene Monitor / Scene Worker (for scene graph inspecting).
  * Unified logging.

> ![http://jgf.googlecode.com/svn/trunk/jgf/doc/screenshots/sample-debug-1.png](http://jgf.googlecode.com/svn/trunk/jgf/doc/screenshots/sample-debug-1.png)

> ![http://jgf.googlecode.com/svn/trunk/jgf/doc/screenshots/sample-scenemonitor-1.png](http://jgf.googlecode.com/svn/trunk/jgf/doc/screenshots/sample-scenemonitor-1.png)

**Loaders and Art Pipeline**

  * Modular loading system for Entities, Scenes and Models.
  * Transparent pooling system.

**Audio**

  * A simple, easy to use sound effects system is provided out of the box.

**Build System**

  * Out of the box build scripts to package games, installers and Java Web Start.


[JGF Home](http://code.google.com/p/jgf) - [Features](Features.md) - [Overview](Overview.md) - [Getting Started](GettingStarted.md) - [Browse the Source](http://code.google.com/p/jgf/source/browse/#svn/trunk/jgf) - [FAQ](FAQ.md)