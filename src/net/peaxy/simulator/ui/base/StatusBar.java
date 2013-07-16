/**
 * 
 */
package net.peaxy.simulator.ui.base;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * @author Liang
 *
 */
public class StatusBar extends JPanel {
	private static final long serialVersionUID = 1437283705282838256L;

	private StatusBarEntry[] statusBarEntries;
	
	public StatusBar(int fieldCount, int height, int...widths) {
		this.setPreferredSize(new Dimension(0, height));
		this.statusBarEntries = new StatusBarEntry[fieldCount];
		for (int i = 0; i < fieldCount; i++) {
			this.statusBarEntries[i] = new StatusBarEntry(widths[i], height);
			this.add(this.statusBarEntries[i]);
		}
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	}
	
	public void setText(int index, String text, int x, int y) {
		this.statusBarEntries[index].setText(text, x, y);
	}
	
	public void setImage(int index, Image image, int x, int y) {
		this.statusBarEntries[index].setImage(image, x, y);
	}
	
	public void setImage(int index, Image image, int x, int y, int width, int height) {
		this.statusBarEntries[index].setImage(image, x, y, width, height);
	}
	
	private class StatusBarEntry extends JPanel {
		private static final long serialVersionUID = 4794227144603929124L;
		
		private String text;
		private Image image;
		private int textX, textY;
		private int imageX, imageY, imageWidth, imageHeight;

		public StatusBarEntry(int width, int height) {
			this.text = "";
			this. image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			this.setPreferredSize(new Dimension(width, height));
			this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		}
		
		public void setText(String text, int x, int y) {
			if (text == null) throw new NullPointerException();
			this.text = text;
			this.textX = x;
			this.textY = y;
			this.repaint();
		}
		
		public void setImage(Image image, int x, int y, int width, int height) {
			if (image == null) throw new NullPointerException();
			this.image = image;
			this.imageX = x;
			this.imageY = y;
			this.imageWidth = width;
			this.imageHeight = height;
			this.repaint();
		}
		
		public void setImage(Image image, int x, int y) {
			this.setImage(image, x, y, -1, -1);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawString(this.text, this.textX, this.textY);
			if (this.imageWidth == -1)
				g.drawImage(this.image, this.imageX, this.imageY, null);
			else
				g.drawImage(this.image, this.imageX, this.imageY, this.imageWidth, this.imageHeight, null);
		}
	}
}
