/**
 * WLTDatabaseStorable instances can be stored in a WLTDatabase
 */
package org.wlt.data;

/**
 * @author kjellw
 *
 */
public interface WLTDatabaseStorable {
	
	
	public void saveToDatabase() throws Exception;
	
	public void updateFromDatabase() throws Exception;
	
	public void createNewInDatabase() throws Exception;
	
	public void loadFromDatabase(int databaseID) throws Exception;
	
	public void removeFromDatabase() throws Exception;
	
	public int getDatabaseID();
	
	public void deAttachFromDatabase();
	
}
