package net.peaxy.simulator.manager;

import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import net.peaxy.simulator.entity.PredefinedRESTCommand;
import net.peaxy.simulator.entity.PredefinedRESTCommandFactory;

public class CommandManager {

	private static ConcurrentHashMap<String, String> supportCommand;
	private static ConcurrentHashMap<String, PredefinedRESTCommand> allCommands;
	
	static {
		supportCommand = new ConcurrentHashMap<String, String>();
		allCommands = new ConcurrentHashMap<String, PredefinedRESTCommand>();
	}
	
	private CommandManager(){		
	}
	
	public static void saveCommandParameter(String endpoint,String method,Hashtable<String, String> params){
		PredefinedRESTCommand command = getCommand(endpoint, method);
		if(command != null){
			
		}
	}
	
	public static PredefinedRESTCommand getCommand(String endpoint,String method){
		PredefinedRESTCommand command = null;
		String key = endpoint + "_" + method;		
		synchronized (allCommands) {
			if(allCommands.containsKey(key)) {
				command = allCommands.get(key);
			} else {
				String commandFile = getCommandFile(key);
				command = PredefinedRESTCommandFactory.readCommandFile(commandFile);
				if(command != null){
					allCommands.put(key, command);
				}
			}
		}
		return command;
	}
	
	public static void addSupportCommand(String key,String commandName){
		synchronized (supportCommand) {
			supportCommand.put(key, commandName);
		}
	}
	
	public static String getCommandFile(String key){
		String commandFile = "";
		synchronized (supportCommand) {
			commandFile = supportCommand.get(key);
		}
		return commandFile;
	}
	
	public static boolean isSupported(String endpoint,String method){
		boolean support = false;
		String key = endpoint + "_" + method;
		synchronized (supportCommand) {
			support = supportCommand.containsKey(key);
		}
		return support;
	}

	public static PredefinedRESTCommand[] getAllCommand() {
		return allCommands.values().toArray(new PredefinedRESTCommand[0]);
	}
}
