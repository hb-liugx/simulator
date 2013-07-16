/**
 * 
 */
package net.peaxy.simulator.image.icon;


/**
 * @author Liang
 *
 */
class RGBBitmap24BPP extends RGBBitmap {
	/**
	 * @param width
	 * @param height
	 * @param bitCount
	 * @param data
	 */
	public RGBBitmap24BPP(int width, int height, int bitCount, byte[] data) {
		super(width, height, bitCount, data);
	}
	
	/* (non-Javadoc)
	 * @see net.peaxy.simulator.image.icon.IndexedBitmap#readPixels(byte[], int)
	 */
	@Override
	protected int readPixels(byte[] data, int offset) {
		int scanLine = getScanLine(this.width * 3);
		int maskScanLine = getScanLine(this.width / 8);
		int pos, b, index, mask, maskOffset = offset + scanLine * this.height;
		for (int i = 0; i < this.height; i++) {
			b = 0;
			mask = 0x80;
			index = 0;
			pos = (this.height - i - 1) * this.width;
			for (int j = 0; j < this.width; j++) {				
				if ((((data[maskOffset + b] & mask) / mask) & 0xff) == 0)
					this.pixels[pos] = (data[offset + index] & 0xff) | ((data[offset + index + 1] & 0xff) << 8) | ((data[offset + index + 2] & 0xff) << 16) | (0xff << 24);
				else
					this.pixels[pos] = (data[offset + index] & 0xff) | ((data[offset + index + 1] & 0xff) << 8) | ((data[offset + index + 2] & 0xff) << 16);
				if (mask == 0x01) {
					mask = 0x80;
					b++;
				} else
					mask >>= 1;
				pos++;
				index += 3;
			}
			offset += scanLine;
			maskOffset += maskScanLine;
		}
		return offset;
	}
}
