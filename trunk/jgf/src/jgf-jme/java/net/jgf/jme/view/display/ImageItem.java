
package net.jgf.jme.view.display;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.config.JmeConfigHelper;

import com.jme.image.Texture;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.TestFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;

/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class ImageItem extends DisplayItem {

	protected Vector3f center = new Vector3f();

	protected String textureUrl;

	protected float size = 1.0f;

	private Quad quad;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		setCenter(JmeConfigHelper.getVector3f(config, configPath + "/center"));
		setTextureUrl(config.getString(configPath + "/textureUrl"));
		setSize(config.getFloat(configPath + "/size"));

	}



	/* (non-Javadoc)
	 * @see net.jgf.jme.view.display.DisplayItem#load(com.jme.scene.Node)
	 */
	@Override
	public void refreshNode(Node display) {

		quad = new Quad("quad-" + this.getId());
		quad.setCullHint(Spatial.CullHint.Never);
		quad.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		//quad.setZOrder(Integer.MIN_VALUE);
		quad.setLightCombineMode(Spatial.LightCombineMode.Off);
		quad.setTextureCombineMode(TextureCombineMode.Replace);

		// Prepare some resources
		Texture imageTexture = TextureManager.loadTexture(
    	ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, getTextureUrl()),
			Texture.MinificationFilter.BilinearNearestMipMap,
			Texture.MagnificationFilter.Bilinear);
	  	TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	    ts.setTexture(imageTexture, 0);
	    ts.setEnabled(true);
	    quad.setRenderState(ts);

	    /*
	    quad.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
	    quad.updateGeometricState(0, true);
		*/
	    
		BlendState as = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		as.setBlendEnabled(true);
		as.setTestEnabled(true);
		as.setTestFunction(TestFunction.GreaterThan);
		as.setEnabled(true);
		quad.setRenderState(as);
	    
		Vector2f displaySize = new Vector2f(DisplaySystem.getDisplaySystem().getRenderer().getWidth(), DisplaySystem.getDisplaySystem().getRenderer().getHeight());

		// TODO: FIXME: size is dependant on image real size and therefore on screen size!
	    //float imageHeight = getSize() * ts.getTexture().getImage().getHeight();
		//float imageWidth = getSize() * ts.getTexture().getImage().getWidth();
		
		// TODO: This uses screen height as scale reference, no matter what aspect ratio screen has.
		float imageHeight = getSize() * displaySize.y;
		float imageWidth = getSize() * displaySize.y / ts.getTexture().getImage().getHeight() * ts.getTexture().getImage().getWidth();
		
		quad.resize(imageWidth, imageHeight);


		Vector3f ortoCenter = new Vector3f(0.5f * displaySize.x, 0.5f * displaySize.y, 0);
		quad.getLocalTranslation().set(ortoCenter.x * center.x + ortoCenter.x, ortoCenter.y * center.y + ortoCenter.y, 0 );

		quad.updateRenderState();

		display.attachChild(quad);

	}

	/* (non-Javadoc)
	 * @see net.jgf.jme.view.display.DisplayItem#destroyNode(com.jme.scene.Node)
	 */
	@Override
	public void destroyNode(Node display) {
		// TODO: destroy textures, etc.?
		display.detachChild(quad);
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

	/**
	 * @return the textureUrl
	 */
	public String getTextureUrl() {
		return textureUrl;
	}

	/**
	 * @param textureUrl the textureUrl to set
	 */
	public void setTextureUrl(String textureUrl) {
		this.textureUrl = textureUrl;
	}

	/**
	 * @return the size
	 */
	public float getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(float size) {
		this.size = size;
	}



}