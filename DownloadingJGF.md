# Downloading JGF #



# JGF Base Project #

Main JGF package includes all that's needed to build games with JGF and JMonkeyEngine. The **base project** is a quick way to start development with JGF (visit the [building from source](BuildingFromSource.md) page if you are interested in building JGF from sources).

Go to the [Downloads](http://code.google.com/p/jgf/downloads/list) page and grab the latest jgf-base-project package.

Unzip it to your projects folder.

You will need to configure this project as a Java project in your Development Environment, so you can build it and run examples:

1) Define source and resource folders. In the base project, you should add the following folders as source and resource folders:

> /src
> /resources

2) At runtime, the resource directory should also be added to your classpath (Eclipse does this automatically when you define those folders as "source folders").

3) Include all libraries (.jar) in the "lib" directory. Javadoc is in the /doc directory (let your IDE know about it):

> /lib

4) At this time you should be able to build (compile) the project.

5) Because JGF currently uses JMonkeyEngine 2, at runtime some native libraries are needed (as with any other JME project). You will need to reference the native libraries in the appropriate **lib-native** directory. This can be done using the JVM option:

> -Djava.library.path=lib-native/windows

(Replace 'windows' with the appropriate folder (windows, linux or macos).

6) Try to run a JGF application running the main class:

> net.jgf.example.box.Box

There are more examples in the package net.jgf.example, test them all.

# Help / What now? #

Check the [Getting Started](GettingStarted.md) or the [Examples](Examples.md) page for more information.

If you need help, or just to let us know you succeeded, visit [JGF Users List](http://groups.google.com/group/jgf-users).


[JGF Home](http://code.google.com/p/jgf) - [Features](Features.md) - [Overview](Overview.md) - [Getting Started](GettingStarted.md) - [Browse the Source](http://code.google.com/p/jgf/source/browse/#svn/trunk/jgf)