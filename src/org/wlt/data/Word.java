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
	
	private boolean changed = true;

	/** 
	 * @see org.wlt.data.WLTDatabaseStorable#saveToDatabase()
	 */
	public void saveToDatabase() throws Exception {
		
		if(changed == false)
			return;
		
		Connection conn = DatabaseHelper.createConnection();

		String sql = "update WORDS set "+
			"word=?," +
			"language=?," +
			"wordcomment=?," +
			(soundFile != null ? "sound=?," : "") +
			"dbVersion=?" +
			" where id=?";
		
		PreparedStatement stmt = conn.prepareStatement(sql);

		int i = 0;
		i++;
		stmt.setString(i, word);
		i++;
		stmt.setString(i, language);
		i++;
		stmt.setString(i, comment);
		
		if(soundFile != null){
			

			i++;
			stmt.setBytes(i, soundFile);
			soundFile = null;
		}
		
		i++;
		stmt.setInt(i, 0);
		i++;
		stmt.setInt(i, databaseID);
		
		stmt.executeUpdate();

		stmt.close();
		
		changed = false;

	}

	/**
	 * @see org.wlt.data.WLTDatabaseStorable#updateFromDatabase()
	 */
	public void updateFromDatabase() throws Exception {
		loadFromDatabase(databaseID);
		changed = false;
	}
	
	public void createNewInDatabase() throws Exception{
		
		Connection conn = DatabaseHelper.createConnection();
		
		String sql = "insert into WORDS(word) values(NULL)";
		
		PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		stmt.executeUpdate();
		
		ResultSet rs = stmt.getGeneratedKeys();
		
		if(rs.next())
			databaseID = rs.getInt(1);
		
		stmt.close();
		
		saveToDatabase();
		
		changed = false;
		
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
		
		//soundFile = result.getBytes("sound");

		}
		
		stmt.close();
		
		changed = false;
		
	}

	public String getWord() {
		return word;
	}



	public void setWord(String word) {
		this.word = word;
		
		changed = true;
	}



	public byte[] getSoundFile() throws Exception{
		
		if(databaseID == -1)
			return soundFile;
		else{
			//Load from database
			
			Connection conn = DatabaseHelper.createConnection();
			
			Statement stmt = conn.createStatement();
			
			ResultSet result = stmt.executeQuery("select * from WORDS where id=" + databaseID);
			
			byte[] sound = null;
		
			if(result.next()){

				sound = result.getBytes("sound");

			}
			
			stmt.close();
			
			return sound;
			
		}
	}



	public void setSoundFile(byte[] soundFile) throws Exception{
		if(databaseID == -1)
			this.soundFile = soundFile;
		else{
			//Save to database
			
			Connection conn = DatabaseHelper.createConnection();

			String sql = "update WORDS set "+
				 "sound=?" +
				" where id=?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setBytes(1, soundFile);
			
			stmt.setInt(2, databaseID);
			
			stmt.executeUpdate();

			stmt.close();
		}
		
		changed = true;
	}



	public String getLanguage() {
		return language;
	}



	public void setLanguage(String language) {
		this.language = language;
		changed = true;
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
		
		changed = true;
		
	}

	@Override
	public String toString() {
		
		return word;
	}

	public void deAttachFromDatabase() throws Exception{

		soundFile = getSoundFile();

		databaseID = -1;
		
		changed = true;
		
	}

	

}
