/**
 * 
 */
package net.peaxy.simulator.image.icon;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author Liang
 *
 */
abstract class RGBBitmap extends Bitmap {
	/**
	 * @param width
	 * @param height
	 * @param bitCount
	 * @param data
	 */
	public RGBBitmap(int width, int height, int bitCount, final byte[] data) {
		super(width, height, bitCount);
		this.readPixels(data, 0);
	}
	
	/* (non-Javadoc)
	 * @see net.peaxy.simulator.image.icon.Bitmap#createImage()
	 */
	@Override
	public Image createImage() {
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, this.width, this.height, this.pixels, 0, this.width);
		return image;
	}
}
