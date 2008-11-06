
package net.jgf.jme.scene;

import java.util.Hashtable;
import java.util.List;

import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.jme.camera.CameraController;
import net.jgf.jme.camera.CameraControllerSet;
import net.jgf.jme.camera.HasCameras;
import net.jgf.jme.scene.sky.HasSky;
import net.jgf.jme.scene.sky.Sky;
import net.jgf.refs.HasReferences;
import net.jgf.refs.References;
import net.jgf.system.System;

import org.apache.log4j.Logger;

import com.jme.scene.state.RenderState;


/**
 */
@Configurable
public class DefaultJmeScene extends JmeScene implements HasSky, HasCameras, HasReferences {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(DefaultJmeScene.class);

	/**
	 * Reusable render states
	 */
	protected Hashtable<String, RenderState> commonRenderStates;

	/**
	 * Scene reference nodes, which can be used as location and rotation references.
	 */
	protected References references;

	protected CameraControllerSet cameras;

	/**
	 * Scene data (spawn points, missions, game modes, etc...)
	 */
	protected DefaultLevelData levelData;

	protected Sky sky;

	public DefaultJmeScene() {

		// Reused render states
		commonRenderStates = new Hashtable<String, RenderState>();

		// Reference nodes
		references = new References();

		cameras = new CameraControllerSet();

		sky = new Sky();

	}

	public DefaultJmeScene(String id) {

		this();
		this.setId(id);

	}

	/**
	 * @return the commonRenderStates
	 */
	public Hashtable<String, RenderState> getCommonRenderStates() {
		return commonRenderStates;
	}

	@Override
	public Sky getSky() {
		return sky;
	}

	@Override
	public CameraControllerSet getCameraControllers() {
		return cameras;
	}

	@Override
	public References getReferences() {
		return references;
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		List<CameraController> controllers = ConfigurableFactory.newListFromConfig(config, configPath + "/camera", CameraController.class);
		for (CameraController camera : controllers) {
			this.getCameraControllers().addCameraController(camera);
			System.getDirectory().addObject(camera.getId(), camera);
		}


	}

}
