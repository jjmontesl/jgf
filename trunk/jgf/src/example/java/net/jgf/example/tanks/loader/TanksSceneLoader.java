
package net.jgf.example.tanks.loader;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.example.tanks.ai.TanksMap;
import net.jgf.example.tanks.ai.TanksMap.Tile;
import net.jgf.jme.camera.StaticCamera;
import net.jgf.jme.refs.SpatialReference;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.scene.SceneLoader;
import net.jgf.scene.Scene;

import org.apache.log4j.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Box;
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

	protected int width;

	protected int height;

	protected String rawData;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		width = config.getInt(configPath + "/width");
		height = config.getInt(configPath + "/height");
		rawData = config.getString(configPath + "/map");

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

  	String[] dataArray;
  	dataArray = rawData.split("\\s+");

		// Generate map
    TanksMap map = new TanksMap(height, width);

		int row = 0;
		int col = 0;
		for (int i = 0; i < dataArray.length; i++) {

			Tile tile = new Tile();
			tile.col = col;
			tile.row = row;
			map.setTile(row, col, tile);

			tile.raise = 0;
			tile.text = dataArray[i];

			// Walls
			if ((tile.text.charAt(0) >= '0') && (tile.text.charAt(0) <= '9')) {
				tile.raise = Integer.parseInt(String.valueOf(tile.text.charAt(0)));
			}

			// Holes
			if (tile.text.charAt(0) == '-') {
				tile.obstacle = true;
				tile.raise = -2;
			}

			// ReferenceSet
			char valChar = (tile.text.length() > 1 ? tile.text.charAt(1) : tile.text.charAt(0));
			if ( ((valChar >= 'a') && (valChar <= 'z')) ||
					 ((valChar >= 'A') && (valChar <= 'Z')) ) {
				Node referenceNode = new Node("referenceNode-" + valChar);
				referenceNode.setLocalTranslation(new Vector3f(0.5f + col, 0.5f * tile.raise, 0.5f + row));
				SpatialReference reference = new SpatialReference(String.valueOf(valChar), referenceNode);

				scene.getReferences().addReference(reference);

		    if (tile.raise >= 1) {
		    	floorNode.setModelBound(null);
		    	floorNode.attachChild(reference.getSpatial());
		    } else {
		    	obstaclesNode.attachChild(reference.getSpatial());
		    	tile.obstacle = true;
		    }

		    referenceNode.updateGeometricState(0, true);

			}

			if (tile.raise > -1.5f) {
		    Box floor = new Box("cell_" + row + "_" + col,
		    		new Vector3f(col, -2, row), new Vector3f(col + 1, 0.5f * tile.raise, row + 1));
		    floor.clearRenderState(RenderState.RS_TEXTURE);
		    //quad.setLightCombineMode(Spatial.LightCombineMode.Off);
		    floor.setTextureCombineMode(TextureCombineMode.Replace);
		    floor.setModelBound(new BoundingBox());
		    floor.updateModelBound();

		    floor.setRenderState(ts);

		    if (tile.raise > 0) {
		    	obstaclesNode.attachChild(floor);
		    	tile.obstacle = true;
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
    /*
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
    */

    // Updates
    scene.getRootNode().updateRenderState();
    scene.getRootNode().updateGeometricState(0, true);

		// Restart timer
		Timer.getTimer().reset();

		scene.getProperties().put("map", map);

		return scene;

	}

}
