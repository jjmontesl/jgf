
package net.jgf.jme.scene;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.jgf.camera.CameraController;
import net.jgf.camera.CameraControllerSet;
import net.jgf.camera.HasCameras;
import net.jgf.config.Config;
import net.jgf.config.Configurable;
import net.jgf.config.ConfigurableFactory;
import net.jgf.jme.scene.sky.HasSky;
import net.jgf.jme.scene.sky.Sky;
import net.jgf.refs.HasReferences;
import net.jgf.refs.ReferenceSet;
import net.jgf.system.Jgf;

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
	protected ReferenceSet references;

	protected CameraControllerSet cameras;

	protected Sky sky;

	protected Map<String, Object> properties;

	public DefaultJmeScene() {

		// Reused render states
		commonRenderStates = new Hashtable<String, RenderState>();

		// Reference nodes
		references = new ReferenceSet();

		cameras = new CameraControllerSet();

		properties = new HashMap<String, Object>();

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
	public ReferenceSet getReferences() {
		return references;
	}

	/**
	 * Configures this object from Config.
	 */
	@Override
	public void readConfig(Config config, String configPath) {

		super.readConfig(config, configPath);

		List<CameraController> controllers = ConfigurableFactory.newListFromConfig(config, configPath + "/cameras/camera", CameraController.class);
		for (CameraController camera : controllers) {
			this.getCameraControllers().addCameraController(camera);
			Jgf.getDirectory().addObject(camera.getId(), camera);
		}

	}



	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}



}
