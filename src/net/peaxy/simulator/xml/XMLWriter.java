/**
 * 
 */
package net.peaxy.simulator.xml;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Liang
 *
 */
public final class XMLWriter {
	private static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
	private static final String dtdLabel = "<!DOCTYPE @1 SYSTEM \"@2\">";
	private static final String xmlEncoding = "utf-8";
	
	public static boolean write(String xmlFileName, XMLElement[] xmlElements) {
		return write(xmlFileName, null, null, xmlElements);
	}
	
	public static boolean write(String xmlFileName, String dtdFileName, String rootElement, XMLElement[] xmlElements) {
		boolean result = xmlElements != null;
		if (result) {
			File xmlFile = new File(xmlFileName);
			try {
				FileOutputStream outputStream = new FileOutputStream(xmlFile);
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(xmlHeader);
				if (dtdFileName != null)
					stringBuilder.append(dtdLabel.replaceAll("@1", rootElement).replaceAll("@2", dtdFileName));
				byte[] bytes = stringBuilder.toString().getBytes(xmlEncoding);
				try {
					outputStream.write(bytes);
					outputStream.flush();
					for (int i = 0; i < xmlElements.length; i++) {
						bytes = xmlElements[i].toString().getBytes(xmlEncoding);
						outputStream.write(bytes);
						outputStream.flush();
					}
					outputStream.close();
				} catch (Throwable t) {
					outputStream.close();
					xmlFile.delete();
					result = false;
				}
			} catch (Throwable t) {
				result = false;
			}
		}
		return result;
	}
	
	private XMLWriter() {}
}
