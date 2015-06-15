# Building JGF from source #



# Get JGF source #

JGF latest sources can be retrieved at JGF source repository (SVN) at:

[http://code.google.com/p/jgf/source/checkout](http://code.google.com/p/jgf/source/checkout)

Instead of using JGF source, you can use the [JGF Base Project](DownloadingJGF.md) which contains everything that's needed to use JGF.

JGF sources for latest releases can be also be downloaded from the [downloads](http://code.google.com/p/jgf/downloads/list) page.

# Using the included Ant script #

JGF source includes an Ant script that can be used to build JGF at /src/build/build-jgf.xml. From JGF directory, it can be ran using:

> `ant -buildfile src/build/build-jgf.xml clean all`

# Configuring your IDE for JGF source #

You will need to configure this project as a Java project in your Development Environment, so you can build it and run examples:

1) Define source AND resource folders. JGF code and resources are distributed along a few folders. Make sure you "include as source folders" all the "java" and "resources" folders:

> /src/example/java
> /src/example/resources
> /src/jgf-jme/java
> /src/jgf-jmephysics/java
> /src/main/java

2) At runtime, all those directories should also be added to your classpath (Eclipse does this automatically when you define those folders as "source folders").

3) Include all libraries (.jar) in the "lib" directories:

> /src/jgf-jme/lib
> /src/main/lib

4) At this time you should be able to build (compile) the project.

5) Because JGF currently uses JMonkeyEngine 2, at runtime some native libraries are needed (as with any other JME project). You will need to reference the native libraries. If you have any doubt about this, you should ensure you can run JMonkeyEngine applications and tests (again, check [JME2 Tutorials Page](http://www.jmonkeyengine.com/wiki/doku.php?id=the_tutorials_-_jme_2) if you are not able to run JME applications). You should then be able to run the following JGF example:

> net.jgf.example.box.Box

6) There are more examples in the package net.jgf.example, test them all.

7) Visit [JGF Users List](http://groups.google.com/group/jgf-users) or [JGF at JME Forums](http://www.jmonkeyengine.com/forum/index.php?topic=9495.30) and let us know you succeeded ;-).

_(Eclipse project files are included along with source code, but you may need to correct some of the project library dependencies and project dependencies (on JME) to suit your environment)_

# What now? #

Check the [Getting Started](GettingStarted.md) or the [Examples](Examples.md) page for more information.

Visit [JGF Users List](http://groups.google.com/group/jgf-users) or [JGF at JME Forums](http://www.jmonkeyengine.com/forum/index.php?topic=9495.30) and let us know you succeeded ;-).



[JGF Home](http://code.google.com/p/jgf) - [Features](Features.md) - [Overview](Overview.md) - [Getting Started](GettingStarted.md) - [Browse the Source](http://code.google.com/p/jgf/source/browse/#svn/trunk/jgf)