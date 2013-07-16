/**
 * 
 */
package net.peaxy.simulator.ui.base;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * @author Liang
 *
 */
public class ListView extends JScrollPane {
	private static final long serialVersionUID = 7347191867349247630L;

	protected JTable table;
	
	public ListView() {
		this.table = new JTable();
		this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setViewportView(this.table);
	}
}
