/**
 * 
 */
package net.peaxy.simulator.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import net.peaxy.simulator.ui.base.TreeView;

/**
 * @author Liang
 *
 */
final class MainFrameTreeView extends TreeView {
	private static final long serialVersionUID = -2058830124172602836L;
	
	public MainFrameTreeView() {
		this.setPreferredSize(new Dimension(200, 0));
		this.setCellRenderer(new MainFrameTreeViewCellRenderer());
		this.setModel(new MainFrameTreeModel());
	}

	private class MainFrameTreeViewCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -4117553705966704365L;
		/*  (non-Javadoc)  
	     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)  
	     */
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			MainFrameTreeModel.TreeNodeData treeNodeData = (MainFrameTreeModel.TreeNodeData)node.getUserObject();
			this.setIcon(treeNodeData.getIcon());
			return this;
		}
	}
}
