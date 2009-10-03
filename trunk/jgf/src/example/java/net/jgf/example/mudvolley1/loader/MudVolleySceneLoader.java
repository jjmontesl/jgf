
package net.jgf.example.mudvolley1.loader;



import net.jgf.config.Configurable;
import net.jgf.example.mudvolley1.MudSettings;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.jme.scene.util.SceneUtils;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.scene.SceneLoader;
import net.jgf.scene.Scene;
import net.jgf.system.Jgf;

import org.apache.log4j.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.util.resource.ResourceLocatorTool;

/**
 */
@Configurable
public final class MudVolleySceneLoader extends SceneLoader {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(MudVolleySceneLoader.class);

	/**
	 * Loads a scene, including scene data
	 */
	@Override
	public Scene load(Scene sceneGen, LoadProperties properties) {

		DefaultJmeScene scene = (DefaultJmeScene) sceneGen;

		scene.setTitle(Jgf.getApp().getName());

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

    // Add skybox
  	Node skybox = SceneUtils.setupSkyBox("mudvolley/skybox/grandcanyon/", ".jpg");
  	skybox.setRenderState(scene.getCommonRenderStates().get("fogDisabled"));
  	scene.getSky().setRootNode(skybox);
  	skybox.updateRenderState();

    // Build the scene

    Texture floorTexture = TextureManager.loadTexture(
    	ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "mudvolley/texture/hardwoodfloor.jpg"),
			Texture.MinificationFilter.NearestNeighborLinearMipMap,
			Texture.MagnificationFilter.NearestNeighbor);
  	TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    ts.setTexture(floorTexture, 0);
    ts.setEnabled(true);

    Box floor = new Box("floorBox", new Vector3f(- MudSettings.FIELD_WIDTH - 2, -5, -8), new Vector3f(MudSettings.FIELD_WIDTH + 2, 0, 8));
    floor.clearRenderState(RenderState.RS_TEXTURE);
    //quad.setLightCombineMode(Spatial.LightCombineMode.Off);
    floor.setTextureCombineMode(TextureCombineMode.Replace);
    floor.setCullHint(CullHint.Never);
    floor.setRenderState(ts);

    Box box = new Box("netBox", new Vector3f(-MudSettings.FIELD_NET_HALFWIDTH, 0, -7), new Vector3f(MudSettings.FIELD_NET_HALFWIDTH, MudSettings.FIELD_NET_HEIGHT, 7));
    box.setRenderState(ts);
    MaterialState msNet = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
    msNet.setEnabled(true);
    msNet.setAmbient(new ColorRGBA(0.2f, 1, 0, 1));
    box.setCullHint(CullHint.Never);
    box.setRenderState(msNet);

    Node fieldNode = new Node("fieldNode");
    fieldNode.attachChild(floor);
    fieldNode.attachChild(box);
    fieldNode.setModelBound(new BoundingBox());
    fieldNode.updateModelBound();


    fieldNode.getLocalTranslation().addLocal(0, 0, 0);
    fieldNode.updateGeometricState(0, true);
    fieldNode.lock();

    scene.getRootNode().attachChild(fieldNode);

    // Updates
    scene.getRootNode().updateRenderState();
    //scene.getRootNode().updateGeometricState(0, true);

		// Restart timer
		Timer.getTimer().reset();

		return scene;

	}

}
