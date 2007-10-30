/**
 * 
 */
package org.wlt.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.wlt.data.database.DatabaseHelper;

/**
 * @author kjellw
 *
 */
public class Word implements WLTDatabaseStorable {

	
	private int databaseID = -1;
	
	private String word;
	
	private byte[] soundFile;
	
	private String language;
	
	private String comment;
	

	/** 
	 * @see org.wlt.data.WLTDatabaseStorable#saveToDatabase()
	 */
	public void saveToDatabase() throws Exception {
		Connection conn = DatabaseHelper.createConnection();

		String sql = "update WORDS set "+
			"word=?," +
			"language=?," +
			"wordcomment=?," +
			"sound=?" +
			" where id=?";
		
		PreparedStatement stmt = conn.prepareStatement(sql);

		stmt.setString(1, word);
		stmt.setString(2, language);
		stmt.setString(3, comment);
		stmt.setBytes(4, soundFile);
		stmt.setInt(5, databaseID);
		stmt.executeUpdate();

		stmt.close();

	}

	/**
	 * @see org.wlt.data.WLTDatabaseStorable#updateFromDatabase()
	 */
	public void updateFromDatabase() throws Exception {
		loadFromDatabase(databaseID);
	}
	
	public void createNewInDatabase() throws Exception{
		Connection conn = DatabaseHelper.createConnection();
		System.out.println("new");
		String sql = "insert into WORDS(word) values(NULL)";
		
		PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		stmt.executeUpdate();
		
		ResultSet rs = stmt.getGeneratedKeys();
		if(rs.next())
			databaseID = rs.getInt(1);
		
		stmt.close();
		
		saveToDatabase();
		
	}

	public void loadFromDatabase(int databaseID) throws Exception{
		Connection conn = DatabaseHelper.createConnection();
		Statement stmt = conn.createStatement();
		
		
		
		this.databaseID = databaseID;
		
		ResultSet result = stmt.executeQuery("select * from WORDS where id=" + databaseID);
		
		if(result.next()){
		
		this.word = result.getString("word");
		
		this.language = result.getString("language");
		
		this.comment = result.getString("wordcomment");
		
		soundFile = result.getBytes("sound");

		}
		
		stmt.close();
		
	}

	public String getWord() {
		return word;
	}



	public void setWord(String word) {
		this.word = word;
	}



	public byte[] getSoundFile() {
		return soundFile;
	}



	public void setSoundFile(byte[] soundFile) {
		this.soundFile = soundFile;
	}



	public String getLanguage() {
		return language;
	}



	public void setLanguage(String language) {
		this.language = language;
	}



	public int getDatabaseID() {
		return databaseID;
	}

	public void removeFromDatabase() throws Exception {
		if(databaseID == -1)
			return;
		Connection conn = DatabaseHelper.createConnection();
		Statement stmt = conn.createStatement();
		
		stmt.execute("delete from WORDS where id=" + databaseID );
		
		conn.close();
		
	}

	@Override
	public String toString() {
		
		return word;
	}

	

}
