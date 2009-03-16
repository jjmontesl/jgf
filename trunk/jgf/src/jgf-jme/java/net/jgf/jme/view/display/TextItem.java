
package net.jgf.jme.view.display;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.jme.scene.util.TextQuadUtils;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

/**
 *
 * @author jjmontes
 * @version $Revision$
 */
@Configurable
public class TextItem extends DisplayItem {

	protected Vector3f center = new Vector3f();

	protected String text = "UNDEFINED";

	protected float size = 0.2f;

	protected float ratio = 1.0f;

	protected String font = TextQuadUtils.DEFAULT_FONT;

	protected Quad quad = null;

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		setCenter(JmeConfigHelper.getVector3f(config, configPath + "/center"));
		setText(config.getString(configPath + "/text"));
		setFont(config.getString(configPath + "/font", getFont()));
		setSize(config.getFloat(configPath + "/size"));
		setRatio(config.getFloat(configPath + "/ratio", getRatio()));

	}



	/* (non-Javadoc)
	 * @see net.jgf.jme.view.display.DisplayItem#load(com.jme.scene.Node)
	 */
	@Override
	public void refreshNode(Node display) {

		// TODO: Review this refreshing strategy... should be usable for HUDs too
		if (quad != null) display.detachChild(quad);
		// TODO: Remove texture? Check if this is done somewhere

		TextQuadUtils textLabel = new TextQuadUtils(text.trim());
		textLabel.setFont(font);

		Vector2f displaySize = new Vector2f(DisplaySystem.getDisplaySystem().getRenderer().getWidth(), DisplaySystem.getDisplaySystem().getRenderer().getHeight());
		Vector3f ortoCenter = new Vector3f(0.5f * displaySize.x, 0.5f * displaySize.y, 0);

		quad = textLabel.getQuad(ratio);
		quad.getLocalTranslation().set(ortoCenter.x * center.x + ortoCenter.x, ortoCenter.y * center.y + ortoCenter.y, 0 );
		quad.getLocalScale().multLocal(size * displaySize.y);
		//billboard.getLocalTranslation().set(0.0f, 0.0f, 0.0f);
		//quad.getLocalTranslation().set(center.x, center.y, 0.0f);

		quad.setRenderQueueMode(Renderer.QUEUE_ORTHO);

		quad.updateRenderState();
		quad.updateGeometricState(0, true);

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
	 * @return the text
	 */
	public String getText() {
		return text;
	}



	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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



	/**
	 * @return the ratio
	 */
	public float getRatio() {
		return ratio;
	}



	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(float ratio) {
		this.ratio = ratio;
	}



	/**
	 * @return the font
	 */
	public String getFont() {
		return font;
	}



	/**
	 * @param font the font to set
	 */
	public void setFont(String font) {
		this.font = font;
	}



	/**
	 * @return the quad
	 */
	public Quad getQuad() {
		return quad;
	}



}