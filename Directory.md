# The Directory #

JGF components need to talk to each other. Because JGF is a loosely coupled system, components need to be provided with references to the other components they collaborate with.

Because JGF often references components by their "id", the Directory exists to keep track of components.

Note that the Directory is not the only way to reach objects. You can get references to objects through other objects (like in `viewNode.getChild(...)` ) as usual.

### Adding and removing objects to the directory ###

### Finding objects through the directory ###



[JGF Home](http://code.google.com/p/jgf) - [Features](Features.md) - [Overview](Overview.md) - [Getting Started](GettingStarted.md) - [Browse the Source](http://code.google.com/p/jgf/source/browse/#svn/trunk/jgf)