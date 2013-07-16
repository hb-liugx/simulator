/**
 * 
 */
package net.peaxy.simulator.xml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

/**
 * @author Liang
 *
 */
public abstract class XMLAbstractAccessor {
protected abstract XMLElementHandlerEntry[] getXMLElementHandlerEntries();
	
	public final void read(String xmlFile) throws Throwable {
		this.read(new FileInputStream(xmlFile));
	}
	
	public final void read(URL url) throws Throwable {
		this.read(url.openStream());
	}
	
	public final void read(InputStream inputStream) throws Throwable {
		Hashtable<String, XMLElementHandler> handlers = new Hashtable<String, XMLElementHandler>();
		XMLElementHandlerEntry[] entries = this.getXMLElementHandlerEntries();
		for (XMLElementHandlerEntry entry : entries)
			handlers.put(entry.elementName, entry.elementHandler);
		XMLElement[] elements = XMLReader.read(inputStream);
		for (XMLElement e : elements)
			processElement(e, handlers);
	}
	
	public final boolean write(String xmlFileName, XMLElement[] xmlElements) {
		return XMLWriter.write(xmlFileName, xmlElements);
	}
	
	private void processElement(XMLElement element, Hashtable<String, XMLElementHandler> handlers) {
		XMLElementHandler handler = handlers.get(element.getName());
		if (handler != null)
			handler.handle(element);
		XMLElement[] children = element.getChildren();
		for (XMLElement e : children)
			processElement(e, handlers);
	}
	
	protected final class XMLElementHandlerEntry {
		private String elementName;
		private XMLElementHandler elementHandler;
		
		public XMLElementHandlerEntry(String elementName, XMLElementHandler elementHandler) {
			this.elementName = elementName;
			this.elementHandler = elementHandler;
		}
	}
}
