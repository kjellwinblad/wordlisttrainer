package org.wlt.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.wlt.settings.DatabaseSettings;



public class DatabaseHelper {

	public enum DatabaseMode { Local, Network}
	
	private static DatabaseMode dbMode = DatabaseMode.Local;
	
	private static String dbURL;
	private static Connection conn = null;
	
	private static String driver;
	private static String localDBURL;
	private static String localDriver;
	private static String networkDriver;
	private static String networkDBURL;
	private static Connection localConn;
	private static Connection networkConn;
	
	
	public static String  CREATE_WORD_LIST_WORDS_STATMENT =
	"CREATE TABLE WORD_LIST_WORDS (" + 
	"		  id INTEGER GENERATED ALWAYS AS IDENTITY," +
	"		  word_list_id INTEGER," +
	"		  wordAID INTEGER," +
	"		  wordBID INTEGER," +
	"         position INTEGER," +
	"		  dbVersion INTEGER," +
	"		PRIMARY KEY(id))";

	public static String  CREATE_WORD_LISTS_STATMENT = 
	"		CREATE TABLE WORD_LISTS (" +
	"		  id INTEGER GENERATED ALWAYS AS IDENTITY," +
	"		  word_list_name CLOB," +
	"		  languageA CLOB," + 
	"		  languageB CLOB," +
	"		  dbVersion INTEGER," +
	"		  PRIMARY KEY(id))";

	public static String  CREATE_WORDS_STATMENT = 
	"		CREATE TABLE WORDS (" +
	"		  id INTEGER GENERATED ALWAYS AS IDENTITY," +
	"		  word CLOB," +
	"		  language CLOB," +
	"		  wordcomment CLOB," +
	"		  sound BLOB," +
	"		  dbVersion INTEGER," +
	"		PRIMARY KEY(id))";
	
	
	
	public static void setDatabaseSettings(DatabaseSettings databaseSettings){
		
		
		try {
			shutdown();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			shutdownLocal();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			shutdownNetwork();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		setLocalConfig();
		setNetworkConfig(databaseSettings);
		
		if(databaseSettings.isCurrentDatabaseModeLocal()){
			dbURL =  localDBURL;
			driver = localDBURL;
			dbMode = DatabaseMode.Local;
		}else{
			dbURL =  networkDBURL;
			driver = networkDBURL;
			dbMode = DatabaseMode.Network;
		}
		
	}
	
    private static void setNetworkConfig(DatabaseSettings databaseSettings) {
		networkDriver = "org.apache.derby.jdbc.ClientDriver";
		networkDBURL = "jdbc:derby://" +
		databaseSettings.getNetworkDatabaseSettings().getHost() +
		":" +
		databaseSettings.getNetworkDatabaseSettings().getPortNumber() +
		"/" + 
		databaseSettings.getNetworkDatabaseSettings().getDatabaseName() + 
		";create=true;user=" +
		databaseSettings.getNetworkDatabaseSettings().getUserName() +
		";password=" +
		databaseSettings.getNetworkDatabaseSettings().getPassword();
		
	}

	private static void setLocalConfig() {
    	localDBURL =  "jdbc:derby:" + DatabaseSettings.getInstance().getLocalDatabaseLocation().getAbsolutePath() + ";create=true";
		localDriver = "org.apache.derby.jdbc.EmbeddedDriver";
	}

	public static Connection createConnection()
    throws Exception{
		
    	if(dbMode == DatabaseMode.Local)
    		return createLocalConnection();
    	else
    		return createNetworkConnection();
    
	}

	public static Connection createLocalConnection()
    throws Exception{
	    	if(localConn == null){
	    		localConn = createConnection(localDBURL, localDriver);
	    		return localConn;
	    	}else
	    		return localConn;
    }
	
	public static Connection createNetworkConnection()
    throws Exception{
	    	if(networkConn == null){
	    		networkConn = createConnection(networkDBURL, networkDriver);
	    		return networkConn;
	    	}else
	    		return networkConn;
    }
	
	public static Connection createConnection(String dbURL, String driver)
    throws Exception{

        try
        {

            Class.forName(driver).newInstance();

            return DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
            throw except;
        }

    }
    
    
    public static void shutdown()
    {
    	shutdownConnection(conn);
    	conn = null;

    }
    
    public static void shutdownNetwork()
    {
    	shutdownConnection(networkConn);
    	networkConn = null;
    }
    
    public static void shutdownLocal()
    {
       shutdownConnection(localConn);
       localConn = null;

    }
    
    public static void shutdownConnection(Connection conn)
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

	public static DatabaseMode getDbMode() {
		return dbMode;
	}

	public static void setDbMode(DatabaseMode dbMode) {
		DatabaseHelper.dbMode = dbMode;
	}
	
	public static void createLocalDatabaseInDefaultLocation() throws Exception{
		Connection con = DatabaseHelper.createConnection();
		con.prepareStatement(DatabaseHelper.CREATE_WORD_LIST_WORDS_STATMENT).execute();
		con.prepareStatement(DatabaseHelper.CREATE_WORD_LISTS_STATMENT).execute();
		con.prepareStatement(DatabaseHelper.CREATE_WORDS_STATMENT).execute();
	}
}
