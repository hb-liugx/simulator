/**
 * 
 */
package net.peaxy.simulator.image.icon;

import java.awt.Image;

/**
 * @author Liang
 * *************************
 * icon entry		16 bytes
 * *************************
 * width				1 byte
 * height			1 byte
 * colorCount		1 byte
 * reserved		1 byte
 * planes			2 bytes
 * bitCount		2 bytes
 * bytesInRes	4 bytes
 * imageOffset	4 bytes
 * **************************
 * 
 * **************************
 * icon image
 * **************************
 * bmpInfoHeader			40 bytes
 * palette						2 bytes or 16bytes or 256 bytes
 * xor							1 byte
 * and							1 byte
 * **************************
 * 
 * **************************
 * bmpInfoHeader		40 bytes
 * **************************
 * size						4 bytes
 * width						4 bytes
 * height					4 bytes
 * planes					2 bytes
 * bitCount				2 bytes
 * compression			4 bytes, 0: none, 1: RLE8, 2: RLE4
 * imageSize				4 bytes
 * xPixelsPerMeter	4 bytes
 * yPixelsPerMeter	4 bytes
 * colorUsed				4 bytes
 * colorImportant		4 bytes
 * **************************
 */
final class IconEntry {
	private Image image;
	private String name;
	
	public IconEntry(final byte[] bytes, int offset) {
		int bitCount1 = (bytes[offset + 6] & 0xff) | ((bytes[offset + 7] & 0xff) << 8);
		if (bitCount1 > 32) bitCount1 = 0;
		int width = bytes[offset] & 0xff;
		int height = bytes[offset + 1] & 0xff;
		int bytesInRes = (bytes[offset + 8] & 0xff) | ((bytes[offset + 9] & 0xff) << 8) | ((bytes[offset + 10] & 0xff) << 16)  | ((bytes[offset + 11] & 0xff) << 24);
		int imageOffset = (bytes[offset + 12] & 0xff) | ((bytes[offset + 13] & 0xff) << 8) | ((bytes[offset + 14] & 0xff) << 16)  | ((bytes[offset + 15] & 0xff) << 24);
		int bitCount2 = (bytes[imageOffset + 14] & 0xff) | ((bytes[imageOffset + 15] & 0xff) << 8);
		if (bitCount2 > 32) bitCount2 = 0;
		int bitCount = bitCount1 > bitCount2 ? bitCount1 : bitCount2;
		int size = bytesInRes - 40;
		byte[] data = new byte[size];
		System.arraycopy(bytes, imageOffset + 40, data, 0, size);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(width);
		stringBuilder.append("*");
		stringBuilder.append(height);
		stringBuilder.append("@");
		stringBuilder.append(bitCount);
		this.name = stringBuilder.toString();
		this.createIamge(width, height, bitCount, data);
	}
	
	private void createIamge(int width, int height, int bitCount, final byte[] data) {
		switch (bitCount) {
		case 1:
			this.image = new IndexedBitmap1BPP(width, height, bitCount, data).createImage();
			break;
		case 4:
			this.image = new IndexedBitmap4BPP(width, height, bitCount, data).createImage();
			break;
		case 8:
			this.image = new IndexedBitmap8BPP(width, height, bitCount, data).createImage();
			break;
		case 24:
			this.image = new RGBBitmap24BPP(width, height, bitCount, data).createImage();
			break;
		case 32:
			this.image = new RGBBitmap32BPP(width, height, bitCount, data).createImage();
			break;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public Image getImage() {
		return this.image;
	}
}
