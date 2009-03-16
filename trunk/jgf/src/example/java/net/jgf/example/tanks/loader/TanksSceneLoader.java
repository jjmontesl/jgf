
package net.jgf.example.tanks.loader;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.camera.StaticCamera;
import net.jgf.jme.refs.SpatialReference;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.scene.SceneLoader;
import net.jgf.scene.Scene;

import org.apache.log4j.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.state.ClipState;
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
public final class TanksSceneLoader extends SceneLoader {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(TanksSceneLoader.class);

	String[] dataArray;

	int width;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		width = config.getInt(configPath + "/width");
		String rawData = config.getString(configPath + "/map");

		dataArray = rawData.split("\\s+");

	}

	/**
	 * Loads a scene, including scene data
	 */
	@Override
	public Scene load(Scene base, LoadProperties properties) {

		DefaultJmeScene scene = (DefaultJmeScene) base;

		// Prepare some resources
    Texture floorTexture = TextureManager.loadTexture(
    	ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "mudvolley/texture/hardwoodfloor.jpg"),
			Texture.MinificationFilter.NearestNeighborLinearMipMap,
			Texture.MagnificationFilter.NearestNeighbor);
  	TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    ts.setTexture(floorTexture, 0);
    ts.setEnabled(true);

    Node fieldNode = new Node("fieldNode");
    Node floorNode = new Node("floorNode");
    Node obstaclesNode = new Node("obstaclesNode");

		// Generate map
		int row = 0;
		int col = 0;
		for (int i = 0; i < dataArray.length; i++) {

			int height = 0;
			String data = dataArray[i];

			// Walls
			if ((data.charAt(0) >= '0') && (data.charAt(0) <= '9')) {
				height = Integer.parseInt(String.valueOf(data.charAt(0)));
			}

			// Holes
			if (data.charAt(0) == '-') height = -2;

			// References
			char valChar = (data.length() > 1 ? data.charAt(1) : data.charAt(0));
			if ( ((valChar >= 'a') && (valChar <= 'z')) ||
					 ((valChar >= 'A') && (valChar <= 'Z')) ) {
				Node referenceNode = new Node("referenceNode-" + valChar);
				referenceNode.setLocalTranslation(new Vector3f(0.5f + col, 0.5f * height, 0.5f + row));
				SpatialReference reference = new SpatialReference(String.valueOf(valChar), referenceNode);
				scene.getReferences().addReference(reference);
			}

			// Load extra models
			/*
			if ((data.length() > 1) && (data.charAt(1) == 'L')) {
				BaseLoader<Node> modelLoader = Jgf.getDirectory().getObjectAs("loader/model/tanks", BaseLoader.class);
				Node model = modelLoader.load("ConverterLoader.resourceUrl=tanks/model/lamppost/lamppost.dae");
				model.setLocalTranslation(new Vector3f(0.5f + col, 0.5f * height, 0.5f + row));
				obstaclesNode.attachChild(model);
				obstaclesNode.updateRenderState();
			}
			 */

			if (height > -1.5f) {
		    Box floor = new Box("cell_" + row + "_" + col,
		    		new Vector3f(col, -2, row), new Vector3f(col + 1, 0.5f * height, row + 1));
		    floor.clearRenderState(RenderState.RS_TEXTURE);
		    //quad.setLightCombineMode(Spatial.LightCombineMode.Off);
		    floor.setTextureCombineMode(TextureCombineMode.Replace);
		    floor.setModelBound(new OrientedBoundingBox());
		    floor.updateModelBound();

		    floor.setRenderState(ts);

		    if (height > 0.1f) {
		    	obstaclesNode.attachChild(floor);
		    } else {
		    	floorNode.setModelBound(null);
		    	floorNode.attachChild(floor);

		    }
			}

	    col++;
			if (col >= width) { col = 0; row++; }
		}

		floorNode.setCullHint(CullHint.Never);
		floorNode.setModelBound(new BoundingBox());
		//floorNode.updateModelBound();

		// TODO: Create camera in the loader
		scene.getCameraControllers().addCameraController(new StaticCamera(
				"scene/camera/test", new Vector3f(0.5f * width, 0.45f * width, 0.5f * row),
				new Vector3f(0.5f * width, 0, 0.49f * row)
		));
		net.jgf.system.Jgf.getDirectory().addObject("scene/camera/test", scene.getCameraControllers().getCameraController("scene/camera/test"));

		fieldNode.attachChild(floorNode);
		fieldNode.attachChild(obstaclesNode);


    fieldNode.getLocalTranslation().addLocal(0, 0, 0);
    // TODO: Study how collisions and rendering are affected by the hierarchy of bounds
    //fieldNode.updateGeometricState(0, true);
    fieldNode.lock();
    fieldNode.lockMeshes();

    scene.getRootNode().attachChild(fieldNode);

    // Water
    InteractiveWater water = new net.jgf.example.tanks.loader.InteractiveWater("water", 40);
    water.setModelBound(new BoundingBox());
    water.updateModelBound();

    MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
    ms.setAmbient(new ColorRGBA(0.1f, 0.15f, 0.25f, 1.0f));
    ms.setDiffuse(new ColorRGBA(0.2f, 0.25f, 0.4f, 1.0f));
    ms.setSpecular(new ColorRGBA(0.2f, 0.2f, 0.25f, 1.0f));
    ms.setShininess(70.0f);
    water.setRenderState(ms);

    ClipState cs = DisplaySystem.getDisplaySystem().getRenderer().createClipState();
    cs.setEnableClipPlane(ClipState.CLIP_PLANE0, true);
    cs.setClipPlaneEquation(ClipState.CLIP_PLANE0, 0, -1, 0, 0);
    water.setRenderState(cs);

    water.updateRenderState();
    water.getLocalTranslation().set(-20 + width / 2, -1.2f, -20 + row / 2);
    water.getLocalScale().set(1f, 0.2f, 1f);

    scene.getRootNode().attachChild(water);

    // Updates
    scene.getRootNode().updateRenderState();
    scene.getRootNode().updateGeometricState(0, true);

		// Restart timer
		Timer.getTimer().reset();

		return scene;

	}

}
