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
		
		private boolean changed = true;
		
		private int position = -1;
		
	

		public void setPosition(int position) {
			
			if (position !=this.position)
				changed = true;
			
			this.position = position;

		}

		public WordBinding(WordList wordList){
			this.wordList = wordList;
			
		}

		public Word getWordA() {
			return wordA;
		}

		public void setWordA(Word wordA) {
			this.wordA = wordA;
			
			changed = true;
		}

		public Word getWordB() {
			return wordB;
		}

		public void setWordB(Word wordB) {
			this.wordB = wordB;
			
			changed = true;
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

			changed = false;
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
			
			changed = false;
		}

		public void removeFromDatabase() throws Exception {
			if(databaseID == -1)
				return;
			
			Connection conn = DatabaseHelper.createConnection();
			Statement stmt = conn.createStatement();

			stmt.execute("delete from WORD_LIST_WORDS where id=" + databaseID);
			

			stmt.close();
			
			changed = true;
		}

		public void saveToDatabase() throws Exception {
			
			if (changed == false)
				return;
			
			Connection conn = DatabaseHelper.createConnection();

			String sql = "update WORD_LIST_WORDS set "+
				"word_list_id=?," +
				"wordAID=?," +
				"wordBID=?," +
				"position=?," +
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
			stmt.setInt(4, position);
			stmt.setInt(5, 1);
			stmt.setInt(6, databaseID);
			
			stmt.executeUpdate();
			
			stmt.close();

			
			changed = false;
		}

		public void updateFromDatabase() throws Exception {
		
			loadFromDatabase(databaseID);
			
			changed = false;
			
		}

		public void deAttachFromDatabase() throws Exception{
			databaseID = -1;

			if(wordA!=null)
				wordA.deAttachFromDatabase();
			
			if(wordB!=null)
				wordB.deAttachFromDatabase();
			
			changed = true;
		}
	
}
