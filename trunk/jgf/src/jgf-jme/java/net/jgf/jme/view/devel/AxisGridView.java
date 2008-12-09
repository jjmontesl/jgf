/*
 * JGF - Java Game Framework
 * $Id$
 *
 * Copyright (c) 2008, JGF - Java Game Framework
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     * Neither the name of the 'JGF - Java Game Framework' nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <copyright holder> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <copyright holder> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.jgf.jme.view.devel;

import java.util.ArrayList;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.scene.SceneManager;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.ZBufferState.TestFunction;
import com.jme.system.DisplaySystem;

/**
 * <p>The {@link AxisGridView} draws a grid of lines on the scene, and also
 * three main lines representing the axes in red, green and blue (red for X, green
 * for Y and blue for Z, so RGB colors correspond to XYZ axis).</p>
 * <p>The grid is a always a square, by default its extent is 40 (along each positive and
 * negative axis) and by default it is lying on the XZ plane (it can be rotated).</p>
 * <p>The grid is generated at view's loading time, so changes to the attributes
 * require to reload this ViewState for them to take effect.</p>
 * <p>This View uses the Camera defined by the associated Scene (through the passed SceneManager).
 * If SceneManager, Scene or Camera are null, the grid is drawn using the current camera settings.</p>
 *
 * @author jjmontes
 */
@Configurable
public class AxisGridView extends BaseViewState {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AxisGridView.class);

	/**
	 * Tbe SceneManager that references our camera
	 */
	protected SceneManager sceneManager;

	/**
	 * AxisGridView root node, where the grid is laid.
	 */
	protected Node rootNode;

	/**
	 * If drawBehind is false, the grid is tested with the z-buffer, which means that
	 * only the visible parts of the grid will be seen and objects in front of the grid will
	 * hide it.
	 */
	protected boolean drawBehind = false;

	/**
	 * If drawAxis is true, Red, Green and Blue axis are drawn for the XYZ axis. Axis are
	 * never rotated, so they are always world axis, but they are translated to the grid
	 * center.
	 */
	protected boolean drawAxis = true;

	/**
	 * Center of the grid.
	 */
	protected final Vector3f center = new Vector3f();

	/**
	 * Space between grid lines.
	 */
	protected float spacing = 2.0f;

	/**
	 * Draw a bold line every given number of lines.
	 */
	protected int marker = 5;

	/**
	 * Grid rotation. This is an Euler rotation (X,Y,Z). Values are multiplied by PI
	 * before being applied.
	 */
	protected final Vector3f rotatePi = new Vector3f();

	/**
	 * Grid extent. The extent is applied towards the positive and also negative sides of the grid.
	 */
	protected float extent = 40.0f;

	/**
	 * Loading {@link AxisGridView} will create the grid. If the grid
	 * attributes are modified, changes will not be visible until this ViewState is reloaded.
	 *
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void load() {
		super.load();
		rootNode = new Node("axisGrid");
		rootNode.attachChild(buildGeometry());
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


	/**
	 * Creates the grid. This method is called internally.
	 */
	protected Node buildGeometry() {

	  Node grid = new Node("grid");
	  Node axis = new Node("axis");
	  Node axisGrid = new Node ("axisGrid");

  	//Create Grid
    ArrayList<Vector3f> markerVertices = new ArrayList<Vector3f>();
    ArrayList<Vector3f> regularVertices = new ArrayList<Vector3f>();
    for (int i = 0; i * spacing <= extent; i++) {

    	if (i % marker > 0) {
    		// Normal line
	    	regularVertices.add(new Vector3f(-extent, 0, i * spacing));
	    	regularVertices.add(new Vector3f(extent, 0, i * spacing));
	    	regularVertices.add(new Vector3f(-extent, 0, -i * spacing));
	    	regularVertices.add(new Vector3f(extent, 0, -i * spacing));
	    	regularVertices.add(new Vector3f(i * spacing, 0, -extent));
	    	regularVertices.add(new Vector3f( i * spacing, 0, extent));
	    	regularVertices.add(new Vector3f(-i * spacing, 0, -extent));
	    	regularVertices.add(new Vector3f( -i * spacing, 0, extent));
    	} else {
    		// Marker line
    		markerVertices.add(new Vector3f(-extent, 0, i * spacing));
    		markerVertices.add(new Vector3f(extent, 0, i * spacing));
    		markerVertices.add(new Vector3f(-extent, 0, -i * spacing));
    		markerVertices.add(new Vector3f(extent, 0, -i * spacing));
    		if (i != 0) {
	    		markerVertices.add(new Vector3f(i * spacing, 0, -extent));
	    		markerVertices.add(new Vector3f( i * spacing, 0, extent));
	    		markerVertices.add(new Vector3f(-i * spacing, 0, -extent));
	    		markerVertices.add(new Vector3f( -i * spacing, 0, extent));
    		}
    	}

    }

    Geometry regularGrid = new Line("regularLine", regularVertices.toArray(new Vector3f[]{}), null,null,null);
    regularGrid.getDefaultColor().set(ColorRGBA.darkGray);
    grid.attachChild(regularGrid);
    Geometry markerGrid = new Line("markerLine", markerVertices.toArray(new Vector3f[]{}), null,null,null);
    regularGrid.getDefaultColor().set(ColorRGBA.lightGray);
    grid.attachChild(markerGrid);

    grid.getLocalRotation().fromAngles(FastMath.PI * rotatePi.x, FastMath.PI * rotatePi.y, FastMath.PI * rotatePi.z);

    axisGrid.attachChild(grid);

    // Create Axis

    if (drawAxis) {

	    Vector3f xAxis = new Vector3f(extent,0,0);	//red
			Vector3f yAxis = new Vector3f(0,extent,0);	//green
			Vector3f zAxis = new Vector3f(0,0,extent);	//blue

	    ColorRGBA[] red = new ColorRGBA[2];
	    red[0] = new ColorRGBA(ColorRGBA.red);
	    red[1] = new ColorRGBA(ColorRGBA.red);

	    ColorRGBA[] green = new ColorRGBA[2];
	    green[0] = new ColorRGBA(ColorRGBA.green);
	    green[1] = new ColorRGBA(ColorRGBA.green);

	    ColorRGBA[] blue = new ColorRGBA[2];
	    blue[0] = new ColorRGBA(ColorRGBA.blue);
	    blue[1] = new ColorRGBA(ColorRGBA.blue);

	    Line lx = new Line("xAxis",new Vector3f[]{xAxis.negate(),xAxis},null,red,null);
	    Line ly = new Line("yAxis",new Vector3f[]{yAxis.negate(),yAxis},null,green,null);
	    Line lz = new Line("zAxis",new Vector3f[]{zAxis.negate(),zAxis},null,blue,null);

	    lx.setModelBound(new BoundingBox());	// Important to set bounds to prevent some error
	    lx.updateModelBound();
	    ly.setModelBound(new BoundingBox());	// Important to set bounds to prevent some error
	    ly.updateModelBound();
	    lz.setModelBound(new BoundingBox());	// Important to set bounds to prevent some error
	    lz.updateModelBound();

	    axis.attachChild(lx);
	    axis.attachChild(ly);
	    axis.attachChild(lz);

	    axisGrid.attachChild(axis);

    }

    // RenderStates for the whole grid and axis

    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    axisGrid.setTextureCombineMode(TextureCombineMode.Off);
    axisGrid.setRenderState(ts);

    ZBufferState zs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
    zs.setFunction(drawBehind ? TestFunction.Always : TestFunction.LessThanOrEqualTo);
    zs.setWritable(false);
    zs.setEnabled(true);
    axisGrid.setRenderState(zs);

    axisGrid.setLightCombineMode(LightCombineMode.Off);

    axisGrid.updateRenderState();

    axisGrid.getLocalTranslation().set(center);
    axisGrid.updateGeometricState(0, true);

    axisGrid.lock();

    return axisGrid;
	}


	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#render(float)
	 */
	@Override
	public void render(float tpf) {

		if (! this.active) return;

		// Update the camera controller
		if ((sceneManager != null) &&
				(sceneManager.getScene() != null) &&
				(sceneManager.getScene().getCamera() != null)) {
			sceneManager.getScene().getCamera().update(tpf);
		}
		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);

	}


	/**
	 * Configures this object from Config.
	 * @see Configurable
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.drawBehind = config.getBoolean(configPath + "/drawBehind", this.drawBehind);
		this.center.set(JmeConfigHelper.getVector3f(config, configPath + "/center", this.center));
		this.rotatePi.set(JmeConfigHelper.getVector3f(config, configPath + "/rotatePi", this.rotatePi));
		this.extent = config.getFloat(configPath + "/extent", this.extent);
		this.marker = config.getInt(configPath + "/marker", this.marker);
		this.spacing = config.getFloat(configPath + "/spacing", this.spacing);

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
	 * If drawAxis is true, Red, Green and Blue axis are drawn for the XYZ axis. Axis are
	 * never rotated, so they are always world axis, but they are translated to the grid
	 * center.
	 */
	public boolean isDrawAxis() {
		return drawAxis;
	}

	/**
	 * If drawAxis is true, Red, Green and Blue axis are drawn for the XYZ axis. Axis are
	 * never rotated, so they are always world axis, but they are translated to the grid
	 * center.
	 */
	public void setDrawAxis(boolean drawAxis) {
		this.drawAxis = drawAxis;
	}

	/**
	 * If drawBehind is false, the grid is tested with the z-buffer, which means that
	 * only the visible parts of the grid will be seen and objects in front of the grid will
	 * hide it.
	 */
	public boolean isDrawBehind() {
		return drawBehind;
	}

	/**
	 * If drawBehind is false, the grid is tested with the z-buffer, which means that
	 * only the visible parts of the grid will be seen and objects in front of the grid will
	 * hide it.
	 */
	public void setDrawBehind(boolean drawBehind) {
		this.drawBehind = drawBehind;
	}

	/**
	 * Returns the grid center.
	 */
	public Vector3f getCenter() {
		return center;
	}

	/**
	 * Sets the grid center (the supplied vector is copied).
	 */
	public void setCenter(Vector3f center) {
		this.center.set(center);
	}

	/**
	 * @return the rootNode
	 */
	public Node getRootNode() {
		return rootNode;
	}

	/**
	 * @param rootNode the rootNode to set
	 */
	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * Returns the space between grid lines.
	 */
	public float getSpacing() {
		return spacing;
	}

	/**
	 * Sets the space between grid lines.
	 */
	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}

	/**
	 * A brighter line is drawn every "marker" lines. Returns that value.
	 */
	public int getMarker() {
		return marker;
	}

	/**
	 * A brighter line is drawn every "marker" lines. Sets that value.
	 */
	public void setMarker(int marker) {
		this.marker = marker;
	}

	/**
	 * Returns the grid extent. The extent extends over the positive and negative sides of each axis, so
	 * the grid side will be twice this value.
	 */
	public float getExtent() {
		return extent;
	}

	/**
	 * Sets the grid extent. The extent extends over the positive and negative sides of each axis, so
	 * the grid side will be twice this value.
	 */
	public void setExtent(float extent) {
		this.extent = extent;
	}

	/**
	 * Retrieves the grid rotation as an Euler's rotation. Values are multiplied by PI.
	 */
	public Vector3f getRotatePi() {
		return rotatePi;
	}

	/**
	 * Sets the grid rotation as an Euler's rotation. Values are multiplied by PI.
	 * before being applied.
	 */
	public void setRotatePi(Vector3f rotatePi) {
		this.rotatePi.set(rotatePi);
	}

}
