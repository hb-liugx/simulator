/**
 * 
 */
package net.peaxy.simulator.entity;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Liang
 *
 */
public class UserRoleRight implements Serializable {
	private static final long serialVersionUID = -921362474135682052L;
	private static final HashMap<String, Right> predefined = new HashMap<String, Right>();
	
	public static synchronized void addRight(String name) {
		if (!predefined.containsKey(name))
			predefined.put(name, new Right(name));
	}
	
	public static synchronized void addSubRight(String rightName, String subRightName) {
		Right right = null;
		if (predefined.containsKey(rightName))
			right = predefined.get(rightName);
		else {
			right = new Right(rightName);
			predefined.put(rightName, right);
		}
		right.addSubRight(subRightName);
	}
	
	private static class Right implements Serializable {
		private static final long serialVersionUID = 402490206865666043L;
		
		//private String name;
		private HashMap<String, Boolean> subset;
		
		public Right(String name) {
			//this.name = name;
			this.subset = new HashMap<String, Boolean>();
		}
		
		/*public String getName() {
			return this.name;
		}*/
		
		public void addSubRight(String name) {
			this.subset.put(name, Boolean.TRUE);
		}
		
		/*public boolean hasRight(String name) {
			return this.subset.containsKey(name);
		}*/
	}
}
