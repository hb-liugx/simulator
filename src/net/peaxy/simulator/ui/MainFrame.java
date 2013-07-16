/**
 * 
 */
package net.peaxy.simulator.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import net.peaxy.simulator.resource.Resource;
import net.peaxy.simulator.ui.base.MenuBarListener;

/**
 * @author Liang
 *
 */
public final class MainFrame extends JFrame implements MenuBarListener {
	private static final long serialVersionUID = 272929763857324593L;
	private static final String title = "Peaxy Simulator";
	private static final String copyright = "Copyright@2012, Peaxy, Inc. All rights reserved.";
	public static final String windowClosedCommand = "windowClosedCommand";
	
	private final MainFrameMenuBar menuBar;
	private final MainFrameToolBar toolbar;
	private final MainFrameTreeView treeView;
	private final MainFrameStatusBar statusBar;
	private final MainFrameMenuActionListener actionListener;
	
	public MainFrame(MainFrameMenuActionListener actionListener) throws Throwable {
		this.setIconImage(Resource.getImage("logo.ico"));
		this.setTitle(title);
		
		this.menuBar = new MainFrameMenuBar(this);
		this.setJMenuBar(this.menuBar);
		
		Container container = this.getContentPane();
		this.toolbar = new MainFrameToolBar();
		container.add(this.toolbar, BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.treeView = new MainFrameTreeView();
		splitPane.setLeftComponent(this.treeView);
		container.add(splitPane, BorderLayout.CENTER);
		
		this.statusBar = new MainFrameStatusBar();
		this.statusBar.copyright(copyright);
		container.add(this.statusBar, BorderLayout.SOUTH);
		
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				MainFrame.this.actionListener.onActionPerformed(windowClosedCommand);
			}
		});
		this.actionListener = actionListener;
	}

	/* (non-Javadoc)
	 * @see net.peaxy.simulator.ui.MainFrameMenuBarTipListener#onTip(java.lang.String)
	 */
	public void onTip(String tip) {
		this.statusBar.info(tip);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o instanceof JMenuItem)
			this.actionListener.onActionPerformed(((JMenuItem)o).getName());
	}
}
