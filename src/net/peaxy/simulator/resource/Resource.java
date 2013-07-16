/**
 * 
 */
package net.peaxy.simulator.resource;

import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.peaxy.simulator.image.icon.IconFile;

/**
 * @author Liang
 *
 */
public final class Resource {
	private static final String materialPath = "material/";
	
	public static URL getResourceURL(String resourceName) {
		return Resource.class.getResource(materialPath + resourceName);
	}
	
	public static Image getImage(String name) {
		try {
			URL url = Resource.class.getResource(materialPath + name);
			if (name.toLowerCase().endsWith(".ico")) {
				IconFile iconFile = new IconFile(url);
				if (iconFile.contains(iconName8))
					return iconFile.getImage(iconName8);
				if (iconFile.contains(iconName4))
					return iconFile.getImage(iconName4);
				return iconFile.getImage();
			}
			return ImageIO.read (url);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
	public static ImageIcon getImageIcon(String name) {
		return new ImageIcon(getImage(name));
	}
	
	private Resource() {}
	
	private static final String iconName8 = "16*16@8";
	private static final String iconName4 = "16*16@4";
}
