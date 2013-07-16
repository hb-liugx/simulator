/**
 * 
 */
package net.peaxy.simulator.ui;

import net.peaxy.simulator.ui.base.StatusBar;

/**
 * @author Liang
 *
 */
final class MainFrameStatusBar extends StatusBar {
	private static final long serialVersionUID = 5902207903190688224L;
	/**
	 * @param fieldCount
	 * @param heigth
	 */
	public MainFrameStatusBar() {
		super(3, 18, 500, 100, 200);
		this.setText(0, "Ready", 5, 14);
	}
	
	public void info(String s) {
		this.setText(0, s, 5, 14);
	}
	
	public void status(String s) {
		this.setText(1, s, 5, 14);
	}
	
	public void copyright(String s) {
		this.setText(2, s, 5, 14);
	}
}
