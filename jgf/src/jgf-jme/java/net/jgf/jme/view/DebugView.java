
package net.jgf.jme.view;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.jme.scene.JmeScene;
import net.jgf.scene.SceneManager;
import net.jgf.system.System;
import net.jgf.util.system.SystemInfoService;
import net.jgf.view.BaseViewState;

import org.apache.log4j.Logger;

import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.Debugger;
import com.jmex.game.state.GameStateManager;
import com.jmex.game.state.StatisticsGameState;

/**
 *
 * @author Matthew D. Hicks
 */
@Configurable
public class DebugView extends BaseViewState {

    private static final Logger logger = Logger.getLogger(DebugView.class);

  	/**
  	 *
  	 */
  	protected String sceneManagerRef;

  	protected Node rootNode;

    protected WireframeState wireState;
    protected LightState lightState;

    protected boolean showBounds = false;
    protected boolean showDepth = false;
    protected boolean showNormals = false;

    @Override
		public void load() {

    	super.load();

    	JmeScene scene= (JmeScene) System.getDirectory().getObjectAs(sceneManagerRef, SceneManager.class).getScene();
    	rootNode = scene.getRootNode();

      // create a statistics game state
      GameStateManager.getInstance().attachChild(new StatisticsGameState("stats", 1f, 0.25f, 0.75f, true));

      // Create a wirestate to toggle on and off. Starts disabled with default
      // width of 1 pixel.
      wireState = DisplaySystem.getDisplaySystem().getRenderer()
              .createWireframeState();
      wireState.setEnabled(false);
      rootNode.setRenderState(wireState);

      // Create ZBuffer for depth
      ZBufferState zbs = DisplaySystem.getDisplaySystem().getRenderer()
              .createZBufferState();
      zbs.setEnabled(true);
      zbs.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
      rootNode.setRenderState(zbs);

      // Lighting
      /** Set up a basic, default light. */
      PointLight light = new PointLight();
      light.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
      light.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
      light.setLocation( new Vector3f( 100, 100, 100 ) );
      light.setEnabled( true );


      // Initial InputHandler
      initKeyBindings();

      // Finish up
      rootNode.updateRenderState();
      rootNode.updateWorldBound();
      rootNode.updateGeometricState(0.0f, true);
    }


    private void initKeyBindings() {

        KeyBindingManager.getKeyBindingManager().set("toggle_wire",  KeyInput.KEY_T);

        KeyBindingManager.getKeyBindingManager().set("toggle_lights", KeyInput.KEY_L);

        KeyBindingManager.getKeyBindingManager().set("toggle_bounds", KeyInput.KEY_B);

        KeyBindingManager.getKeyBindingManager().set("toggle_normals", KeyInput.KEY_N);

        // TODO: Screenshot should be another ViewState
        KeyBindingManager.getKeyBindingManager().set("screen_shot", KeyInput.KEY_F1);

        KeyBindingManager.getKeyBindingManager().set("parallel_projection", KeyInput.KEY_F2);

        KeyBindingManager.getKeyBindingManager().set("toggle_depth", KeyInput.KEY_F3);

        KeyBindingManager.getKeyBindingManager().set("mem_report", KeyInput.KEY_R);

        KeyBindingManager.getKeyBindingManager().set("toggle_mouse", KeyInput.KEY_M);
    }

    @Override
		public void update(float tpf) {

    	super.update(tpf);

      // Update the geometric state of the rootNode
      rootNode.updateGeometricState(tpf, true);

      /** If toggle_wire is a valid command (via key T), change wirestates. */
      if (KeyBindingManager.getKeyBindingManager().isValidCommand(
              "toggle_wire", false)) {
          wireState.setEnabled(!wireState.isEnabled());
          rootNode.updateRenderState();
      }
      /** If toggle_bounds is a valid command (via key B), change bounds. */
      if (KeyBindingManager.getKeyBindingManager().isValidCommand(
              "toggle_bounds", false)) {
          showBounds = !showBounds;
      }
      /** If toggle_depth is a valid command (via key F3), change depth. */
      if (KeyBindingManager.getKeyBindingManager().isValidCommand(
              "toggle_depth", false)) {
          showDepth = !showDepth;
      }

      if (KeyBindingManager.getKeyBindingManager().isValidCommand(
              "toggle_normals", false)) {
          showNormals = !showNormals;
      }
      // TODO: Screenshot facility should be another ViewState
      if (KeyBindingManager.getKeyBindingManager().isValidCommand(
              "screen_shot", false)) {
          DisplaySystem.getDisplaySystem().getRenderer().takeScreenShot(
                  "SimpleGameScreenShot");
      }
      if (KeyBindingManager.getKeyBindingManager().isValidCommand(
              "parallel_projection", false)) {
          if (DisplaySystem.getDisplaySystem().getRenderer().getCamera()
                  .isParallelProjection()) {
              cameraPerspective();
          } else {
              cameraParallel();
          }
      }
      if (KeyBindingManager.getKeyBindingManager().isValidCommand(
              "mem_report", false)) {
          logger.info(SystemInfoService.getSystemInfo());
      }
      if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_mouse", false)) {
            MouseInput.get().setCursorVisible(!MouseInput.get().isCursorVisible());
            logger.info("Cursor Visibility set to " + MouseInput.get().isCursorVisible());
        }

    }

    // TODO: If allows change of mode, save camera previous values
    protected void cameraPerspective() {
        DisplaySystem display = DisplaySystem.getDisplaySystem();
        Camera cam = display.getRenderer().getCamera();
        cam.setFrustumPerspective(45.0f, (float) display.getWidth()
                / (float) display.getHeight(), 1, 1000);
        cam.setParallelProjection(false);
        cam.update();
    }

    protected void cameraParallel() {
        DisplaySystem display = DisplaySystem.getDisplaySystem();
        Camera cam = display.getRenderer().getCamera();
        cam.setParallelProjection(true);
        // TODO: the camaraPararell should be parameterized in Debug config
        // TODO: the previous camera data should be saved
        // TODO: this needs to be combine with the camera settings??
        float aspect = (float) display.getWidth() / display.getHeight();
        cam.setFrustum(-100.0f, 1000.0f, -50.0f * aspect, 50.0f * aspect, -50.0f, 50.0f);
        //cam.setFrustum(-100.0f, 1000.0f, -10.0f * aspect, 10.0f * aspect, -10.0f, 10.0f);
        cam.update();
    }

    @Override
		public void render(float tpf) {

    	super.render(tpf);

      if (showBounds) {
          Debugger.drawBounds(rootNode, DisplaySystem.getDisplaySystem()
                  .getRenderer(), true);
      }

      if (showNormals) {
          Debugger.drawNormals(rootNode, DisplaySystem.getDisplaySystem()
                  .getRenderer());
      }

      if (showDepth) {
          DisplaySystem.getDisplaySystem().getRenderer().renderQueue();
          Debugger.drawBuffer(Texture.RenderToTextureType.Depth, Debugger.NORTHEAST,
                  DisplaySystem.getDisplaySystem().getRenderer());
      }

      /*
    	if (System.getSettings().getRender().isDrawPhysics()) {
  			// We can only show the physic debugger if the level is a Physic Scene
  			if (System.getLevel() instanceof DefaultPhysicLevel)
  			PhysicsDebugger.drawPhysics(((DefaultPhysicLevel) System.getLevel()).getPhysicsSpace(), DisplaySystem.getDisplaySystem().getRenderer());
  		}
    	*/

    }

  	/**
  	 * Configures this object from Config.
  	 */
  	@Override
		public void readConfig(Config config, String configPath) {

  		super.readConfig(config, configPath);
  		this.sceneManagerRef = config.getString(configPath + "/sceneManager/@ref");

  	}

}
