/**
 * 
 */
package net.peaxy.simulator.image.icon;

/**
 * @author Liang
 *
 */
class IndexedBitmap1BPP extends IndexedBitmap {

	/**
	 * @param width
	 * @param height
	 * @param bitCount
	 * @param data
	 */
	public IndexedBitmap1BPP(int width, int height, int bitCount, byte[] data) {
		super(width, height, bitCount, data);
	}

	/* (non-Javadoc)
	 * @see net.peaxy.simulator.image.icon.IndexedBitmap#readPixels(byte[], int)
	 */
	@Override
	protected int readPixels(final byte[] data, int offset) {
		return readMask(data, offset, this.pixels, this.width, this.height);
	}
}
