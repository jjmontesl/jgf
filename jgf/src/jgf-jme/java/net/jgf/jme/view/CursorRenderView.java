
package net.jgf.jme.view;



import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;

/**
 */
@Configurable
public class CursorRenderView extends BaseViewState {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(CursorRenderView.class);

	protected Node rootNode;

	protected AbsoluteMouse mouse;

	protected String textureUrl;

	public CursorRenderView() {
		rootNode = new Node();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void doLoad() {

		
		super.doLoad();

		mouse = new AbsoluteMouse("MouseInput", DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem.getDisplaySystem().getHeight());
		mouse.setSpeed(1.0f);

		// Prepare some resources
    Texture floorTexture = TextureManager.loadTexture(
    	ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "tanks/texture/cursor.png"),
			Texture.MinificationFilter.NearestNeighborLinearMipMap,
			Texture.MagnificationFilter.NearestNeighbor);
  	TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
    ts.setTexture(floorTexture, 0);
    ts.setEnabled(true);

    BlendState as1 = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
    as1.setBlendEnabled(true);
    as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
    as1.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
    as1.setTestEnabled(true);
    as1.setTestFunction(BlendState.TestFunction.GreaterThan);
    mouse.setRenderState(as1);

    mouse.setRenderState(ts);
    mouse.setLocalScale(1.0f);

    mouse.setHotSpotOffset(new Vector3f(0.0f, 0.0f, 0.0f));

    rootNode.attachChild(mouse);
    rootNode.updateRenderState();
    rootNode.updateModelBound();

	}

	
	
	@Override
	public void doUnload() {
		super.doUnload();
		rootNode.detachChild(mouse);
		mouse = null;
	}

	/**
	 * Scene geometry update.
	 */
	@Override
	public void doUpdate(float tpf) {

			if (! this.active) return;

			rootNode.updateGeometricState(tpf, true);

	}

	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void doRender(float tpf) {

		if (! this.active) return;

		DisplaySystem display = DisplaySystem.getDisplaySystem();

		display.getRenderer().getCamera().update();

		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);

	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		textureUrl = config.getString(configPath + "/textureUrl");

	}

	/**
	 * @return the mouse
	 */
	public AbsoluteMouse getMouse() {
		return mouse;
	}

}
