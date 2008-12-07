
package net.jgf.jme.model.util;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.Savable;

public class FakeSavable<E> implements Savable {

	private static final Logger logger = Logger.getLogger(FakeSavable.class);

	protected E content;


	public FakeSavable() {
		super();
	}

	public FakeSavable(E content) {
		super();
		this.content = content;
	}


	/**
	 * @return the content
	 */
	public E getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(E content) {
		this.content = content;
	}


	@Override
	public Class getClassTag() {
		return null;
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		throw new IOException(this + " cannot be read as it is a fake Savable");
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		throw new IOException(this + " cannot be saved as it is a fake Savable");
	}



}
