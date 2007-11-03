package org.wlt.settings;

public class NetworkDatabaseSettings {

	String host = "localhost";
	long portNumber = 1527;
	String databaseName = "DB"; 
	
	
	String userName = "me";
	String password = "mine";

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
		DatabaseSettings.getInstance().saveToPropertiesFile();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
		DatabaseSettings.getInstance().saveToPropertiesFile();
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
		DatabaseSettings.getInstance().saveToPropertiesFile();
	}
	public long getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(long portNumber) {
		this.portNumber = portNumber;
		DatabaseSettings.getInstance().saveToPropertiesFile();
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
		DatabaseSettings.getInstance().saveToPropertiesFile();
	}
	
	
	
	
}
