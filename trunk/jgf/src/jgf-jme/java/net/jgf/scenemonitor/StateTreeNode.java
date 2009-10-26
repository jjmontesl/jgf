package net.jgf.scenemonitor;

import javax.swing.Icon;

import net.jgf.core.state.State;
import net.jgf.core.state.StateNode;

import com.acarter.composabletree.ComposableTreeNode;
import com.acarter.jmejtree.JMEJTreeIcon;

public class StateTreeNode implements ComposableTreeNode {

		StateNode<?> node = null;
	
		public StateTreeNode(Object node) {
			//this.node = node;
		}
	
        public Object getChild(Object parent, int index) {
                
        	Object result = null;
            if(parent instanceof StateNode<?>) {
               result = ((StateNode<?>)parent).children().get(index);
            } 
            
            return result;
                
        }

        public int getChildCount(Object parent) {
        	int result = 0;
            if(parent instanceof StateNode<?>) {
               result = ((StateNode<?>)parent).children().size();
            } 
            return result;
        }

        public int getChildIndex(Object parent, Object child) {
        	
        	int result = -1;
            if(parent instanceof StateNode<?>) {
               result = ((StateNode<?>)parent).children().indexOf(child);
            } 
            return result;
        	
        }

        public Icon getIcon(Object node) {
                return JMEJTreeIcon.getIcon().node;
        }

        public Class<?> getNodeClassType() {
                return State.class;
        }

        public String getNodeText(Object node, boolean selected, boolean expanded,
                        boolean leaf, int row, boolean hasFocus) {
                return ((State)node).getId() + " (type = " + node.getClass().getSimpleName() + ")";
        }

        public String getNodeToolTipText(Object node, boolean selected,
                        boolean expanded, boolean leaf, int row, boolean hasFocus) {
        	return ((State)node).getId() + " (type = " + node.getClass().getSimpleName() + ")";
        }

        public Object getParent(Object child) {
                return null;
        }

}
