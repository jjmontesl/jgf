
package net.jgf.example.tanks.loader;



import java.util.HashSet;
import java.util.Set;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.example.tanks.loader.TanksMap.Tile;
import net.jgf.jme.camera.StaticCamera;
import net.jgf.jme.refs.SpatialReference;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.LoadProperties;
import net.jgf.loader.scene.SceneLoader;
import net.jgf.scene.Scene;

import org.apache.log4j.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
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

	public static int EXTENT_LIMIT = 6;
	
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
	
	
		// Generate map
	    TanksMap map = new TanksMap(height, width);
	    
	    fillMap (map, rawData);
		groupTiles (map);
	    generateTerrain (map, floorNode, obstaclesNode, scene, ts);

		floorNode.setCullHint(CullHint.Never);
		floorNode.setModelBound(new BoundingBox());
		//floorNode.updateModelBound();

		// TODO: Create camera in the loader
		scene.getCameraControllers().addCameraController(new StaticCamera(
				"scene/camera/test", 
				new Vector3f(0.5f * width, 0.45f * width, 0.5f * height),
				new Vector3f(0.5f * width, 0, 0.49f * height)
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
	
	    // Updates
	    scene.getRootNode().updateRenderState();
	    scene.getRootNode().updateGeometricState(0, true);

		// Restart timer
		Timer.getTimer().reset();

		scene.getProperties().put("map", map);

		return scene;

	}
	
	private void fillMap(TanksMap map, String rawData) {
		
	  	String[] dataArray;
	  	dataArray = rawData.split("\\s+");
		
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

			// Obstacles
			if (tile.raise > 0) {
		    	tile.obstacle = true;
			}
			
			// ReferenceSet
			tile.tag = tile.text.substring(1);
			
			col++;
			if (col >= width) { col = 0; row++; }
		}
	}
	
	private boolean isValidGroup (TanksMap map, Tile tile, int extentRow, int extentCol) {

		if (extentRow > TanksSceneLoader.EXTENT_LIMIT) return false;
		if (extentCol > TanksSceneLoader.EXTENT_LIMIT) return false;
		if (tile.row + extentRow > map.height) return false;
		if (tile.col + extentCol > map.width) return false;
		
		for (int i = 0; i < extentRow; i ++) {
			for (int j = 0; j < extentCol; j++) {
				Tile testTile = map.getTile(tile.row + i, tile.col + j);
				if ((testTile.raise != tile.raise) ||
				    (testTile.group != 0)) return false;
			}
		}
		
		return true;
			
	}
	
	private void groupTile (TanksMap map, Tile tile, int group) {

		int extentRow = 1;
		int extentCol = 1;
		boolean growRow = true;
		boolean growCol = true;
		
		boolean keepSearching;
		do {
			keepSearching = false;
			int tryExtentRow = extentRow + (growRow ? 1 : 0);
			int tryExtentCol = extentCol + (growCol ? 1 : 0);
			if (isValidGroup(map, tile, tryExtentRow, tryExtentCol)) {
				extentRow = tryExtentRow;
				extentCol = tryExtentCol;
				keepSearching = true;
			} else if ((growRow == true) && (growCol == true)) {
				growRow = false; 
				keepSearching = true;
			} else if ((growRow == false) && (growCol == true) && (extentRow == extentCol)) {
				growRow = true;
				growCol = false;
				keepSearching = true;
			} else {
				keepSearching = false;
			}
		} while (keepSearching);
		
		// Tag them
		for (int i = 0; i < extentRow; i ++) {
			for (int j = 0; j < extentCol; j++) {
				map.getTile(tile.row + i, tile.col + j).group = group;
			}
		}
		
	}
	
	private void groupTiles(TanksMap map) {
		// Group tiles according to height
		int group = 1;
		for (int i = 0; i < map.height; i++) {
			for (int j = 0; j < map.width; j++) {
				Tile tile = map.getTile(i, j);
				if (tile.group == 0) {
					// Group this block
					groupTile (map, tile, group);
					group++;
				}
			}
		}
	}
	
	private void generateBlock(TanksMap map, Tile tile, Node floorNode, Node obstaclesNode, TextureState ts) {

		int maxCol;
		int maxRow;
		int minCol = tile.col;
		int minRow = tile.row;
		
		// Find corners
		for (maxRow = minRow; ((maxRow < map.height) && (map.getTile(maxRow, minCol).group == tile.group)); maxRow++);
		for (maxCol = minCol; ((maxCol < map.width) && (map.getTile(minRow, maxCol).group == tile.group)); maxCol++);
		maxCol--;
		maxRow--;
		
		if (tile.raise > -1.5f) {
			int random_floor = 0 - (FastMath.nextRandomInt(2, 6));
		    Box block = new Box("block_" + minRow + "_" + minCol,
		    		new Vector3f(minCol, random_floor, minRow), new Vector3f(maxCol + 1, 0.5f * tile.raise, maxRow + 1));
		    block.clearRenderState(RenderState.RS_TEXTURE);
		    block.setTextureCombineMode(TextureCombineMode.Replace);
		    block.setModelBound(new BoundingBox());
		    block.updateModelBound();

		    block.setRenderState(ts);

		    if (tile.raise > 0) {
		    	obstaclesNode.attachChild(block);
		    } else {
		    	floorNode.setModelBound(null);
		    	floorNode.attachChild(block);

		    }
		}
		
	}
	
	private void generateTerrain(TanksMap map, Node floorNode, Node obstaclesNode, DefaultJmeScene scene, TextureState ts) {

		Set<Integer> generatedBlocks = new HashSet<Integer>();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
			
				Tile tile = map.getTile(i, j);
				
				// Blocks
				if (!generatedBlocks.contains(tile.group)) {
					generateBlock(map, tile, floorNode, obstaclesNode, ts);
					generatedBlocks.add(tile.group);
				}
				
				// References
				if (tile.tag.length() > 0) {
					char valChar = tile.tag.charAt(0);
					if ( ((valChar >= 'a') && (valChar <= 'z')) ||
						 ((valChar >= 'A') && (valChar <= 'Z')) ) {
						Node referenceNode = new Node("referenceNode-" + valChar);
						referenceNode.setLocalTranslation(new Vector3f(0.5f + tile.col, 0.5f * tile.raise, 0.5f + tile.row));
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
				
				}

			}
		}
		
	}

}
