package net.jgf.loader;

import net.jgf.core.component.Component;

public interface Loader<E> extends Component {

	public E load(E base, LoadProperties properties);

}