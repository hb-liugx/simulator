/**
 * 
 */
package net.peaxy.simulator.image.icon;

/**
 * @author Liang
 *
 */
class IndexedBitmap4BPP extends IndexedBitmap {
	/**
	 * @param width
	 * @param height
	 * @param bitCount
	 * @param data
	 */
	public IndexedBitmap4BPP(int width, int height, int bitCount, byte[] data) {
		super(width, height, bitCount, data);
	}

	/* (non-Javadoc)
	 * @see net.peaxy.simulator.image.icon.IndexedBitmap#readPixels(byte[], int)
	 */
	@Override
	protected int readPixels(byte[] data, int offset) {
		int scanLine = getScanLine(this.width / 2);
		boolean isHigh;
		int pos, b, value;
		
		for (int i = 0; i < this.height; i++) {
			b = offset;
			isHigh = true;
			pos = (this.height - i - 1) * this.width;
			for (int j = 0; j < this.width; j++, pos++) {
                if (isHigh)
                	value = (data[b] & 0xf0) >> 4;
                else
                	value = data[b++] & 0x0f;
                this.pixels[pos] = value & 0xff;
                isHigh = !isHigh;
			}
			offset += scanLine;
		}
		return offset;
	}
}
