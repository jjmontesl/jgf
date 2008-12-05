package net.jgf.logic.action;

import net.jgf.core.component.Component;
import net.jgf.logic.LogicState;

public interface LogicAction extends LogicState {

	public void perform(String action);

}