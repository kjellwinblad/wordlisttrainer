package org.wlt.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

	public static final String dbURL = 
	"jdbc:derby://localhost:1527/DB;create=true;user=me;password=mine";
	private static Connection conn = null;
	
    public static Connection createConnection()
    {
    	if(conn == null){
    	
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
            return conn;
        }
        catch (Exception except)
        {
            except.printStackTrace();
            return null;
        }
        
    	}else
    		return conn;
    }
    
    
    public static void shutdown()
    {
        try
        {

            if (conn != null)
            {
                conn.close();
                conn = null;
            }           
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }

    }
}
