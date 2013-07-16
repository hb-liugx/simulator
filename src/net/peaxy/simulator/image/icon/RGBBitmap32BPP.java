/**
 * 
 */
package net.peaxy.simulator.image.icon;


/**
 * @author Liang
 *
 */
public final class RGBBitmap32BPP extends RGBBitmap {
	/**
	 * @param width
	 * @param height
	 * @param bitCount
	 * @param data
	 */
	public RGBBitmap32BPP(int width, int height, int bitCount, byte[] data) {
		super(width, height, bitCount, data);
	}

	/* (non-Javadoc)
	 * @see net.peaxy.simulator.image.icon.Bitmap#readPixels(byte[], int)
	 */
	@Override
	protected int readPixels(final byte[] data, int offset) {
		int scanLine = this.width * 4;
		int b, pos;
		
		for (int i = 0; i < this.height; i++) {
			b = 0;
			pos = (this.height - i - 1) * this.width;
			for (int j = 0; j < this.width; j++, pos++, b += 4)
				this.pixels[pos] = (data[offset + b] & 0xff) | ((data[offset + b + 1] & 0xff) << 8) | ((data[offset + b + 2] & 0xff) << 16) | ((data[offset + b + 3] & 0xff) << 24);
			offset += scanLine;
		}
		
		return offset;
	}
}
