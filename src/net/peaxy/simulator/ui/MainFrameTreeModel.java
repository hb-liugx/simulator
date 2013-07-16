/**
 * 
 */
package net.peaxy.simulator.ui;

import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.peaxy.simulator.resource.Resource;

/**
 * @author Liang
 *
 */
final class MainFrameTreeModel implements TreeModel, Serializable {
	private static final long serialVersionUID = -4208360301618558894L;
	private static final String rootName = "Simulator";
	
	private DefaultMutableTreeNode root;
	private EventListenerList listenerList;
	private boolean asksAllowsChildren;
	
	public MainFrameTreeModel() {
		this.listenerList = new EventListenerList();
		
		try {
			TreeNodeData treeNodeData = new TreeNodeData();
			treeNodeData.setLevel(0);
			treeNodeData.setName(rootName);
			treeNodeData.setIcon(Resource.getImageIcon("logo.ico"));
			this.root = new DefaultMutableTreeNode(treeNodeData);
			
			DefaultMutableTreeNode treeNode;
			
			treeNodeData = new TreeNodeData();
			treeNodeData.setLevel(1);
			treeNodeData.setName("RESTful API");
			//treeNodeData.setIcon(Resource.ICON_PRODUCT);
			treeNode = new DefaultMutableTreeNode(treeNodeData);
			this.root.add(treeNode);
			
			treeNodeData = new TreeNodeData();
			treeNodeData.setLevel(1);
			treeNodeData.setName("Device");
			//treeNodeData.setIcon(Resource.ICON_CUSTOMER);
			treeNode =  new DefaultMutableTreeNode(treeNodeData);
			this.root.add(treeNode);
			
			treeNodeData = new TreeNodeData();
			treeNodeData.setLevel(1);
			treeNodeData.setName("Event");
			//treeNodeData.setIcon(Resource.ICON_PRODUCT);
			treeNode = new DefaultMutableTreeNode(treeNodeData);
			this.root.add(treeNode);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private TreeNode[] getPathToRoot(TreeNode aNode) {
        return getPathToRoot(aNode, 0);
    }
    
	private void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
		Object[] listeners = this.listenerList.getListenerList();
		TreeModelEvent e = null;
		for (int i = listeners.length-2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				if (e == null)
					e = new TreeModelEvent(source, path, childIndices, children);
				((TreeModelListener)listeners[i + 1]).treeNodesChanged(e);
			}          
		}
	}
	
	private void nodesChanged(TreeNode node, int[] childIndices) {
	    if(node != null) {
		    if (childIndices != null) {
		    	int cCount = childIndices.length;
		    	if(cCount > 0) {
		    		Object[] cChildren = new Object[cCount];
		    		for(int counter = 0; counter < cCount; counter++)
		    			cChildren[counter] = node.getChildAt(childIndices[counter]);
		    		fireTreeNodesChanged(this, getPathToRoot(node), childIndices, cChildren);
		    	}
		    } else if (node == getRoot()) {
		    	fireTreeNodesChanged(this, getPathToRoot(node), null, null);
		    }
		}
	}
    
	private void nodeChanged(TreeNode node) {
		if(listenerList != null && node != null) {
	        TreeNode parent = node.getParent();
	        if(parent != null) {
	            int anIndex = parent.getIndex(node);
	            if(anIndex != -1) {
	                int[] cIndexs = new int[1];
	                cIndexs[0] = anIndex;
	                nodesChanged(parent, cIndexs);
	            }
	        } else if (node == getRoot()) {
		    	nodesChanged(node, null);
		    }
		}
    }
	
	private TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
        TreeNode[] retNodes;
        if(aNode == null) {
            if(depth == 0)
                return null;
               retNodes = new TreeNode[depth];
        } else {
            depth++;
            if(aNode == root)
                retNodes = new TreeNode[depth];
            else
                retNodes = getPathToRoot(aNode.getParent(), depth);
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void addTreeModelListener(TreeModelListener l) {
		this.listenerList.add(TreeModelListener.class, l);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index) {
		return ((TreeNode)parent).getChildAt(index);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {
		return ((TreeNode)parent).getChildCount();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {
		if(parent == null || child == null)
            return -1;
        return ((TreeNode)parent).getIndex((TreeNode)child);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot() {
		return this.root;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {
		if(this.asksAllowsChildren)
            return !((TreeNode)node).getAllowsChildren();
        return ((TreeNode)node).isLeaf();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void removeTreeModelListener(TreeModelListener l) {
		this.listenerList.remove(TreeModelListener.class, l);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
		MutableTreeNode aNode = (MutableTreeNode)path.getLastPathComponent();
        aNode.setUserObject(newValue);
        nodeChanged(aNode);
	}

	public final class TreeNodeData implements Serializable {
		private static final long serialVersionUID = -3749079079349378035L;
		
		private int id;
		private int level;
		private String name;
		private ImageIcon icon;
		
		public int getId() {
			return this.id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public int getLevel() {
			return this.level;
		}
		
		public void setLevel(int level) {
			this.level = level;
		}
		
		public String getName() {
			return this.name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public ImageIcon getIcon() {
			return this.icon;
		}
		
		public void setIcon(ImageIcon icon) {
			this.icon = icon;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
}
