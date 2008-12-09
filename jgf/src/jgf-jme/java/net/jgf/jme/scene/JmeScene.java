
package net.jgf.jme.scene;

import net.jgf.scene.BaseScene;

import org.apache.log4j.Logger;

import com.jme.scene.Node;


/**
 */
public class JmeScene extends BaseScene {

	/**
	 * Class logger
	 */
	private static final Logger logger = Logger.getLogger(JmeScene.class);

	/**
	 * The scene root Node.
	 */
	protected Node rootNode;

	/**
	 * Default constructor.
	 */
	public JmeScene() {

		// Geometric space
		rootNode = new Node("sceneRoot");

	}

	/**
	 * @return Returns the message.
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns the title.
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the rootNode.
	 */
	public Node getRootNode() {
		return rootNode;
	}

	/**
	 * Updates the level (making all calculations needed that
	 * result in the next level state)
	 */
	public void update(float tpf) {

		// TODO: Note that here is where the updateGeometricState call is done. Document!!!
		// TODO: Is this correct? doing this here? see SceneRenderView!
		// TODO: What about other things (skybox, etc...) that need to be updated?
    rootNode.updateGeometricState( tpf, true );

	}

	/* (non-Javadoc)
	 * @see net.jgf.core.component.Component#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.jgf.scene.Scene#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
