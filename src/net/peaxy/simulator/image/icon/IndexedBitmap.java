/**
 * 
 */
package net.peaxy.simulator.image.icon;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author Liang
 *
 */
abstract class IndexedBitmap extends Bitmap {
	protected final Color[] palette;
	private final int[] mask;
	
	public IndexedBitmap(int width, int height, int bitCount, final byte[] data) {
		super(width, height, bitCount);
		this.palette = new Color[1 << this.bitCount];
		this.mask = new int[width * height];
		int offset = this.readPalette(data);
		offset = this.readPixels(data, offset);
		readMask(data, offset, this.mask, width, height);
	}
	
	@Override
	public Image createImage() {
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		int index = 0;
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				index = i * this.width + j;
				int rgb = this.palette[this.pixels[index]].getRGB();
				if (this.mask[index] == 1)
					rgb &= 0x00ffffff;
				else
					rgb |= 0xff000000;
				image.setRGB(j, i, rgb);
			}
		}
		return image;
	}
	
	private int readPalette(final byte[] data) {
		int offset = 0;
		int loops = this.palette.length;
		for (int i = 0; i < loops; i++, offset += 4)
			this.palette[i] = new Color(data[offset + 2] & 0xff, data[offset + 1] & 0xff, data[offset] & 0xff);
		return offset;
	}
	
	protected static int readMask(final byte[] data, int offset, final int[] des, int width, int height) {
		int scanLine = getScanLine(width / 8);
		int b, mask, pos;
		
		for (int i = 0; i < height; i++) {
			b = 0;
			mask = 0x80;
			pos = (height - i - 1) * width;
			for (int j = 0; j < width; j++) {
				des[pos++] = ((data[offset + b] & mask) / mask) & 0xff;
				if (mask == 0x01) {
					mask = 0x80;
					b++;
				} else
					mask >>= 1;
			}
			offset += scanLine;
		}
		
		return offset;
	}
}
