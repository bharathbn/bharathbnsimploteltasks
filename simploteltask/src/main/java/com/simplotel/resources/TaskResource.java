package com.simplotel.resources;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.simplotel.resources.model.SimplotelTask;
import com.simplotel.resources.service.SimplotelTaskService;

@Path("/")
public class TaskResource {  

	private static final String welcome = "Welcome to simplotel";
	
	@GET
    @Produces(MediaType.TEXT_PLAIN)//Entry point for app
    public String getIt() {
        return welcome;
    }
		
	
	@Path("{user_id}")// entry point for user
	public TaskRootResource redirecttosubresource(@PathParam("user_id") String userid ) throws SQLException, NotFoundException      
    {
		
		try  
		  {  
		    int uid = Integer.parseInt(userid);  
		    
		    return new TaskRootResource(uid);
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    throw new NotFoundException("user not found with the id: " + userid );   
		  }  
		
    }
	
}
