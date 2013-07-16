package net.peaxy.simulator.manager;

import java.util.List;

import net.peaxy.simulator.data.Record;
import net.peaxy.simulator.data.TableSpace;
import net.peaxy.simulator.entity.domain.PeaxyBaseDomain;
import net.peaxy.simulator.entity.domain.User;
import net.peaxy.simulator.entity.domain.UserRole;
import net.peaxy.simulator.util.Utility;
import net.peaxy.simulator.web.WebServerContainer;
import net.peaxy.simulator.web.WebServerSessionManager;

import org.codehaus.jettison.json.JSONObject;

public class UserManager {
	
	public static final String TABLE_NAME = "user";
	
	public static final String SESSION_USER_NAME = "LoginUser";
	public static final String SESSION_TOKEN_NAME = "UserToken";
	public static final String SESSION_LOCKED_TIME = "LockedTime";
	public static final String SESSION_FAILED_COUNT = "FailedCount";
	
	static{
		if(!TableSpace.getInstance().hasTable(TABLE_NAME)){
    		TableSpace.getInstance().addTable(TABLE_NAME);
    	}
	}

	private static UserManager instance = null;
	
	private UserManager(){		
	}
	
	public static UserManager getInstance(){
		if(instance == null){
			instance = new UserManager();
		}
		return instance;
	}

	public void createUser(User user) {
		ConfigManager.addMessage("create user : " + user.getName());
		user.setToken(Utility.generateSN(8));
		if(!TableSpace.getInstance().hasData(TABLE_NAME, user.getName())){
			TableSpace.getInstance().setData(TABLE_NAME, user.getName(), user.toJSON());
		}
	}

	public void updateUserPwd(User user) {
		ConfigManager.addMessage("update user password: " + user.getName());
    	TableSpace.getInstance().setData(TABLE_NAME,
    			user.getName(), user.toJSON());
	}

	public User validateUser(String name, String password, String existingToken, String sessionID) {
		JSONObject json = TableSpace.getInstance().getData(TABLE_NAME, name).toJson();
		WebServerSessionManager session = WebServerContainer.getSessionManager();
		User loginUser = null;
		if(json != null){
			try {
				loginUser = (User)PeaxyBaseDomain.toDomain(json.toString());
				if(loginUser != null){
					if(loginUser.isLock()){
						long lastFailTime = Long.parseLong(session.getData(sessionID, SESSION_LOCKED_TIME+"_"+name));
						if( System.currentTimeMillis() - lastFailTime < 30000L ){
							throw new SecurityException("The User is locked.");
						} else {
							loginUser.setLock(false);
							session.setData(sessionID, SESSION_LOCKED_TIME+"_"+name, String.valueOf(0));
							session.setData(sessionID, SESSION_FAILED_COUNT+"_"+name, String.valueOf(0));
							TableSpace.getInstance().setData(TABLE_NAME,loginUser.getName(), loginUser.toJSON());
						}
					}
					if(loginUser.getPassword().equals(password) ){
						ConfigManager.addMessage("user : " + name + " login successful.");
						session.setData(sessionID, SESSION_USER_NAME, name);
						session.setData(sessionID, SESSION_TOKEN_NAME, loginUser.getToken());
						
						session.setData(sessionID, SESSION_LOCKED_TIME+"_"+name, String.valueOf(0));
						session.setData(sessionID, SESSION_FAILED_COUNT+"_"+name, String.valueOf(0));
					} else {
						int count = 0;
						try {
							count = Integer.parseInt(session.getData(sessionID, SESSION_FAILED_COUNT+"_"+name));
						} catch (Exception e){
						}
						count++;
						session.setData(sessionID, SESSION_FAILED_COUNT+"_"+name, String.valueOf(count));
						if(count > 2 ) {
							loginUser.setLock(true);
							session.setData(sessionID, SESSION_LOCKED_TIME+"_"+name, String.valueOf(System.currentTimeMillis()));
							TableSpace.getInstance().setData(TABLE_NAME,loginUser.getName(), loginUser.toJSON());
						}
						throw new SecurityException("Login failed.");
					} 
				} else {
					throw new SecurityException("Login failed.");
				}
			} catch (Exception e){
				throw new SecurityException("Login failed.");
			}
		}
		return loginUser;
	}
	
	public User getAdminUser(){
		User user = null;
		List<Record> records = TableSpace.getInstance().getAllData(TABLE_NAME);
		for(Record record : records){
			user = (User)PeaxyBaseDomain.toDomain(record.toJson().toString());
			if(user.getRole() == UserRole.ADMIN){
				return user;
			}
		}
		return null;
	}
	public boolean hasUser(){
		return !TableSpace.getInstance().isEmptyTable(TABLE_NAME);
	}
}
