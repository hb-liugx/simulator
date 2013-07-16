/**
 * 
 */
package net.peaxy.simulator.xml;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * @author Liang
 *
 */
public final class XMLElement implements Serializable {
	private static final long serialVersionUID = -7647745288504964142L;
	
	private String name;
	private String characters;
	private Hashtable<String, String> attributes;
	private XMLElement parent;
	private LinkedList<XMLElement> children;
	
	public XMLElement(String name) {
		this.name = name;
		this.attributes = new Hashtable<String, String>();
		this.children = new LinkedList<XMLElement>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCharacters() {
		return this.characters;
	}
	
	public void setCharacters(String characters) {
		this.characters = characters;
	}
	
	public String getAttribute(String attributeName) {
		return this.attributes.get(attributeName);
	}
	
	public void setAttribute(String attributeName, String attribute) {
		if(attribute != null)
			this.attributes.put(attributeName, attribute);
	}
	
	public String removeAttribute(String attributeName) {
		return this.attributes.remove(attributeName);
	}
	
	public XMLElement getParent() {
		return this.parent;
	}
	
	public void setParent(XMLElement parent) {
		this.parent = parent;
	}
	
	public XMLElement[] getChildren() {
		return this.children.toArray(new XMLElement[0]);
	}
	
	public void addChild(XMLElement childElement) {
		this.children.add(childElement);
	}
	
	public boolean removeChild(XMLElement childElement) {
		return this.children.remove(childElement);
	}
	
	public boolean removeChild(String childElementName) {
		int loops = this.children.size();
		for (int i = 0; i < loops; i++) {
			if (this.children.get(i).name.equals(childElementName)) {
				this.children.remove(i);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(symbolLessThan);
		stringBuilder.append(this.name);
		String[] keys = this.attributes.keySet().toArray(new String[0]);
		for (String key : keys) {
			stringBuilder.append(symbolSpace);
			stringBuilder.append(key);
			stringBuilder.append(symbolEquality);
			stringBuilder.append(symbolQuotation);
			stringBuilder.append(this.attributes.get(key));
			stringBuilder.append(symbolQuotation);
		}
		stringBuilder.append(symbolGreaterThan);
		if (this.characters != null)
			stringBuilder.append(this.characters);
		int loops = this.children.size();
		for (int i = 0; i < loops; i++)
			stringBuilder.append(this.children.get(i).toString());
		stringBuilder.append(symbolLessThan);
		stringBuilder.append(symbolSolidus);
		stringBuilder.append(this.name);
		stringBuilder.append(symbolGreaterThan);
		return stringBuilder.toString();
	}
	
	private static final String symbolEquality = "=";
	private static final String symbolQuotation = "\"";
	private static final String symbolLessThan = "<";
	private static final String symbolGreaterThan = ">";
	private static final String symbolSolidus = "/"; 
	private static final String symbolSpace = " ";
}
