package com.simplotel.resources.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.appengine.api.utils.SystemProperty;

public class DatabaseAccessService 
{
	String url = null;  
	Connection conn;
	public DatabaseAccessService(){};
	
public Connection getDatabaseConnection() throws SQLException
{
	try {
	      if (SystemProperty.environment.value() ==
	          SystemProperty.Environment.Value.Production) 
	      {
	        Class.forName("com.mysql.jdbc.GoogleDriver");
	        url = "jdbc:google:mysql://bharathsimplotel:tasksdatabase/simploteltasks?user=root";
	        //"jdbc:google:mysql://your-project-id:your-instance-name/database", "user", "password");
	      } 
	      else 
	      {
	        Class.forName("com.mysql.jdbc.Driver");
	        url = "jdbc:mysql://173.194.243.27:3306/simploteltasks?user=root";
	      }
	      
	      conn = DriverManager.getConnection(url);//,"bharath","simplotel");
	      
	    } 
		catch (Exception e) {
	      e.printStackTrace();
	      throw new SQLException("database connection Error");
	    }
	
	return conn;
}
	
	
}
