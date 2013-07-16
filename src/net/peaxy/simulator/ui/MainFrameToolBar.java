/**
 * 
 */
package net.peaxy.simulator.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import net.peaxy.simulator.resource.Resource;

/**
 * @author Liang
 *
 */
final class MainFrameToolBar extends JPanel implements ActionListener {
	private static final long serialVersionUID = -5862515880865138614L;

	private JToolBar toolBar;
	
	public MainFrameToolBar() {
		this.setPreferredSize(new Dimension(0, 28));
		this.setLayout(null);
		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);
		this.toolBar.setBounds(2, 2, 1280, 26);
		this.addButtons();
		this.add(this.toolBar);
	}
	

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("exit")) {
			System.exit(0);
		}
	}

	private void addButtons() {
		JButton button = new JButton();
		try {
			button.setIcon(Resource.getImageIcon("exit.gif"));
		} catch (Throwable t) {
			t.printStackTrace();
		}
		button.setActionCommand("exit");
		button.setToolTipText("to exit the application");
		button.addActionListener(this);
		this.toolBar.add(button);
	}
}
