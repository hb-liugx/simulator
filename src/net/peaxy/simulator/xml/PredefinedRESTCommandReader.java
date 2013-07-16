/**
 * 
 */
package net.peaxy.simulator.xml;

import java.util.LinkedList;

import net.peaxy.simulator.data.TableSpace;
import net.peaxy.simulator.entity.PredefinedRESTCommand;
import net.peaxy.simulator.entity.PredefinedRESTCommandFactory;

/**
 * @author Liang
 *
 */
public final class PredefinedRESTCommandReader extends XMLAbstractAccessor {
	private PredefinedRESTCommand command = null;

	public PredefinedRESTCommand getCommand(){
		return this.command;
	}
	/* (non-Javadoc)
	 * @see net.peaxy.storage.simulator.xml.XMLAbstractAccessor#getXMLElementHandlerEntries()
	 */
	@Override
	protected XMLElementHandlerEntry[] getXMLElementHandlerEntries() {
		LinkedList<XMLElementHandlerEntry> entries = new LinkedList<XMLElementHandlerEntry>();
		entries.add(new XMLElementHandlerEntry("command", new XMLElementHandler() {
			public void handle(XMLElement element) {
				String clazz = element.getAttribute("class");
				String endpoint = element.getAttribute("endpoint");
				String method= element.getAttribute("method");
				command = PredefinedRESTCommandFactory.buildCommand(clazz);
				if(command != null){
					command.setName(element.getAttribute("name"));
					command.setMethod(method);
					command.setEndpoint(endpoint);
					command.setSourceClass(clazz);
					command.setPesistent("true".equalsIgnoreCase(element.getAttribute("persistence")));
					command.setStorageName(element.getAttribute("storageName"));
					if(command.isPesistent()){
						TableSpace.getInstance().addTable(command.getStorageName());
					}
				}
			}
		}));
		entries.add(new XMLElementHandlerEntry("successful", new XMLElementHandler() {
			public void handle(XMLElement element) {
				if(command != null){
					command.setSuccessfulOutput(element.getCharacters());
					command.setSuccessfulOutputType(element.getAttribute("type"));
					command.setSuccessfulOutputKey(element.getAttribute("key"));
					command.setSuccessfulHttpCode(Integer.parseInt(element.getAttribute("httpcode")));
				}
			}
		}));
		entries.add(new XMLElementHandlerEntry("failed", new XMLElementHandler() {
			public void handle(XMLElement element) {
				if(command != null){
					command.setFailedOutput(element.getCharacters());
					command.setFailedOutputType(element.getAttribute("type"));
					command.setFailedHttpCode(Integer.parseInt(element.getAttribute("httpcode")));
				}
			}
		}));
		entries.add(new XMLElementHandlerEntry("parameters", new XMLElementHandler() {
			public void handle(XMLElement element) {
				if(command != null){
					command.initParamaeter();
				}
			}
		}));
		entries.add(new XMLElementHandlerEntry("parameter", new XMLElementHandler() {
			public void handle(XMLElement element) {
				if(command != null){
					PredefinedRESTCommand.CommandParameter parameter = new PredefinedRESTCommand.CommandParameter();
					parameter.setName(element.getAttribute("name"));
					parameter.setObjectName(element.getAttribute("objectName"));
					parameter.setComparable("true".equalsIgnoreCase(element.getAttribute("comparable")));
					parameter.setSessional("true".equalsIgnoreCase(element.getAttribute("sessional")));
					parameter.setKey("true".equalsIgnoreCase(element.getAttribute("isKey")));
					command.addParameter(parameter);
				}
			}
		}));
		return entries.toArray(new XMLElementHandlerEntry[0]);
	}
}
