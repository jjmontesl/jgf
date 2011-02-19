
package net.jgf.example.tanks.loader;



import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.example.tanks.camera.TanksCamera;
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
import com.jme.image.Texture.WrapMode;
import com.jme.light.PointLight;
import com.jme.light.SimpleLightNode;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Box;
import com.jme.scene.state.LightState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.util.resource.ResourceLocatorTool;
import com.sceneworker.app.undo.SpatialUndoAction;
import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

/**
 */
@Configurable
public final class TanksSceneLoader extends SceneLoader {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(TanksSceneLoader.class);

	public static int EXTENT_LIMIT = 5;
	
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
	  	
	    floorTexture.setWrap(WrapMode.Repeat);
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

		//floorNode.setCullHint(CullHint.Never);
		//floorNode.setModelBound(new BoundingBox());
		floorNode.updateModelBound();

		// TODO: Create camera in the loader
		scene.getCameraControllers().addCameraController(new StaticCamera(
				"scene/camera/test", 
				new Vector3f(0.5f * width, 0.45f * width, 0.5f * height),
				new Vector3f(0.5f * width, 0, 0.49f * height)
		));
		net.jgf.system.Jgf.getDirectory().addObject("scene/camera/test", scene.getCameraControllers().getCameraController("scene/camera/test"));
		scene.getCameraControllers().addCameraController(new TanksCamera("scene/camera/tanks"));
		net.jgf.system.Jgf.getDirectory().addObject("scene/camera/tanks", scene.getCameraControllers().getCameraController("scene/camera/tanks"));

		partition(floorNode, 8);
		partition(obstaclesNode, 8);
		
		fieldNode.attachChild(floorNode);
		fieldNode.attachChild(obstaclesNode);

	    //fieldNode.getLocalTranslation().addLocal(0, 0, 0);
	    //fieldNode.setLocalTranslation(new Vector3f(-map.width / 2, 0, -map.height / 2));
	    // TODO: Study how collisions and rendering are affected by the hierarchy of bounds
	    fieldNode.updateGeometricState(0, true);
	    
	    // center fieldnode
	    
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
	
	private void partition(Node node, int size) {
	    Map<String, List<Spatial>> nodes = new Hashtable<String, List<Spatial>>();
	    node.updateWorldBound(true);
	    for (Spatial spatial : node.getChildren()) {
	        
	        spatial.setModelBound(new BoundingBox());
	        spatial.updateModelBound();
	        spatial.updateWorldBound();
	        
	        int blockX = -111;
	        int blockY = -111;
	        if (spatial.getWorldBound() != null) {
	            blockX = (int) (((int)spatial.getWorldBound().getCenter().x) % size);
	            blockY = (int) (((int)spatial.getWorldBound().getCenter().z) % size);
	        }
	        String nodeName = "block-" + size + "_" + (blockX*size) + "_" + (blockY*size);
	        List<Spatial> blockNode = nodes.get(nodeName);
	        if (blockNode == null) {
	            blockNode = new ArrayList<Spatial>();
	            nodes.put(nodeName, blockNode);
	        }
	        blockNode.add(spatial);
	    }
	    
	    // Now attach all blocks
	    node.detachAllChildren();
	    for (java.util.Map.Entry<String, List<Spatial>>blockNode : nodes.entrySet()) {
	        logger.debug("Adding new spatial partition: " + blockNode.getKey());
	        Node tNode = new Node(blockNode.getKey());
	        for (Spatial spatial : blockNode.getValue()) {
	            tNode.attachChild(spatial);
	        }
	        tNode.updateWorldVectors();
	        node.attachChild(tNode);
	    }
	    node.updateWorldVectors();
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
			map.tiles[row][col] = tile;

			tile.raise = 0;
			tile.text = dataArray[i];

			// Walls
			if ((tile.text.charAt(0) >= '0') && (tile.text.charAt(0) <= '9')) {
				tile.raise = Integer.parseInt(String.valueOf(tile.text.charAt(0)));
			}
			if (tile.text.charAt(0) == '-') {
				tile.raise = -10;
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
				Tile testTile = map.tiles[tile.row + i][tile.col + j];
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
				map.tiles[tile.row + i][tile.col + j].group = group;
			}
		}
		
	}
	
	private void groupTiles(TanksMap map) {
		// Group tiles according to height
		int group = 1;
		for (int i = 0; i < map.height; i++) {
			for (int j = 0; j < map.width; j++) {
				Tile tile = map.tiles[i][j];
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
		for (maxRow = minRow; ((maxRow < map.height) && (map.tiles[maxRow][minCol].group == tile.group)); maxRow++);
		for (maxCol = minCol; ((maxCol < map.width) && (map.tiles[minRow][maxCol].group == tile.group)); maxCol++);
		maxCol--;
		maxRow--;

		
		if (tile.raise > -1.5f) {
			int random_floor = 0 - (FastMath.nextRandomInt(2, 6));
		    Box block = new Box("block_" + minRow + "_" + minCol,
		    		new Vector3f(minCol, random_floor, minRow), new Vector3f(maxCol + 1, 0.5f * tile.raise, maxRow + 1));
		    
		    // Adjust texture coords
			float texScale = 0.5f;
			
			float texMinY = texScale * minRow;
			float texMaxY = texScale * (maxRow + 1);
			float texMinX = texScale * minCol;
			float texMaxX = texScale * (maxCol + 1);
			/*texMaxY = texMaxY - texMinY;
			texMinY = 0;
			texMaxX = texMaxX - texMinX;
			texMinX = 0;*/
			
		    float[] textureData = {
		        1, 0, 0, 0, 0, 1, 1, 1, // back
		        1, 0, 0, 0, 0, 1, 1, 1, // right
		        1, 0, 0, 0, 0, 1, 1, 1, // front
		        1, 0, 0, 0, 0, 1, 1, 1, // left
		        texMinX, texMinY, texMinX, texMaxY, texMaxX, texMaxY, texMaxX, texMinY, // top
		        texMinX, texMinY, texMinX, texMaxY, texMaxX, texMaxY, texMaxX, texMinY   // bottom
		    };
		    FloatBuffer tex = block.getTextureCoords().get(0).coords;
            tex.clear();
            tex.put(textureData);
		    
		    block.clearRenderState(RenderState.RS_TEXTURE);
		    block.setTextureCombineMode(TextureCombineMode.Replace);
		    block.setModelBound(new BoundingBox());
		    block.updateModelBound();

		    block.setRenderState(ts);

		    if (tile.raise > 0) {
		    	obstaclesNode.attachChild(block);
		    } else {
		    	//floorNode.setModelBound(null);
		    	floorNode.attachChild(block);

		    }
		}
		
	}
	
	private void generateTerrain(TanksMap map, Node floorNode, Node obstaclesNode, DefaultJmeScene scene, TextureState ts) {

		
		LightState ls1 = (LightState) scene.getRootNode().getRenderState(StateType.Light);
		ls1.detachAll();
		scene.getRootNode().updateRenderState();
		
		
		Set<Integer> generatedBlocks = new HashSet<Integer>();
		
		int referenceIndex = 1;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
			
				Tile tile = map.tiles[i][j];
				
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
						Node referenceNode = new Node("referenceNode-" + referenceIndex);
						referenceNode.setLocalTranslation(new Vector3f(0.5f + tile.col, 0.5f * tile.raise, 0.5f + tile.row));
						SpatialReference reference = null;
						if ((valChar == 'x' || valChar == 'z')) {
							reference = new SpatialReference(tile.tag, referenceNode);
						} else {
							reference = new SpatialReference(tile.tag+"_"+referenceIndex, referenceNode);
						}
						
						
						PointLight light = new PointLight();
						//light.setLocation(referenceNode.getWorldTranslation().add(0, 1.5f, 0));
						LightState ls = (LightState) scene.getRootNode().getRenderState(StateType.Light);
						//if (ls.getLightList().size() < 6) {
							ls.attach(light);
							light.setEnabled(true);
							light.setAttenuate(true);
							light.setConstant(.1f);
							light.setLinear(.01f);
							light.setQuadratic(.1f);
							light.setAmbient(ColorRGBA.randomColor());
							light.setDiffuse(ColorRGBA.randomColor());
							scene.getRootNode().updateRenderState();
					        SimpleLightNode ln = new SimpleLightNode("ln" + i, light);
					        ln.setLocalTranslation(referenceNode.getWorldTranslation().add(0, 1.5f, 0));
					        scene.getRootNode().attachChild(ln);
					        
						//}
	
						scene.getReferences().addReference(reference);
						referenceIndex++;
	
					    if (tile.raise > 1) {
					    	//floorNode.setModelBound(null);
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
