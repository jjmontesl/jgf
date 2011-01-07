
package net.jgf.jme.view;



import net.jgf.config.Config;
import net.jgf.config.ConfigException;
import net.jgf.config.Configurable;
import net.jgf.jme.config.JmeConfigHelper;
import net.jgf.view.BaseViewStateNode;

import org.apache.log4j.Logger;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;
import com.jmex.effects.transients.Fader;
import com.jmex.effects.transients.Fader.FadeMode;

/**
 *
 * <p>Note that the FaderViewNode fades to black (or other color) everything that has already been
 * drawn. Therefore this node cannot be used to fade only certain elements on the screen if something
 * else has already been rendered (in order to do so, those other elements would provide their
 * own alpha blending faders). Think of this node as a "full screen fader".</p>
 * <p>This node by default forces render queue processing, so fading is processed
 * immediately and other geometry can be drawn over the faded stencil buffer.</p>
 */
@Configurable
// TODO: Allow to customize the "disableOnFinish" "unloadOnFinish"...
public class FaderViewNode extends BaseViewStateNode {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(FaderViewNode.class);

	protected Node rootNode;

	protected float fadeInTime = 0.0f;

	protected float fadeOutTime = 0.0f;

	protected float autoFadeOutTime = -1.0f;

	protected boolean allowKeyToSkip = false;

	protected boolean deactivateOnFinish = true;

	protected boolean unloadOnFinish = false;

	protected ColorRGBA color = ColorRGBA.black;

	protected Fader fader;

	protected FadeMode fadeMode;

	protected float timeElapsed;

	public FaderViewNode() {
		rootNode = new Node();
	}

	/* (non-Javadoc)
	 * @see net.jgf.view.BaseViewState#load()
	 */
	@Override
	public void doLoad() {

		super.doLoad();

		rootNode.detachAllChildren();

		// Now we'll create our Fader and add
		fader = new Fader("Fader-" + this.getId(), DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem.getDisplaySystem().getHeight(), color, fadeInTime);
		fader.setAlpha(1.0f);
		rootNode.attachChild(fader);

    rootNode.attachChild(fader);
    rootNode.updateRenderState();
    rootNode.updateModelBound();

	}



	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseStateNode#unload()
	 */
	@Override
	public void doUnload() {
		super.doUnload();
		rootNode.detachAllChildren();
		fader = null;
	}

	/**
	 * Scene geometry update.
	 */
	@Override
	public void update(float tpf) {

			super.update(tpf);
			rootNode.updateGeometricState(Timer.getTimer().getTimePerFrame(), true);


			if ((fadeMode == FadeMode.FadeIn) && (! (fader.getAlpha() > 0.0f))) {
				timeElapsed += tpf;
				if ((autoFadeOutTime > 0.0f)&&(timeElapsed > autoFadeOutTime)) {
					this.fadeOut();
				}
			}

			if ((fadeMode == FadeMode.FadeOut) && (!(fader.getAlpha() < 1.0f))) {
				if (isDeactivateOnFinish()) this.deactivate();
				if (isUnloadOnFinish()) this.doUnload();
			}

	}

	/**
	 * Fades out from current fading level.
	 */
	public void fadeOut() {
		if (fadeOutTime < 0) {
			throw new ConfigException("Invalid value for property 'fadeInTime' of " + this + " (it must be >= 0)");
		}
		fadeMode = FadeMode.FadeOut;
		fader.setMode(fadeMode);
		fader.setFadeTimeInSeconds(fadeOutTime);
	}

	/**
	 * Fades in from current fading level.
	 */
	public void fadeIn() {
		if (fadeInTime < 0) {
			throw new ConfigException("Invalid value for property 'fadeInTime' of " + this + " (it must be >= 0)");
		}
		timeElapsed = 0.0f;
		fadeMode = FadeMode.FadeIn;
		fader.setMode(fadeMode);
		fader.setFadeTimeInSeconds(fadeInTime);
	}

	/* (non-Javadoc)
	 * @see net.jgf.core.state.BaseStateNode#activate()
	 */
	@Override
	public void doActivate() {
		super.doActivate();
		this.fadeIn();
	}

	/**
	 * Draws the level (and debug info, if needed).
	 * Note that the wireframe state is activated from the Commands
	 * class.
	 */
	@Override
	public void render(float tpf) {

		super.render(tpf);

		//DisplaySystem display = DisplaySystem.getDisplaySystem();
		//display.getRenderer().getCamera().update();
		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);
		// TODO: Rendering the queue should be optional: also double check consequences
		DisplaySystem.getDisplaySystem().getRenderer().renderQueue();

	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		this.setAllowKeyToSkip(config.getBoolean(configPath + "/allowKeyToSkip", isAllowKeyToSkip()));
		this.setDeactivateOnFinish(config.getBoolean(configPath + "/deactivateOnFinish", isDeactivateOnFinish()));
		this.setUnloadOnFinish(config.getBoolean(configPath + "/unloadOnFinish", isUnloadOnFinish()));
		this.setAutoFadeOutTime(config.getFloat(configPath + "/autoFadeOutTime", getAutoFadeOutTime()));
		this.setFadeInTime(config.getFloat(configPath + "/fadeInTime", getFadeInTime()));
		this.setFadeOutTime(config.getFloat(configPath + "/fadeOutTime", getFadeOutTime()));
		this.setColor(JmeConfigHelper.getColor(config, configPath + "/color", getColor()));


	}

	/**
	 * @return the fadeInTime
	 */
	public float getFadeInTime() {
		return fadeInTime;
	}

	/**
	 * @param fadeInTime the fadeInTime to set
	 */
	public void setFadeInTime(float fadeInTime) {
		this.fadeInTime = fadeInTime;
	}



	/**
	 * @return the unloadOnFinish
	 */
	public boolean isUnloadOnFinish() {
		return unloadOnFinish;
	}

	/**
	 * @param unloadOnFinish the unloadOnFinish to set
	 */
	public void setUnloadOnFinish(boolean unloadOnFinish) {
		this.unloadOnFinish = unloadOnFinish;
	}

	/**
	 * @return the fadeOutTime
	 */
	public float getFadeOutTime() {
		return fadeOutTime;
	}

	/**
	 * @param fadeOutTime the fadeOutTime to set
	 */
	public void setFadeOutTime(float fadeOutTime) {
		this.fadeOutTime = fadeOutTime;
	}

	/**
	 * @return the autoFadeOutTime
	 */
	public float getAutoFadeOutTime() {
		return autoFadeOutTime;
	}

	/**
	 * @param autoFadeOutTime the autoFadeOutTime to set
	 */
	public void setAutoFadeOutTime(float autoFadeOutTime) {
		this.autoFadeOutTime = autoFadeOutTime;
	}

	/**
	 * @return the allowKeyToSkip
	 */
	public boolean isAllowKeyToSkip() {
		return allowKeyToSkip;
	}

	/**
	 * @param allowKeyToSkip the allowKeyToSkip to set
	 */
	public void setAllowKeyToSkip(boolean allowKeyToSkip) {
		this.allowKeyToSkip = allowKeyToSkip;
	}

	/**
	 * @return the color
	 */
	public ColorRGBA getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(ColorRGBA color) {
		this.color = color;
	}

	/**
	 * @return the deactivateOnFinish
	 */
	public boolean isDeactivateOnFinish() {
		return deactivateOnFinish;
	}

	/**
	 * @param deactivateOnFinish the deactivateOnFinish to set
	 */
	public void setDeactivateOnFinish(boolean deactivateOnFinish) {
		this.deactivateOnFinish = deactivateOnFinish;
	}





}
