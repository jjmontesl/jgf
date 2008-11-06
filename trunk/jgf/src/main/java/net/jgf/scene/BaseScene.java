
package net.jgf.scene;

import java.util.Date;

import net.jgf.core.component.BaseComponent;

import org.apache.log4j.Logger;


/**
 */
public abstract class BaseScene extends BaseComponent implements Scene {

	/**
	 * Class logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BaseScene.class);

	/**
	 * Map identifier (its short name).
	 */
	protected String name;

	/**
	 * Map title.
	 */
	protected String title;

	/**
	 * Map's creator message or subtitle.
	 */
	protected String message;

	/**
	 * Date when this level was constructed.
	 */
	protected Date spawnDate;

	/**
	 * Default constructor.
	 */
	public BaseScene() {

		// General info
		this.spawnDate = new Date();

	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
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
