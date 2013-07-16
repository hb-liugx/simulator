/**
 * 
 */
package net.peaxy.simulator.image.icon;

import java.util.LinkedList;

/**
 * @author Liang
 *
 *	reserved	2 bytes, always 0
 *	type			2 bytes, always 1
 *	count			2 bytes
 */
final class IconHeader {
	private static final int iconEntrySize = 16;
	private LinkedList<IconEntry> iconEntries;
	
	public IconHeader(final byte[] bytes) {
		this.iconEntries = new LinkedList<IconEntry>();
		int count = (bytes[4] & 0xff)  | ((bytes[5] & 0xff) << 8);
		int offset = 6;
		for (int i = 0; i < count; i++) {
			this.iconEntries.add(new IconEntry(bytes, offset));
			offset += iconEntrySize;
		}
	}
	
	public int getCount() {
		return this.iconEntries.size();
	}
	
	public IconEntry getIconEntry(int index) {
		return this.iconEntries.get(index);
	}
}
