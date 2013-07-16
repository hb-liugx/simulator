package net.peaxy.simulator.entity;

import javax.ws.rs.core.MultivaluedMap;

import net.peaxy.simulator.data.TableSpace;

public class PredefinedRESTCommandCompletion extends PredefinedRESTCommand {

	private static final long serialVersionUID = -659951324097273120L;

	public Output invoke(final MultivaluedMap<String, String> queryParameters, final String sessionID, final String host) {
		boolean noUser = true;
		try {
			noUser = TableSpace.getInstance().isEmptyTable(this.getStorageName());
		} catch(Exception e){
			TableSpace.getInstance().addTable(this.getStorageName());
			return generateOutput(null,"true",501,false);
		}
		if(noUser){			
			return generateOutput(null,"true",200,true);
		} else {
			return generateOutput(null,"false",200,true);
		}
	}
}
