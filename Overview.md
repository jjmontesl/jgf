# JGF Overview #

### JGF Configuration ###

JGF applications are defined using a central configuration file. JGF reads this file and initializes a number of components (settings, console, logging, game engine, audio system, logic state machine, view state machine, entity system...).

When you want to add functionality to the game, you add components to the configuration file. For example, if you wish to add a screenshot capabilities to your game, you would add the following component that allows users to make a screenshot when pressing a key (note the name of the _class_ and the parameters _key_ and _path_):

```
<view id="view/root/level/screenshot" class="net.jgf.jme.view.ScreenshotView" 
    autoLoad="true" autoActivate="true" >
  <key>KEY_F12</key>
  <path>$APP_HOME/screenshots/screenshot-$ID.png</path>
</view>
```

In real life you would usually be copying the component you need from examples and modifying parameters to suit your needs.

### Component types ###

  * View States: define different views you will be using. This includes menu views, introduction screens, on screen display, loading screens. Views can be laid on top of other views.
  * Logic States: define different parts of your game behaviour. User needs to code these ones.
  * Entities: represent any entity your game uses. This includes players and enemies, bullets or spawn points... Entity Manager and Entity Groups are provided.
  * Other components: logging, audio, settings and other objects are also managed by JGF and can be declared at configuration time too.

Components are defined in the configuration file, and work together to make your game run. You can see the configuration file as a tree of components:

![http://jgf.googlecode.com/svn/trunk/jgf/doc/screenshots/mudvolley-configtree.png](http://jgf.googlecode.com/svn/trunk/jgf/doc/screenshots/mudvolley-configtree.png)

_Structure of the JGF configuration file (showing some arbitrary services and components)_

JGF provides many components of different nature. They allow you to sequence states, create scenes, load cameras, load entities, load models, transform models, add a skybox to a scene...

### System overview ###

![http://jgf.googlecode.com/svn/trunk/jgf/doc/design/diagrams/game-components.png](http://jgf.googlecode.com/svn/trunk/jgf/doc/design/diagrams/game-components.png)


[JGF Home](http://code.google.com/p/jgf) - [Features](Features.md) - [Overview](Overview.md) - [Getting Started](GettingStarted.md) - [Browse the Source](http://code.google.com/p/jgf/source/browse/#svn/trunk/jgf)