/**
 * 
 */
package net.peaxy.simulator.ui.base;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import net.peaxy.simulator.resource.Resource;
import net.peaxy.simulator.xml.XMLAbstractAccessor;
import net.peaxy.simulator.xml.XMLElement;
import net.peaxy.simulator.xml.XMLElementHandler;

/**
 * @author Liang
 *
 */
public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 172657526234153604L;
	
	private HashMap<String, JMenuItem> menuMap;
	private MenuBarListener listener;
	
	public MenuBar() {
		super();
		this.menuMap = new HashMap<String, JMenuItem>();
	}
	
	public MenuBar(MenuBarListener listener) {
		this();
		this.listener = listener;
	}
	
	public MenuBar(MenuBarListener listener, URL url) throws Throwable {
		this(listener);
		this.buildFromXML(url);
	}
	
	public JMenu createMenu(String name, String text, String tip, int mnemonic, Icon icon, MenuBarListener listener) {
		MenuBarMenu menu = new MenuBarMenu(text, tip, mnemonic, icon, listener);
		menu.setName(name);
		this.menuMap.put(name, menu);
		return menu;
	}
	
	public JMenu createMenu(String name, String text, String tip, int mnemonic, Icon icon) {
		return this.createMenu(name, text, tip, mnemonic, icon, this.listener);
	}
	
	public JMenuItem createMenuItem(String name, String text, String tip, int mnemonic, KeyStroke accelerator, Icon icon, MenuBarListener listener) {
		MenuBarMenuItem menuItem = new MenuBarMenuItem(text, tip, mnemonic, accelerator, icon, listener);
		menuItem.setName(name);
		this.menuMap.put(name, menuItem);
		return menuItem;
	}
	
	public JMenuItem createMenuItem(String name, String text, String tip, int mnemonic, KeyStroke accelerator, Icon icon) {
		return this.createMenuItem(name, text, tip, mnemonic, accelerator, icon, this.listener);
	}
	
	public MenuBarListener getListener() {
		return this.listener;
	}
	
	public void setListener(MenuBarListener listener) {
		this.listener = listener;
	}
	
	public void setMenuEnabled(String menuID, boolean enabled) {
		if (this.menuMap.containsKey(menuID))
			this.menuMap.get(menuID).setEnabled(enabled);
	}
	
	private void buildFromXML(URL url) throws Throwable {
		new MenuBarReader().read(url);
	}

	private class MenuBarReader extends XMLAbstractAccessor {
		/* (non-Javadoc)
		 * @see net.peaxy.simulator.xml.XMLAbstractAccessor#getXMLElementHandlerEntries()
		 */
		@Override
		protected XMLElementHandlerEntry[] getXMLElementHandlerEntries() {
			LinkedList<XMLElementHandlerEntry> entries = new LinkedList<XMLElementHandlerEntry>();
			entries.add(new XMLElementHandlerEntry("menu", new XMLElementHandler() {
				public void handle(XMLElement element) {
					int mnemonic = MenuBarReader.this.getMnemonic(element.getAttribute("mnemonic").trim());
					Icon icon = MenuBarReader.this.getIcon(element.getAttribute("icon").trim());
					JMenu menu = MenuBar.this.createMenu(element.getAttribute("name"), element.getAttribute("text"), element.getAttribute("tip"), mnemonic, icon);
					menu.setEnabled("true".equalsIgnoreCase(element.getAttribute("enabled")));
					menu.setVisible("true".equalsIgnoreCase(element.getAttribute("visible")));
					XMLElement parent = element.getParent();
					if (parent != null) {
						JMenuItem parentMenu = MenuBar.this.menuMap.get(parent.getAttribute("name"));
						if (parentMenu != null)
							parentMenu.add(menu);
						else
							MenuBar.this.add(menu);
					} else
						MenuBar.this.add(menu);
				}
			}));
			entries.add(new XMLElementHandlerEntry("item", new XMLElementHandler() {
				public void handle(XMLElement element) {
					int mnemonic = MenuBarReader.this.getMnemonic(element.getAttribute("mnemonic").trim());
					KeyStroke accelerator = this.getAccelerator(element.getAttribute("accelerator").trim());
					Icon icon = MenuBarReader.this.getIcon(element.getAttribute("icon").trim());
					JMenuItem menuItem = MenuBar.this.createMenuItem(element.getAttribute("name"), element.getAttribute("text"), element.getAttribute("tip"), mnemonic, accelerator, icon);
					menuItem.setEnabled("true".equalsIgnoreCase(element.getAttribute("enabled")));
					menuItem.setVisible("true".equalsIgnoreCase(element.getAttribute("visible")));
					XMLElement parent = element.getParent();
					if (parent != null) {
						JMenuItem parentMenu = MenuBar.this.menuMap.get(parent.getAttribute("name"));
						if (parentMenu != null)
							parentMenu.add(menuItem);
					}
				}
				
				private KeyStroke getAccelerator(String acceleratorString) {
					KeyStroke accelerator = null;
					if (!acceleratorString.isEmpty()) {
						String[] accelerators = acceleratorString.split("\\+");
						if (accelerators.length > 1) {
							String[] masks = accelerators[1].split("\\|");
							if (masks.length > 0) {
								int modifiers = 0;
								for (String mask : masks)
									modifiers |= Integer.parseInt(mask.replaceAll("0x", ""), 16);
								accelerator = KeyStroke.getKeyStroke(Integer.parseInt(accelerators[0].replaceAll("0x", ""), 16), modifiers);
							}
						}
					}
					return accelerator;
				}
			}));
			entries.add(new XMLElementHandlerEntry("separator", new XMLElementHandler() {
				public void handle(XMLElement element) {
					XMLElement parent = element.getParent();
					if (parent != null) {
						JMenuItem parentMenu = MenuBar.this.menuMap.get(parent.getAttribute("name"));
						if (parentMenu instanceof JMenu)
							((JMenu)parentMenu).addSeparator();
					}
				}
			}));
			return entries.toArray(new XMLElementHandlerEntry[0]);
		}
		
		private int getMnemonic(String mnemonicName) {
			if (mnemonicName.startsWith("0x"))
				return Integer.parseInt(mnemonicName.replaceAll("0x", ""), 16);
			return Integer.parseInt(mnemonicName);
		}
		
		private Icon getIcon(String iconName) {
			return iconName.isEmpty() ? null : Resource.getImageIcon(iconName);
		}
	}
	
	private class MenuBarMenu extends JMenu {
		private static final long serialVersionUID = 1864410441926056503L;
		
		private final String tip;
		private  boolean isSelected = false;
		private MenuBarListener listener;
		
		public MenuBarMenu(String text, String tip, int mnemonic, Icon icon, MenuBarListener listener) {
			super(text);
			this.tip = tip;
			this.setMnemonic(mnemonic);
			if (icon != null)
				this.setIcon(icon);
			this.listener = listener;
		}
		
		@Override
		public void menuSelectionChanged(boolean isIncluded) {
			super.menuSelectionChanged(isIncluded);
			if (isIncluded != this.isSelected) {
				if (isIncluded)
	        		 this.listener.onTip(this.tip);
	        	 this.isSelected = isIncluded;
			}
		}
	}
	
	private class MenuBarMenuItem extends JMenuItem {
		private static final long serialVersionUID = 9056472439387729246L;
		
		private final String tip;
		private  boolean isSelected = false;
		private MenuBarListener listener;
		
		public MenuBarMenuItem(String text, String tip, int mnemonic, KeyStroke accelerator, Icon icon, MenuBarListener listener) {
			super(text, mnemonic);
			this.tip = tip;
			if (accelerator != null)
				this.setAccelerator(accelerator);
			if (icon != null)
				this.setIcon(icon);
			this.listener = listener;
			this.addActionListener(this.listener);
		}
		
		@Override
		public void menuSelectionChanged(boolean isIncluded) {
			super.menuSelectionChanged(isIncluded);
			if (isIncluded != this.isSelected) {
				if (isIncluded)
	        		 this.listener.onTip(this.tip);
	        	 this.isSelected = isIncluded;
			}
		}
	}
}
