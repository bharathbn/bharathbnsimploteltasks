package com.simplotel.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.simplotel.resources.model.SimplotelTask;
import com.simplotel.resources.service.SimplotelTaskService;

public class TaskSubResource {
	
	private final int userid;
	public TaskSubResource(int uid)
	{
		userid = uid;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})//get response for tasks end point
	public List<SimplotelTask> gettasks(
			@QueryParam("page") @DefaultValue("1") int page,
		    @QueryParam("page_size") @DefaultValue("10") int pageSize,
		    @QueryParam("deleted") @DefaultValue("false")String deleted,
		    @QueryParam("sortby") @DefaultValue("due_date") String sortby,
		    @QueryParam("sort_order") @DefaultValue("ASC") String sort_order,//ASC|DESC;
		    @QueryParam("state") @DefaultValue("10") int state,
		    @QueryParam("priority") @DefaultValue("10") int priority,
		    @QueryParam("created_start") @DefaultValue("0000-00-00") String created_start,
		    @QueryParam("created_end") @DefaultValue("9999-12-31")String created_end,
		    @QueryParam("last_modified_start") @DefaultValue("0000-00-00") String last_modified_start,
		    @QueryParam("last_modified_end") @DefaultValue("9999-12-31") String last_modified_end,
		    @QueryParam("duedate_start") @DefaultValue("0000-00-00")String duedate_start,
		    @QueryParam("duedate_end") @DefaultValue("9999-12-31") String duedate_end,
		    @QueryParam("deleted_start") @DefaultValue("0000-00-00")String deleted_start,
		    @QueryParam("deleted_end") @DefaultValue("9999-12-31") String deleted_end) throws SQLException      
    {
		
		return new SimplotelTaskService(userid).getalltasks(page-1,pageSize
				,deleted,sortby,sort_order,
				state,priority,
				created_start,created_end,
				duedate_start,duedate_end,
				last_modified_start,last_modified_end,
				deleted_start,deleted_end				
				);
		
    }
	

	
	@POST
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	public SimplotelTask addTask(SimplotelTask input) throws SQLException    // to create new task  
    {
		return new SimplotelTaskService(userid).addTask(input);
    }
	
	
	
	@Path("{task_id}")
	public TaskOperationResource redirecttosubresource(@PathParam("task_id") String task_id ) throws SQLException, NotFoundException//entry point for task id      
    {
		try  
		  {  
		    int tid = Integer.parseInt(task_id);  
		    
		    return new TaskOperationResource(userid,tid);
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    throw new NotFoundException("user id not found");   
		  }  
		
    }
	
	
	

}
