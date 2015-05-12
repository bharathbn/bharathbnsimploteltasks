package com.simplotel.resources.service;

import java.util.Date;

import javax.ws.rs.BadRequestException;

import com.simplotel.resources.model.SimplotelTask;

public class simplotelTaskUtil {

	
public static SimplotelTask validateRequestData(SimplotelTask input)
{
	
	String name,description;
	int state, priority;
	String due_date;
	try
	{
		 name = input.getName();
         description = input.getDescription();
         priority = input.getPriority();
         state = input.getState();
         due_date = input.getDuedate();
	}
    catch(BadRequestException bre)
	{
    	throw new BadRequestException("please validate your data in request body");
	}
	
	if(!(name.length() > 0)) throw new BadRequestException("please provice valid name for Task");
	if(due_date == null)throw new BadRequestException("please provice valid due date for Task");
	
	switch(priority){
	case 1:break;
	case 3:break;
	case 5:break;
	default : priority=3;
	break;
	}
	
	input.setPriority(priority);
	switch(state){
	case 1:break;
	case 2:break;
	case 3:break;
	default : state=3;
	break;
	}
	input.setState(state);
	
	return input;
}
	
	
	
	
	
}
