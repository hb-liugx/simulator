/**
 * 
 */
package net.peaxy.simulator.ui.base;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

/**
 * @author Liang
 *
 */
public class TreeView extends JScrollPane {
	private static final long serialVersionUID = 6162945280075702534L;

	private JTree tree;

	public TreeView() {
		this.tree = new JTree();
		this.setViewportView(this.tree);
	}
	
	public void setModel(TreeModel newModel) {
		this.tree.setModel(newModel);
	}
	
	public void setCellRenderer(TreeCellRenderer treeCellRenderer) {
		this.tree.setCellRenderer(treeCellRenderer);
	}
}
