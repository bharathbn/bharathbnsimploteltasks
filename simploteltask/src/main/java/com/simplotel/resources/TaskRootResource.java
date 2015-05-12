package com.simplotel.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.simplotel.resources.model.SimplotelTask;
import com.simplotel.resources.service.SimplotelTaskService;
public class TaskRootResource {
	
	private static final String  TASK_PATH = "tasks";
	
	private static final String user_Reply = "please add \"/tasks\" to API URI";
	private final int userid;
	public TaskRootResource(int uid)
	{
		userid = uid;
	}
	
	
	@GET // get response for user end point
	@Produces(MediaType.TEXT_PLAIN)
	public String responseforUser()
	{
		return user_Reply;
	}
	
	
	@Path(TASK_PATH)//entry point for tasks
	public TaskSubResource getallTasks()       
    {
		return new TaskSubResource(userid);
    }
	
}
