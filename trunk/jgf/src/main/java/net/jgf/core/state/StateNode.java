package net.jgf.core.state;

import java.util.List;

import net.jgf.core.service.ServiceException;


public interface StateNode<T extends State> {

	public void attachChild(T logicState);
	
	public void dettachChild(T state);

	public boolean containsChild(T state);

	public List<T> children();

}