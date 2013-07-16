package net.peaxy.simulator.web.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import net.peaxy.simulator.data.TableSpace;
import net.peaxy.simulator.entity.domain.User;
import net.peaxy.simulator.manager.UserManager;
import net.peaxy.simulator.web.exception.AuthenticationException;
import net.peaxy.simulator.web.exception.InternalException;

@Path("/aaa")
public class UserService {

	static{
		if(!TableSpace.getInstance().hasTable("user")){
    		TableSpace.getInstance().addTable("user");
    	}
	}
	
	@POST    
    @Path("/user/login/{name}/{password}")
    @Produces(MediaType.APPLICATION_JSON)        
    public User login(@PathParam("name") String name,@PathParam("password") String password, @Context HttpHeaders headers ) {
		List<String> values = headers.getRequestHeader("hf-token");
		String sessionID = headers.getCookies().get("JSESSIONID").getValue();
        String existingToken = null;        
		if ( values != null && values.size() > 0 ){
            existingToken = values.get(0);
		}
        try {
            User u = UserManager.getInstance().validateUser(name, password, existingToken, sessionID);
            return u;
        } 
        catch (SecurityException e) {
            throw new AuthenticationException("login failed");
        }
        catch ( Exception ex ) {
            throw new InternalException(ex.getMessage());
        }
    }
	
	@PUT
    @Path("/user/create")
    @Consumes(MediaType.APPLICATION_JSON)     
    public void createUser(User user) {
		try {
            UserManager.getInstance().createUser(user);            
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
	}
	
	@POST
    @Path("/user/updatepwd")
    @Consumes(MediaType.APPLICATION_JSON)    
    public void updateUserPassword(User user) {
    	try {
            UserManager.getInstance().updateUserPwd(user);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }
	
	@GET
    @Path("/adminaddress")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAdminEmail() {
        return "admin@peaxy.net";
    }
	
}
