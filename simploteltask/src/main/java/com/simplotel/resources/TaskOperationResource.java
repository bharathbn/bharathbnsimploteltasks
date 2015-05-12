package com.simplotel.resources;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.simplotel.resources.model.SimplotelTask;
import com.simplotel.resources.service.SimplotelTaskService;


@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
public class TaskOperationResource {
	
	private static final String  TASK_PATH = "tasks";
	
	private final int userid;
	private final int taskid;
	public TaskOperationResource(int uid , int tid)
	{
		userid = uid;
		taskid = tid;
	}
	
	
	@GET
	public SimplotelTask getTask() throws SQLException// response for task details
	{
		return new SimplotelTaskService(userid).getTask(taskid , null);
	}
	
	
	@PUT
	public SimplotelTask updateTask(SimplotelTask input) throws SQLException   // update task   
    {
		return new SimplotelTaskService(userid).updateTask(taskid,input);
    }
	
	@DELETE
	public SimplotelTask deleteTask() throws SQLException      // delete task
    {
		return new SimplotelTaskService(userid).deleteTask(taskid);
    }
	
	
	
}
