/**
 * 
 */
package net.peaxy.simulator.xml;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author Liang
 *
 */
public final class XMLReader extends DefaultHandler {
	public static XMLElement[] read(InputStream inputStream) throws Throwable {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		org.xml.sax.XMLReader xmlReader = saxParser.getXMLReader();
		XMLReader handler = new XMLReader();
		xmlReader.setContentHandler(handler);
		xmlReader.parse(new InputSource(inputStream));
		inputStream.close();
		return handler.elements.toArray(new XMLElement[0]);
	}
	
	private LinkedList<XMLElement> elements;
	private Stack<XMLElement> elementsStack;
	
	private XMLReader() {
		this.elements =  new LinkedList<XMLElement>();
		this.elementsStack =  new Stack<XMLElement>();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) 	throws SAXException {
		XMLElement e = new XMLElement(qName);
		int loops = attributes.getLength();
		for (int i = 0; i < loops; i++)
			e.setAttribute(attributes.getQName(i), attributes.getValue(i));
		if (this.elementsStack.size() > 0) {
			XMLElement parent = this.elementsStack.lastElement();
			parent.addChild(e);
			e.setParent(parent);
		}
		this.elementsStack.push(e);
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (length > 0) {
			String s = new String(ch, start, length).trim();
			if (s.length() > 0 && this.elementsStack.size() > 0)
				this.elementsStack.lastElement().setCharacters(s);
		}
	}

	@Override
	public void endElement (String uri, String localName, String qName) throws SAXException {
		XMLElement e = this.elementsStack.pop();
		if (this.elementsStack.size() == 0 && e != null)
			this.elements.add(e);
    }
}
