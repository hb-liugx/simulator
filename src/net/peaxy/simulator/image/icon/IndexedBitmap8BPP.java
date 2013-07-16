/**
 * 
 */
package net.peaxy.simulator.image.icon;

/**
 * @author Liang
 *
 */
class IndexedBitmap8BPP extends IndexedBitmap {
	/**
	 * @param width
	 * @param height
	 * @param bitCount
	 * @param data
	 */
	public IndexedBitmap8BPP(int width, int height, int bitCount, final byte[] data) {
		super(width, height, bitCount, data);
	}

	/* (non-Javadoc)
	 * @see net.peaxy.simulator.image.icon.IndexedBitmap#readPixels(byte[], int)
	 */
	@Override
	protected int readPixels(final byte[] data, int offset) {
		int scanLine = getScanLine(this.width);
		int pos;
		for (int i = 0; i < this.height; i++) {
			pos = (this.height - i - 1) * this.width;
			for (int j = 0; j < this.width; j++)
				this.pixels[pos++] = data[offset + j] & 0xff;
			offset += scanLine;
		}
		return offset;
	}
}
