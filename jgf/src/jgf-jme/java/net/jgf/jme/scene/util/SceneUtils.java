package net.jgf.jme.scene.util;

import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.system.Application;

import org.apache.log4j.Logger;

import com.jme.image.Texture;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.FogState.DensityFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;

// TODO: Organize and if needed relocate this to appropriate (unimportant) helpers ???
public class SceneUtils {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(Application.class);

	private static final String DEFAULT_SKYBOX_SUFFIX = ".png";

	/**
	 * Sets up default lighting
	 */
	public static LightState createDefaultLightState() {

    DirectionalLight light = new DirectionalLight();
    light.setDirection (new Vector3f(-1.0f, -0.3f, -0.5f).normalizeLocal());
    light.setAmbient(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
    light.setDiffuse(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
    light.setSpecular(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
    light.setConstant(0.0f);
    light.setLinear(0.0f);
    light.setQuadratic(0.0f);
    light.setShadowCaster(true);
    light.setEnabled(true);



    DirectionalLight light2 = new DirectionalLight();
    light2.setDirection (new Vector3f(-8.3f, -0.6f, 0.9f));
    light2.setAmbient(new ColorRGBA(0.4f, 0.4f, 0.9f, 0.7f));
    light2.setDiffuse(new ColorRGBA(0.4f, 0.4f, 0.9f, 0.7f));
    light2.setSpecular(new ColorRGBA(0.4f, 0.4f, 0.9f, 0.7f));
    light2.setConstant(0.0f);
    light2.setLinear(0.0f);
    light2.setQuadratic(0.0f);
    light2.setShadowCaster(true);
    light2.setEnabled(true);


    /*
    PointLight pl = new PointLight();
    pl.setLightMask(0);
    pl.setEnabled(true);
    pl.setDiffuse(new ColorRGBA(.7f, .7f, .7f, 1.0f));
    pl.setAmbient(new ColorRGBA(.25f, .25f, .25f, .25f));
    pl.setLocation(new Vector3f(-30,30,-80));
    pl.setSpecular(new ColorRGBA(0.2f, 0.2f, 0.3f, 0.0f));
    pl.setConstant(100.0f);
    pl.setLinear(100.0f);
    pl.setQuadratic(100.0f);
    pl.setShadowCaster(true);
    */



    LightState ls = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
    ls.detachAll();
    ls.attach(light);
    ls.attach(light2);
    //ls.attach(pl);
    ls.setGlobalAmbient(new ColorRGBA(0.0f, 0.0f, 0.0f, 0.0f));
    ls.setTwoSidedLighting(false);
    return ls;

	}

	/**
	 * Creates some typical render states
	 */
	public static void createCommonRenderStates(DefaultJmeScene scene) {

		// TODO: Use an enumeration for these and the key for the commonRenderStates in DefaultJMEScene

		// Create ZBuffer for depth
		ZBufferState zb = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zb.setEnabled(true);
		zb.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		scene.getCommonRenderStates().put("zBuffer", zb);

		// ZBuffer read only
		ZBufferState zs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zs.setWritable(false);
		zs.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		scene.getCommonRenderStates().put("zBufferReadOnly", zs);

		// ZBuffer disabled ??
		ZBufferState zd = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zd.setEnabled(false);
		scene.getCommonRenderStates().put("zBufferDisabled", zd);

		// Cull back sides
		CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		cs.setCullFace(CullState.Face.Back);
		scene.getCommonRenderStates().put("cullBack", cs);

		// Cull none
		CullState csn = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		csn.setCullFace(CullState.Face.None);
		scene.getCommonRenderStates().put("cullNone", csn);

		// Alpha state
		BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		as.setBlendEnabled(true);
		scene.getCommonRenderStates().put("alpha", as);

		// Fog state
		FogState fs = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
		// TODO: move to createFogState(...)
		/*
		fs.setEnabled(scene.getSceneInfo().isFogEnabled());
		fs.setColor(scene.getSceneInfo().getFogColor());
		fs.setEnd(scene.getSceneInfo().getFogEnd());
		fs.setStart(scene.getSceneInfo().getFogStart());
		*/
		fs.setDensityFunction(DensityFunction.Linear);
		scene.getCommonRenderStates().put("fog", fs);

		// Fog state disabled
		FogState fsd = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
		fsd.setEnabled(false);
		scene.getCommonRenderStates().put("fogDisabled", fsd);
	}

	public static Node setupSkyBox(String prefix) {
		return setupSkyBox(prefix, null);
	}

	/**
	 * Sets up a Skybox
	 */
	// TODO: There should be possible to configure the constants below (positiveZ...)
	public static Node setupSkyBox(String prefix, String suffix) {

		if (suffix == null) suffix = DEFAULT_SKYBOX_SUFFIX;

    // Create the node
    Skybox skybox = new Skybox("skybox", 400, 400, 400);

    Texture north = TextureManager.loadTexture(
    		ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, prefix + "positiveZ" + suffix),
    		Texture.MinificationFilter.NearestNeighborLinearMipMap,
        Texture.MagnificationFilter.NearestNeighbor);
    Texture south = TextureManager.loadTexture(
    		ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, prefix + "negativeZ" + suffix),
    		Texture.MinificationFilter.NearestNeighborLinearMipMap,
        Texture.MagnificationFilter.NearestNeighbor);
    Texture east = TextureManager.loadTexture(
    		ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, prefix + "positiveX" + suffix),
    		Texture.MinificationFilter.NearestNeighborLinearMipMap,
        Texture.MagnificationFilter.NearestNeighbor);
    Texture west = TextureManager.loadTexture(
    		ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, prefix + "negativeX" + suffix),
    		Texture.MinificationFilter.NearestNeighborLinearMipMap,
        Texture.MagnificationFilter.NearestNeighbor);
    Texture up = TextureManager.loadTexture(
    		ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, prefix + "positiveY" + suffix),
    		Texture.MinificationFilter.NearestNeighborLinearMipMap,
        Texture.MagnificationFilter.NearestNeighbor);
    Texture down = TextureManager.loadTexture(
    		ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, prefix + "negativeY" + suffix),
    		Texture.MinificationFilter.NearestNeighborLinearMipMap,
        Texture.MagnificationFilter.NearestNeighbor);

    skybox.setTexture(Skybox.Face.North, north);
    skybox.setTexture(Skybox.Face.West, west);
    skybox.setTexture(Skybox.Face.South, south);
    skybox.setTexture(Skybox.Face.East, east);
    skybox.setTexture(Skybox.Face.Up, up);
    skybox.setTexture(Skybox.Face.Down, down);

    skybox.preloadTextures();

    return skybox;
	}

}
