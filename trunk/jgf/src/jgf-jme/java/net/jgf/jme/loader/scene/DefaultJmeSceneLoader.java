
package net.jgf.jme.loader.scene;

import net.jgf.config.Configurable;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.jme.scene.util.SceneUtils;
import net.jgf.loader.BaseLoader;
import net.jgf.loader.LoadProperties;
import net.jgf.scene.Scene;



/**
 */
@Configurable
public class DefaultJmeSceneLoader extends BaseLoader<Scene> {

	@Override
	public Scene load(Scene base, LoadProperties properties) {

		DefaultJmeScene scene = (DefaultJmeScene) base;

		if (scene == null) scene = new DefaultJmeScene();

		combineProperties(properties);

		// TODO: Check this
		scene.setTitle(properties.get("DefaultJmeSceneLoader.title"));

		// Initialize common render states
		SceneUtils.createCommonRenderStates(scene);

		// Enable z-buffer by default
        scene.getRootNode().setRenderState(scene.getCommonRenderStates().get("zBuffer"));
    
        // Enable culling by default
        scene.getRootNode().setRenderState(scene.getCommonRenderStates().get("cullBack"));
    
        // Lighting
        scene.getRootNode().setRenderState(SceneUtils.createDefaultLightState());
    
        // Add fog
        //scene.getRootNode().setRenderState(scene.getCommonRenderStates().get("fog"));
    
		return scene;

	}



}
