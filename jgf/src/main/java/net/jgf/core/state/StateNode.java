package net.jgf.core.state;


public interface StateNode<T extends State> {

	public void attachChild(T logicState);

}