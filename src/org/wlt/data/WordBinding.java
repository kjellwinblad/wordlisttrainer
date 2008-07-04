package org.wlt.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.wlt.data.database.DatabaseHelper;

public class WordBinding implements WLTDatabaseStorable{

		private int databaseID = -1;
	
		private Word wordA;
		
		private Word wordB;
		
		private WordList wordList;
		
		public WordBinding(WordList wordList){
			this.wordList = wordList;
			
		}

		public Word getWordA() {
			return wordA;
		}

		public void setWordA(Word wordA) {
			this.wordA = wordA;
		}

		public Word getWordB() {
			return wordB;
		}

		public void setWordB(Word wordB) {
			this.wordB = wordB;
		}

		public void createNewInDatabase() throws Exception {
			Connection conn = DatabaseHelper.createConnection();

			String sql = "insert into WORD_LIST_WORDS(word_list_id) values(NULL)";

			PreparedStatement stmt = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);



			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next())
				databaseID = rs.getInt(1);

			stmt.close();

			saveToDatabase();
			
		}

		public int getDatabaseID() {
			
			return databaseID;
		}

		public void loadFromDatabase(int databaseID) throws Exception {
			
			this.databaseID = databaseID;
			
			Connection conn = DatabaseHelper.createConnection();
			Statement stmt = conn.createStatement();

			
			ResultSet result = stmt
					.executeQuery("select * from WORD_LIST_WORDS where id="
							+ databaseID);

			if(result.next()) {
				Word wA = new Word();
				Word wB = new Word();

				wA.loadFromDatabase(result.getInt("wordAID"));
				wB.loadFromDatabase(result.getInt("wordBID"));

				setWordA(wA);
				setWordB(wB);
				
			}

			stmt.close();
			
		}

		public void removeFromDatabase() throws Exception {
			if(databaseID == -1)
				return;
			
			Connection conn = DatabaseHelper.createConnection();
			Statement stmt = conn.createStatement();

			stmt.execute("delete from WORD_LIST_WORDS where id=" + databaseID);
			

			stmt.close();
			
		}

		public void saveToDatabase() throws Exception {
			Connection conn = DatabaseHelper.createConnection();

			String sql = "update WORD_LIST_WORDS set "+
				"word_list_id=?," +
				"wordAID=?," +
				"wordBID=?," +
				"dbVersion=?" +
				" where id=?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);

			if(wordA.getDatabaseID()==-1)
				wordA.createNewInDatabase();
			else
				wordA.saveToDatabase();
			
			if(wordB.getDatabaseID()==-1)
				wordB.createNewInDatabase();
			else
				wordB.saveToDatabase();
			

			stmt.setInt(1, wordList.getDatabaseID());
			stmt.setInt(2, wordA.getDatabaseID());
			stmt.setInt(3, wordB.getDatabaseID());
			stmt.setInt(4, 0);
			stmt.setInt(5, databaseID);
			
			stmt.executeUpdate();
			
			stmt.close();
			
		}

		public void updateFromDatabase() throws Exception {
		
			loadFromDatabase(databaseID);
			
		}

		public void deAttachFromDatabase() throws Exception{
			databaseID = -1;
			System.out.println("WORD BINDING DEATACH");
			if(wordA!=null)
				wordA.deAttachFromDatabase();
			
			if(wordB!=null)
				wordB.deAttachFromDatabase();
		}
	
}
