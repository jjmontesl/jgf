
package net.jgf.jme.model.util;

import java.io.IOException;

import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.Savable;

public class TransientSavable<E> implements Savable {

	protected E content;

	public TransientSavable() {
		super();
	}

	public TransientSavable(E content) {
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
	public Class<?> getClassTag() {
      return this.getClass();
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		// Nothing to do
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		// Nothing to do
	}



}
