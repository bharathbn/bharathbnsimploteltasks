package com.simplotel.resources.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import com.simplotel.resources.database.DatabaseAccessService;
import com.simplotel.resources.model.SimplotelTask;

public class SimplotelTaskService {
	private final int  userid;
	
	public SimplotelTaskService(int uid)
	{
		userid = uid;
	}
	
	
	static final String table_Name = "simploteltasks.tasks";
	static final int page_size = 50;
	static final int page_start = 0;
	
	
	
	//construcint response for list of tasks
	public List<SimplotelTask> constructResponseForAllTasks(ResultSet resultSet) throws SQLException
	{
		ArrayList<SimplotelTask> tasksList = new ArrayList<SimplotelTask>();
		
    	 while (resultSet.next()) 
		{
			SimplotelTask st = new SimplotelTask();
			String temp;
            
            st.setId(resultSet.getInt("taskid"));
            st.setUserid(resultSet.getInt("userid"));
            st.setName(resultSet.getString("name"));
            st.setDescription(resultSet.getString("description"));
            st.setPriority(resultSet.getInt("priority"));
            st.setState(resultSet.getInt("state"));
            
            temp = resultSet.getString("created");
            temp = temp.substring(0, temp.length() - 2);
            st.setCreated(temp);
            
            temp = resultSet.getString("last_modified");
            temp = temp.substring(0, temp.length() - 2);
            st.setLastmodified(temp);
            
            temp = resultSet.getString("due_date");
            temp = temp.substring(0, temp.length() - 2);
            st.setDuedate(temp);
            
            temp = resultSet.getString("deleted");
            if(temp!=null)
            {
            	temp = temp.substring(0, temp.length() - 2);
                st.setDeleted(temp);
            }
            
            tasksList.add(st);
		}
		 
		 return tasksList;
	}
	
	
	//constructing response for single task details
	public SimplotelTask constructResponseForTask(ResultSet resultSet) throws SQLException
	{
		SimplotelTask st = new SimplotelTask();
		String temp;
    	    st.setId(resultSet.getInt("taskid"));
            st.setUserid(resultSet.getInt("userid"));
            st.setName(resultSet.getString("name"));
            st.setDescription(resultSet.getString("description"));
            st.setPriority(resultSet.getInt("priority"));
            st.setState(resultSet.getInt("state"));
           
            temp = resultSet.getString("created");
            temp = temp.substring(0, temp.length() - 2);
            st.setCreated(temp);
            
            temp = resultSet.getString("last_modified");
            temp = temp.substring(0, temp.length() - 2);
            st.setLastmodified(temp);
            
            temp = resultSet.getString("due_date");
            temp = temp.substring(0, temp.length() - 2);
            st.setDuedate(temp);
            
            temp = resultSet.getString("deleted");
            if(temp!=null)
            {
            	temp = temp.substring(0, temp.length() - 2);
                st.setDeleted(temp);
            }
            
		 return st;
	}
	
	
	//connecting to database and fetching tasks
	public List<SimplotelTask> getalltasks(
			int page, int pageSize,String deleted,
			String sortby,String sort_order,
			int state,int priority,
			String created_start,String created_end,
			String duedate_start,String duedate_end,
			String last_modified_start,String last_modified_end,
			String deleted_start,String deleted_end	) throws SQLException
	{
		Connection con= new DatabaseAccessService().getDatabaseConnection();
		StringBuffer query = new StringBuffer("select * from ");
		query.append(table_Name );
		
		query.append(" where userid = ? ");//1
		
		if(!(deleted.equalsIgnoreCase("true")))query.append(" and deleted is  NULL ");//checking for delted query param
		
		ArrayList<Integer> states = new ArrayList<Integer>();  // state filter apply  
		states.add(1);
		states.add(2);
		states.add(3);
		if(states.contains(state))query.append(" and  state = "+ state);
		
		ArrayList<Integer> priorities = new ArrayList<Integer>(); //priority filter apply 
		priorities.add(1);
		priorities.add(3);
		priorities.add(5);
		if(priorities.contains(priority))query.append(" and  priority = "+ priority);
		
		
		//all date filters for respective column
		query.append(" and ( created between  ? and ? ) ");//2,3
		query.append(" and ( due_date between  ? and ? ) ");//4,5
		query.append(" and ( last_modified between  ? and ? ) ");//6,7
		
		boolean deleted_range_enable = false; 
		if((!deleted_start.equals("0000-00-00")) || (!deleted_end.equals("9999-12-31"))){
			deleted_range_enable = true;
			query.append(" and ( deleted between  ? and ? ) ");//8,9
		}
		
		
		ArrayList<String> orderbyclumns = new ArrayList<String>();
		orderbyclumns.add("priority");
		orderbyclumns.add("state");
		orderbyclumns.add("created");
		if(orderbyclumns.contains(sortby))query.append(" order by "+  sortby);
		else query.append(" order by  due_date ");	
		
		String desc = "DESC";
		if(sort_order.equalsIgnoreCase(desc))
			query.append(" DESC ");
		
		int pagecal = 	page * pageSize; //because LIMIT will take ----->base,next_rows_count
		query.append(" LIMIT " + pagecal + "," + pageSize ); // paging concept using LIMIT
		
		
		PreparedStatement statement = con.prepareStatement(query.toString());    
		
		statement.setInt(1,userid);    
		
		statement.setString(2,created_start);
		statement.setString(3,created_end);
		
		statement.setString(4,duedate_start);
		statement.setString(5,duedate_end);
		
		statement.setString(6,last_modified_start);
		statement.setString(7,last_modified_end);
		
		if(deleted_range_enable)
		{
			statement.setString(8,deleted_start);
			statement.setString(9,deleted_end);
		}
			
			
		ResultSet resultSet = statement.executeQuery();
		List<SimplotelTask> tasksList=  constructResponseForAllTasks(resultSet);//forming response
		con.close();
		return tasksList;
	}
	
	
	//fetching single task from database
	public SimplotelTask getTask(int tid ,Connection con) throws SQLException
	{
		if(con == null)con= new DatabaseAccessService().getDatabaseConnection();
		StringBuffer query = new StringBuffer("select * from");
		query.append(" " +table_Name );
		query.append(" where taskid = ?");
		query.append(" and userid = ? ");
		PreparedStatement statement = con.prepareStatement(query.toString());    
		statement.setInt(1,tid);  
		statement.setInt(2,userid);
		ResultSet resultSet = statement.executeQuery();
		if (!resultSet.next()){
			throw new NotFoundException("Task is not found with the id: " + tid );
			}
		SimplotelTask task=  constructResponseForTask(resultSet);
		con.close();
		return task;
	}
	
	//adding new task to database
	synchronized public  SimplotelTask addTask(SimplotelTask input) throws SQLException
	{
		int rowCount = 0;
		int status  = 2;
		SimplotelTask parsedData = simplotelTaskUtil.validateRequestData(input);
		
		Connection con= new DatabaseAccessService().getDatabaseConnection();
		
		//taking row count
		PreparedStatement st = con.prepareStatement("SELECT COUNT(*) FROM " + table_Name);
		ResultSet rs = st.executeQuery();
	    rs.next();
	    rowCount = rs.getInt(1);
		
	  //insert into simploteltasks.tasks values(taskid,userid,name,description,priority,state,created,last_modified,due_date,deleted);
		try
		{
			StringBuffer query = new StringBuffer("insert into ");
			query.append(table_Name);
			query.append(" values(?,?,?,?,?,?,now(),now(),?,NULL)");
			PreparedStatement statement = con.prepareStatement(query.toString());    
			
			statement.setInt(1,++rowCount);  
			statement.setInt(2,userid);
			statement.setString(3,parsedData.getName());
			statement.setString(4,parsedData.getDescription());
			statement.setInt(5,parsedData.getPriority());
			statement.setInt(6,parsedData.getState());
			statement.setString(7,parsedData.getDuedate());
			status = statement.executeUpdate();
			//con.close();
		}
		catch(BadRequestException bre)
		{
			con.close();
			throw new BadRequestException("please validate your requsert");
		}
	    
         if (status == 0) {
          //Failure
        	con.close();
        	throw new BadRequestException("please validate your requsert");
        }
        	 
		 return getTask(rowCount ,con);
	}
	
	//updating existing task
	synchronized public  SimplotelTask updateTask(int tid, SimplotelTask input) throws SQLException
	{
		int status  = 2;
		SimplotelTask parsedData = simplotelTaskUtil.validateRequestData(input);
		
		Connection con= new DatabaseAccessService().getDatabaseConnection();
		
		//taking row count
		StringBuffer qry = new StringBuffer("SELECT taskid FROM ");
		qry.append(table_Name);
		qry.append(" where taskid = " + tid);
		qry.append(" and userid = "+ userid);
		qry.append(" and deleted is null");
		PreparedStatement st = con.prepareStatement(qry.toString());
		ResultSet rs = st.executeQuery();
	    if(!rs.next()) throw new NotFoundException("Task not found with id : " + tid);
		
	  //insert into simploteltasks.tasks values(taskid,userid,name,description,priority,state,created,last_modified,due_date,deleted);
		try
		{
			StringBuffer query = new StringBuffer("update ");
			query.append(table_Name);
			query.append(" set");
			
			query.append(" name= ? ");
			query.append(" ,description = ?");
			query.append(" ,priority = ?");
			query.append(" ,state = ?");
			query.append(" ,last_modified = now()");
			query.append(" ,due_date  = ?");
			
			query.append(" where taskid = ?");
			
			PreparedStatement statement = con.prepareStatement(query.toString());    
			
			statement.setString(1,parsedData.getName());
			statement.setString(2,parsedData.getDescription());
			statement.setInt(3,parsedData.getPriority());
			statement.setInt(4,parsedData.getState());
			statement.setString(5,parsedData.getDuedate());
			statement.setInt(6,tid);
			status = statement.executeUpdate();
			//con.close();
		}
		catch(BadRequestException bre)
		{
			con.close();
			throw new BadRequestException("please validate your requsert");
		}
	    
         if (status == 0) {
          //Failure
        	con.close();
        	throw new BadRequestException("please validate your requsert");
        }
        	 
		 return getTask(tid, con);
	}
	
	
	//marking as delete in database
	synchronized public  SimplotelTask deleteTask(int tid) throws SQLException
	{
		int status  = 2;
		
		Connection con= new DatabaseAccessService().getDatabaseConnection();
		
		StringBuffer qry = new StringBuffer("SELECT taskid FROM ");
		qry.append(table_Name);
		qry.append(" where taskid = " + tid);
		qry.append(" and userid = "+ userid);
		qry.append(" and deleted is null");
		PreparedStatement st = con.prepareStatement(qry.toString());
		ResultSet rs = st.executeQuery();
	    if(!rs.next()) throw new NotFoundException("Task not found with id : " + tid);
		
		try
		{
			StringBuffer query = new StringBuffer("update ");
			query.append(table_Name);
			query.append(" set");
			
			query.append(" deleted= now() ");
			query.append(" where taskid = ?");
			PreparedStatement statement = con.prepareStatement(query.toString());    
			statement.setInt(1, tid);;
			
			status = statement.executeUpdate();
			//con.close();
		}
		catch(BadRequestException bre)
		{
			con.close();
			throw new BadRequestException("please validate your requsert");
		}
	    
         if (status == 0) {
          //Failure
        	con.close();
        	throw new SQLException("database error");
        }
        	 
		 return getTask(tid, con);
	}
	
	
	
	
	
}

