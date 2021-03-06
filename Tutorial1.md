# JGF Tutorial 1: First Application with JGF #



## Setting up ##

In this tutorial we will build the a simple volleyball game, which will evolve during following tutorials.

![http://jgf.googlecode.com/svn/trunk/jgf/doc/screenshots/mudvolley-ingame.png](http://jgf.googlecode.com/svn/trunk/jgf/doc/screenshots/mudvolley-ingame.png)

The game will feature two sphere-like players and a spherical ball. Players will be able to move left, right and jump.

Firstly, check the [JGF Base Project](DownloadingJGF.md) page, and grab the latest `jgf-project-skel` package. Ensure you are able to run JGF samples as explained there.

You can run the result of this tutorial running the JGF class `net.jgf.example.mudvolley1`.

## Introduction ##

JGF games are defined using a central configuration file where different aspects of the game are described.

Let's create the main entry point for the application. In JGF, applications are started calling `start()` on an Application object, which receives the path to the configuration resource within the 'data' dir:

```
public class MudVolley1 {

	/**
	 * Entry point to the application. JGF applications should just create
	 * the Application object and call .start() on it.
	 * @see Application
	 */

	public static void main(String[] args) throws Exception {

		Application app = new Application("mudvolley/mudvolley1.xml", args);
		app.start();

	}
}
```

## Configuration ##

We will discuss the [mudvolley1.xml](http://code.google.com/p/jgf/source/browse/trunk/jgf/src/example/resources/data/mudvolley/mudvolley1.xml) file.

The configuration file is an XML file containing with a root 'jgf' node. Inside, different JGF services are defined. JGF reads this file and creates all services and components.

Feel free to remove comments if you feel they are unnecessary in order to make the configuration file easier to read for you.

### Application section ###

Every JGF game requires an application section containing some information about the application. These entries are mandatory:

```
	<application>
	
		<!-- General application information -->
		<key>mudvolley</key>
		<name>MudVolley JGF Example 1: Introducing JGF</name>
		<company>JGF</company>
		<version>1.0</version>
		<networkProtocolVersion>1.0</networkProtocolVersion>
		

		<!-- In debug mode more verbose logging is produced, and some extra checks
			 are performed during runtime. This is a boolean parameter (true|false). -->
		<debug>true</debug>
		
		<!-- Application description: to be used in short listings and command line help -->
		<description>
			MudVolley is a Slime Volley clone that shows some of
			Java Game Framework (JGF) features.
		</description>
		
		<!-- A reference to the engine service being used. Currently, only JMonkeyEngine is
		supported. This service is defined below -->
		<engine ref="engine" />
		
	</application>
```


### Services ###

The rest of the configuration file consists of services and components. JGF components must have an `id` attribute that identifies them uniquely. Components use these ids to obtain references to other components.

Each component also requires a `class` attribute that defines the name of the implementation class of a service or component.

### Engine section ###

The engine service initializes the game engine to use. Currently, the only supported game engine is JMonkeyEngine, and this service is implemented by the net.jgf.jme.engine.JMEEngine class.

Note you need to provide some parameters to this service (see comments).

```

	<service id="engine" class="net.jgf.jme.engine.JMEEngine">

		<!-- 'collectStats' parameter causes JMEEngine to collect engine statistics
		like FPSs, objects drawn, number of textures... Here,
		we use variable interpolation to apply the same value for 'debug', defined above.  -->
		<collectStats>${application/debug}</collectStats>
		
		<!--  Reference to the LogicManager service, defined below -->
		<logicManager ref="logic" />
		
		<!--  Reference to the ViewManager service, defined below -->
		<viewManager ref="view" />
		
	</service>
```

We also provide references to other components ("logic" and "view") which are defined later.

### Scene section ###

Scene rendering relies on a service called the Scene Manager. It contains a Scene object.

A JGF JME scene contains the root element of the scene graph hierarchy. It can also contain cameras, skyboxes or other information relative to the scene being rendered.

Normally, scenes can be loaded by loaders, but in this simple example the scene is also defined in the configuration file, as well as a camera for the scene.

```
	<service id="scene/manager" class="net.jgf.scene.SimpleSceneManager" depends="engine">
			
			<!-- DefaultJmeScene is a jMonkeyEngine scene which includes camera
			management, an optional skybox and some tooling -->
			<scene id="scene" class="net.jgf.jme.scene.DefaultJmeScene">
			
			    <!-- It is possible to define cameras inside DefaultJmeScene configuration,
				    using the "cameras" element below.
				    This is not the only place to define cameras. Games load
				    camera information from data or create them dynamically. -->
				<cameras>
					<camera id="scene/cameras/overview" class="net.jgf.jme.camera.StaticCamera" >
						<target>0.0 4.0 0.0</target>
						<location>0.0 4.0 20.0</location>
					</camera>
				</cameras>

                <!-- This "camera" element defines the Scene "current" camera. 
                    Here we reference the fixed camera defined above. -->
				<camera ref="scene/cameras/overview" />
			
			</scene>
			
	</service>
```


### Entity Management ###

The Entity Manager is a simple service that contains a tree of game entities.

You can use entities to manage every object or player that needs to be updated every logic step. Commonly, entities are arranged in EntityGroups for easier management.

```

	<service id="entity" class="net.jgf.entity.DefaultEntityManager">
		
		<!-- In this mudvolley example, an empty EntityGroup is created. Entities
        (players and ball) will be added to this group later. As this is a very simple
        game, we put all entities in the same container group. -->
		<entity id="entity/root" class="net.jgf.entity.EntityGroup" >
				
		</entity>
			
	</service> 
```

### Loading system ###

The Loading System provides loading capabilities for scenes, entities and models.
The loading system defines a number of loaders. The game code uses these loaders to load scenes, entities, models...

JGF provides some general-purpose loaders, but in this example we use custom loaders that create the necessary geometry for the mudvolley game players and field. For simplicity we use boxes and spheres for the field and players in this first tutorial .

_(Note that using JGF loader system is entirely optional. You can use your own preferred way of loading assets, or combine JGF and custom loaders.)_

We define three custom loaders (for the scene and entities):

```
	<service id="loader" class="net.jgf.loader.DefaultLoaderManager">
				
		<loader id="loader/scene" class="net.jgf.example.mudvolley1.loader.MudVolleySceneLoader">
		</loader>

		<loader id="loader/entity/player" class="net.jgf.example.mudvolley1.loader.MudVolleyPlayerEntityLoader" >
		</loader>
		<loader id="loader/entity/ball" class="net.jgf.example.mudvolley1.loader.MudVolleyBallEntityLoader" >
		</loader> 	
				
	</service>
```

Note that, since these are custom loaders, their implementations are located within the net.jgf.example.mudvolley1 package.

Check implementations to see what's going on during scene and entity loading:

  * [net.jgf.example.mudvolley1.loader.MudVolleySceneLoader](http://code.google.com/p/jgf/source/browse/trunk/jgf/src/example/java/net/jgf/example/mudvolley1/loader/MudVolleySceneLoader.java)
  * [net.jgf.example.mudvolley1.loader.MudVolleyPlayerEntityLoader](http://code.google.com/p/jgf/source/browse/trunk/jgf/src/example/java/net/jgf/example/mudvolley1/loader/MudVolleyPlayerEntityLoader.java)
  * [net.jgf.example.mudvolley1.loader.MudVolleyBallEntityLoader](http://code.google.com/p/jgf/source/browse/trunk/jgf/src/example/java/net/jgf/example/mudvolley1/loader/MudVolleyBallEntityLoader.java)

This game requires two types of entities: Ball and Player. Entities have an `update()` method that is usually called every game step. In this game, entity movement is performed in the update method, but collisions are checked for in a separate "logic state".

### Game Logic ###

The Logic Manager contains a tree of states that the game may be active at a given point in time.

The combination and activation of these states is what drives the actual "logic" of the game. Active children are updated every iteration of the game.

```
	<service id="logic" class="net.jgf.logic.DefaultLogicManager">
	
	    <!-- The ExclusiveLogicNode ensures that only one of its child LogicNodes is
	         active at the same time. -->
		<logic id="logic/root" class="net.jgf.logic.ExclusiveLogicNode">
			
			<!-- This is a custom logic state that, when activated, loads the scene
			    and entities and moves to the next logic state. -->
			<logic id="logic/root/newgame" class="net.jgf.example.mudvolley1.logic.NewGameLogic" 
				autoLoad="true" autoActivate="true" >
			</logic>
			
			<!--  This logic state runs during game. It updates the scene (moving players,
			     ball, calculating collisions... -->
			<logic id="logic/root/ingame" class="net.jgf.logic.LogicStateNode">
			
				<logic id="logic/root/ingame/ingame" class="net.jgf.example.mudvolley1.logic.InGameLogic" 
					autoLoad="true" autoActivate="true" >
				</logic>
			
			</logic>
			
		</logic>
		
	</service>
```

### Game Aspect and Rendering ###

View Manager contains a tree of states that the application may be rendering at a given point in time. The combination and activation of these states is what drives the actual "aspect" of the game. Input processing is also done at this level.

```
	<service id="view" class="net.jgf.view.DefaultViewManager" >

		<view id="view/root" class="net.jgf.view.ViewStateNode">
			
			<!-- A ViewStateNode groups other states so they are better organized and
			also they can be activated and loaded together. All active children
			are updated and rendered in every game iteration. -->
			<view id="view/root/ingame" class="net.jgf.view.ViewStateNode">
				
				<!--  This is a custom view (note the mudvolley namespace). It handles
				input for both players in the mudvolley example game. -->
				<view id="view/root/ingame/input" class="net.jgf.example.mudvolley1.view.InputView"
					autoLoad="true" autoActivate="true" >
				</view>

                <!--  This is a JGF view that renders the scene contained in the SceneManager -->
				<view id="view/root/ingame/scene" class="net.jgf.jme.view.SceneRenderView"
					autoLoad="true" autoActivate="true" >
					<sceneManager ref="scene/manager" />		
				</view>
				
				<!-- This view renders the Skybox in the scene (the Scene needs to be 
				loaded with a Skybox for it to work) -->
				<view id="view/root/ingame/skybox" class="net.jgf.jme.view.SkyboxRenderView"
					autoLoad="true" autoActivate="true" >
					<sceneManager ref="scene/manager" />	
				</view>
				
			</view>
							
		</view>
			
	</service>
```