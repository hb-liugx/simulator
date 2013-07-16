package net.peaxy.simulator.entity;

import net.peaxy.simulator.xml.PredefinedRESTCommandReader;

public class PredefinedRESTCommandFactory {
	
	private PredefinedRESTCommandFactory(){
	}

	public static PredefinedRESTCommand buildCommand(String className){
		PredefinedRESTCommand command = null;
		if("".equals(className)){
			command = new PredefinedRESTCommand();
		} else {
			try {
				Class<?> clazz = Class.forName(className);
				command = (PredefinedRESTCommand) clazz.newInstance();
			} catch (Exception e) {
				command = new PredefinedRESTCommand();
				e.printStackTrace();
			}
		}
		return command;
	}
	
	public static PredefinedRESTCommand readCommandFile(String commandFile) {
		PredefinedRESTCommand command = null;		
		try {
			PredefinedRESTCommandReader commandReader = new PredefinedRESTCommandReader();
			commandReader.read(commandFile);
			command = commandReader.getCommand();
		} catch (Throwable e) {
			//e.printStackTrace();
		}
		return command;
	}
}
