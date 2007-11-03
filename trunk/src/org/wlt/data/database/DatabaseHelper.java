package org.wlt.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.wlt.settings.DatabaseSettings;

public class DatabaseHelper {

	private static String dbURL = 
	"jdbc:derby://localhost:1527/DB;create=true;user=me;password=mine";
	private static Connection conn = null;
	
	private static String driver;
	
	
	public static void setDatabaseSettings(DatabaseSettings databaseSettings){
		if(databaseSettings.isCurrentDatabaseModeLocal()){
			System.out.println("LOCAL MODE");
			dbURL =  "jdbc:derby:" + "DB" + ";create=true";
			driver = "org.apache.derby.jdbc.EmbeddedDriver";
		}else{
			System.out.println("NETWORK MODE");
			driver = "org.apache.derby.jdbc.ClientDriver";
			dbURL = "jdbc:derby://" +
			databaseSettings.getNetworkDatabaseSettings().getHost() +
			":" +
			databaseSettings.getNetworkDatabaseSettings().getPortNumber() +
			"/DB;create=true;user=" +
			databaseSettings.getNetworkDatabaseSettings().getUserName() +
			";password=" +
			databaseSettings.getNetworkDatabaseSettings().getPassword();
			System.out.println(dbURL);
		}
		System.out.println(dbURL);
	}
	
    public static Connection createConnection()
    throws Exception{
    	if(conn == null){
    	
        try
        {
        	System.out.println(driver);
            Class.forName(driver).newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
            return conn;
        }
        catch (Exception except)
        {
            except.printStackTrace();
            throw except;
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
