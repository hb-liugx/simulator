/**
 * 
 */
package net.peaxy.simulator.image.icon;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * @author Liang
 *
 */
public class IconFile {
	private HashMap<String, Image> images;
	
	public IconFile(String filename) throws IOException {
		this.images = new HashMap<String, Image>();
		this.read(new FileInputStream(filename));
	}
	
	public IconFile(URL url) throws IOException {
		this.images = new HashMap<String, Image>();
		this.read(url.openStream());
	}
	
	public boolean contains(String name) {
		return this.images.containsKey(name);
	}
	
	public int size() {
		return this.images.size();
	}
	
	public String[] getImageNames() {
		return this.images.keySet().toArray(new String[0]);
	}
	
	public Image[] getImages() {
		return this.images.values().toArray(new Image[0]);
	}
	
	public Image getImage(String name) {
		return this.images.get(name);
	}
	
	public Image getImage() {
		Image[] images = this.images.values().toArray(new Image[0]);
		if (images.length > 0)
			return images[0];
		return null;
	}
	
	public ImageIcon getImageIcon() {
		Image[] images = this.images.values().toArray(new Image[0]);
		if (images.length > 0)
			return  new ImageIcon(images[0]);
		return null;
	}
	
	public ImageIcon getImageIcon(String name) {
		if (this.images.containsKey(name))
			return new ImageIcon(this.images.get(name));
		return null;
	}
	
	private void read(InputStream inputStream) throws IOException {
		int size = inputStream.available();
		byte[] bytes = new byte[size];
		int readBytes = 0;
		try {
			readBytes = inputStream.read(bytes, 0, size);
		} finally {
			inputStream.close();
		}
		if (readBytes != size)
			throw new IOException();
		IconHeader header = new IconHeader(bytes);
		int loops = header.getCount();
		for (int i = 0; i < loops; i++) {
			IconEntry entry = header.getIconEntry(i);
			Image image = entry.getImage();
			if (image != null)
				this.images.put(entry.getName(), image);
		}
	}
}
