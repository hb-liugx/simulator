package net.peaxy.simulator.xml;

import java.util.LinkedList;

import net.peaxy.simulator.manager.CommandManager;

public class CommandListRead extends XMLAbstractAccessor {

	@Override
	protected XMLElementHandlerEntry[] getXMLElementHandlerEntries() {
		LinkedList<XMLElementHandlerEntry> entries = new LinkedList<XMLElementHandlerEntry>();
		entries.add(new XMLElementHandlerEntry("command", new XMLElementHandler() {
			public void handle(XMLElement element) {
				String method = element.getAttribute("method");
				String endpoint = element.getAttribute("endpoint");
				String file = element.getAttribute("file");
				CommandManager.addSupportCommand(endpoint+"_"+method,file);
			}
		}));
		return entries.toArray(new XMLElementHandlerEntry[0]);
	}

}
