/**
 * 
 */
package net.peaxy.simulator.ui;

import net.peaxy.simulator.resource.Resource;
import net.peaxy.simulator.ui.base.MenuBar;
import net.peaxy.simulator.ui.base.MenuBarListener;

/**
 * @author Liang
 *
 */
final class MainFrameMenuBar extends MenuBar {
	private static final long serialVersionUID = 861695605724154316L;

	public MainFrameMenuBar(MenuBarListener listener) throws Throwable {
		super(listener, Resource.getResourceURL("menu.resource.xml"));
	}
}
