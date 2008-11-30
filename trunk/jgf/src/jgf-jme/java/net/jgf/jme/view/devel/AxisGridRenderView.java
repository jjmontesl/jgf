package net.jgf.jme.view.devel;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.scene.SceneManager;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.ZBufferState.TestFunction;
import com.jme.system.DisplaySystem;

/**
 */
@Configurable
public class AxisGridRenderView extends BaseViewState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AxisGridRenderView.class);


	/**
	 * The camera controller that manages the Scene Render camera.
	 */
	protected SceneManager sceneManager;

	protected Node rootNode;

	protected boolean drawBehind = true;

	protected Vector3f center = Vector3f.ZERO.clone();

	// Build the axes
	public static final int GRID_LINES = 30;
	public static final float GRID_SPACING = 2;

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void load() {
		super.load();

		rootNode = new Node();
		Node unlit = buildGeometry();
		rootNode.attachChild(unlit);
		rootNode.lock();
	}



	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseState#unload()
	 */
	@Override
	public void unload() {
		super.unload();
		if (rootNode != null) {
			rootNode.detachAllChildren();
			rootNode = null;
		}
	}



	protected Node buildGeometry() {
		Node unlit = new Node();

	  Node solids = new Node("solids");
	  //Node dotteds = new Node("dotteds");

  	//Create Grid
    Vector3f[] vertices = new Vector3f[GRID_LINES * 2 * 2];
    float edge = GRID_LINES / 2 * GRID_SPACING;
    for (int ii = 0, idx = 0; ii < GRID_LINES; ii++) {
      float coord = (ii - GRID_LINES / 2) * GRID_SPACING;
      vertices[idx++] = new Vector3f(-edge + center.x, center.y, coord + center.z);
      vertices[idx++] = new Vector3f(+edge + center.x, center.y, coord + center.z);
      vertices[idx++] = new Vector3f(coord + center.x, center.y, -edge + center.z);
      vertices[idx++] = new Vector3f(coord + center.x, center.y, +edge + center.z);
    }

    Geometry grid = new com.jme.scene.Line("grid",vertices, null,null,null);
    grid.getDefaultColor().set(ColorRGBA.darkGray);  // gridconstants do not need bounds obviously...
    solids.attachChild(grid);

    /*
    Line gridDotted = new com.jme.scene.Line("gridDotted", vertices, null,null,null);
    gridDotted.setStipplePattern((short)0xFF00);
    gridDotted.getDefaultColor().set(ColorRGBA.darkGray);  // gridconstants do not need bounds obviously...
    dotteds.attachChild(gridDotted);
    */

    //Create Axis
    Vector3f xAxis = new Vector3f(edge,0,0);	//red
		Vector3f yAxis = new Vector3f(0,edge,0);	//green
		Vector3f zAxis = new Vector3f(0,0,edge);	//blue

    ColorRGBA[] red = new ColorRGBA[2];
    red[0] = new ColorRGBA(ColorRGBA.red);
    red[1] = new ColorRGBA(ColorRGBA.red);

    ColorRGBA[] green = new ColorRGBA[2];
    green[0] = new ColorRGBA(ColorRGBA.green);
    green[1] = new ColorRGBA(ColorRGBA.green);

    ColorRGBA[] blue = new ColorRGBA[2];
    blue[0] = new ColorRGBA(ColorRGBA.blue);
    blue[1] = new ColorRGBA(ColorRGBA.blue);

    Line lx = new Line("xAxis",new Vector3f[]{xAxis.negate().add(center),xAxis.add(center)},null,red,null);
    Line ly = new Line("yAxis",new Vector3f[]{yAxis.negate().add(center),yAxis.add(center)},null,green,null);
    Line lz = new Line("zAxis",new Vector3f[]{zAxis.negate().add(center),zAxis.add(center)},null,blue,null);

    /*
    Line dlx = new Line("xAxisDotted",new Vector3f[]{xAxis.negate(),xAxis},null,red,null);
    dlx.setStipplePattern((short)0xFF00);
    Line dly = new Line("yAxisDotted",new Vector3f[]{yAxis.negate(),yAxis},null,green,null);
    dly.setStipplePattern((short)0xFF00);
    Line dlz = new Line("zAxisDotted",new Vector3f[]{zAxis.negate(),zAxis},null,blue,null);
    dlz.setStipplePattern((short)0xFF00);
    */

    lx.setModelBound(new BoundingBox());	// Important to set bounds to prevent some error
    lx.updateModelBound();
    ly.setModelBound(new BoundingBox());	// Important to set bounds to prevent some error
    ly.updateModelBound();
    lz.setModelBound(new BoundingBox());	// Important to set bounds to prevent some error
    lz.updateModelBound();
    /*
    dlx.setModelBound(new BoundingBox());	// Important to set bounds to prevent some error
    dlx.updateModelBound();
    dly.setModelBound(new BoundingBox());	// Important to set bounds to prevent some error
    dly.updateModelBound();
    dlz.setModelBound(new BoundingBox());	// Important to set bounds to prevent some error
    dlz.updateModelBound();
		*/


    solids.attachChild(lx);
    solids.attachChild(ly);
    solids.attachChild(lz);

    /*
    dotteds.attachChild(dlx);
    dotteds.attachChild(dly);
    dotteds.attachChild(dlz);
    */

    ZBufferState zs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
    zs.setFunction(TestFunction.LessThanOrEqualTo);
    zs.setEnabled(! drawBehind);
    solids.setRenderState(zs);
    solids.updateRenderState();
    unlit.attachChild(solids);

    /*
    ZBufferState zs2 = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
    zs2.setFunction(TestFunction.GreaterThan);
    dotteds.setRenderState(zs2);
    dotteds.updateRenderState();
    if (drawBehind) unlit.attachChild(dotteds);
    */

    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    unlit.setTextureCombineMode(TextureCombineMode.Off);
    unlit.setRenderState(ts);
    unlit.updateRenderState();

    unlit.lock();

    return unlit;
	}


	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void render(float tpf) {

		if (! this.active) return;

		// Update the camera controller
		if (sceneManager.getCamera() == null) return;

		// TODO: Do only when needed
		//scene.getRootNode().updateRenderState();

		//if (passManager != null) passManager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);

		/*
		SceneMonitor.getMonitor().renderViewer(DisplaySystem.getDisplaySystem().getRenderer());
		*/

	}


	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.drawBehind = config.getBoolean(configPath + "/drawBehind", this.drawBehind);
		this.center = JmeConfigHelper.getVector3f(config, configPath + "/gridCenter", this.center);

		net.jgf.system.Jgf.getDirectory().register(this, "sceneManager", config.getString(configPath + "/sceneManager/@ref"));
	}

	/**
	 * @return the sceneManager
	 */
	public SceneManager getSceneManager() {
		return sceneManager;
	}

	/**
	 * @param sceneManager the sceneManager to set
	 */
	public void setSceneManager(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}



	/**
	 * @return the drawBehind
	 */
	public boolean isDrawBehind() {
		return drawBehind;
	}

	/**
	 * @param drawBehind the drawBehind to set
	 */
	public void setDrawBehind(boolean drawBehind) {
		this.drawBehind = drawBehind;
	}

	/**
	 * @return the center
	 */
	public Vector3f getCenter() {
		return center;
	}

	/**
	 * @param center the center to set
	 */
	public void setCenter(Vector3f center) {
		this.center = center;
	}

}
