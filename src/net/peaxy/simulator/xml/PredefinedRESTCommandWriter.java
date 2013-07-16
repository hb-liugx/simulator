/**
 * 
 */
package net.peaxy.simulator.xml;

import java.util.Set;

import net.peaxy.simulator.entity.PredefinedRESTCommand;
import net.peaxy.simulator.entity.PredefinedRESTCommand.CommandParameter;
import net.peaxy.simulator.manager.CommandManager;

/**
 * @author Liang
 *
 */
public final class PredefinedRESTCommandWriter {
	public boolean write(String xmlFileName, String dtdFileName) {
		XMLElement predefinedXMLElement = new XMLElement("predefined");
		PredefinedRESTCommand[] commands = CommandManager.getAllCommand();
		for (PredefinedRESTCommand command : commands) {
			XMLElement commandXMLElement = new XMLElement("command");
			commandXMLElement.setAttribute("name", command.getName());
			commandXMLElement.setAttribute("method", command.getMethod());
			commandXMLElement.setAttribute("endpoint", command.getEndpoint());
			String source = command.getSource();
			if ("predefined".equalsIgnoreCase(source)) {
				commandXMLElement.setAttribute("source", source);
				commandXMLElement.setAttribute("source.class", command.getSourceClass());
			}
			
			XMLElement outputXMLElement = new XMLElement("output");
			XMLElement successfulXMLElement = new XMLElement("successful");
			successfulXMLElement.setAttribute("type", command.getSuccessfulOutputType());
			successfulXMLElement.setCharacters(command.getSuccessfulOutput());
			outputXMLElement.addChild(successfulXMLElement);
			XMLElement failedXMLElement = new XMLElement("failed");
			failedXMLElement.setAttribute("type", command.getFailedOutputType());
			failedXMLElement.setCharacters(command.getFailedOutput());
			outputXMLElement.addChild(failedXMLElement);
			commandXMLElement.addChild(outputXMLElement);
			
			XMLElement parametersXMLElement = new XMLElement("parameters");
			parametersXMLElement.setAttribute("persistent", Boolean.toString(command.isPesistent()).toLowerCase());
			Set<String> names = command.getParameterNames();
			for (String name : names) {
				XMLElement parameterXMLElement = new XMLElement("parameter");
				CommandParameter parameter = command.getParameter(name);
				parameterXMLElement.setAttribute("name", name);				
				parameterXMLElement.setAttribute("comparable", Boolean.toString(parameter.isComparable()).toLowerCase());
				parameterXMLElement.setAttribute("sessional", Boolean.toString(parameter.isSessional()).toLowerCase());
				parametersXMLElement.addChild(parameterXMLElement);
			}
			commandXMLElement.addChild(parametersXMLElement);
			predefinedXMLElement.addChild(commandXMLElement);
		}
		return XMLWriter.write(xmlFileName, dtdFileName, "predefined", new XMLElement[] {predefinedXMLElement});
	}
}
