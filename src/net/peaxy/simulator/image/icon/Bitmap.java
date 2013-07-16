/**
 * 
 */
package net.peaxy.simulator.image.icon;

import java.awt.Image;


/**
 * @author Liang
 *
 */
abstract class Bitmap {
	protected int width;
	protected int height;
	protected int bitCount;
	protected final int[] pixels;
	
	public Bitmap(int width, int height, int bitCount) {
		this.width = width;
		this.height = height;
		this.bitCount = bitCount;
		this.pixels = new int[width * height];
	}
	
	protected abstract int readPixels(final byte[] data, int offset);
	public abstract Image createImage();
	
	protected static int getScanLine(int width) {
		int scanLine = width;
		int mod = scanLine % 4;
		if (mod != 0)
			scanLine += 4 - mod;
		return scanLine;
	}
}
