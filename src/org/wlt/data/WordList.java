package org.wlt.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.wlt.data.database.DatabaseHelper;

public class WordList implements WLTDatabaseStorable {

	private int databaseID = -1;

	private String wordListName;

	private String languageA;

	private String languageB;

	private List<WordBinding> wordBindings = new LinkedList<WordBinding>();

	private boolean wordBindingsInit = false;

	private int numberOfWordBindings = -1;

	public void saveToDatabase() throws Exception {
		// TODO Auto-generated method stub
		Connection conn = DatabaseHelper.createConnection();

		String sql = "update WORD_LISTS set " + "word_list_name=?,"
				+ "languageA=?," + "languageB=?," + "dbVersion=?"
				+ " where id=?";

		PreparedStatement stmt = conn.prepareStatement(sql);

		stmt.setString(1, wordListName);
		stmt.setString(2, languageA);
		stmt.setString(3, languageB);
		stmt.setInt(4, 1);
		stmt.setInt(5, databaseID);
		stmt.executeUpdate();

		stmt.close();

		if (wordBindingsInit == false)
			return;
		int counter = 0;
		for (WordBinding wb : wordBindings) {

			Word wordA = wb.getWordA();
			Word wordB = wb.getWordB();

			if (wordA == null) {
				wordA = new Word();
				wb.setWordA(wordA);
			}

			if (wordA.getDatabaseID() == -1)
				wordA.createNewInDatabase();
			else
				wordA.saveToDatabase();

			if (wordB == null) {
				wordB = new Word();
				wb.setWordB(wordB);
			}

			if (wordB.getDatabaseID() == -1)
				wordB.createNewInDatabase();
			else
				wordB.saveToDatabase();

			wb.setPosition(counter);

			if (wb.getDatabaseID() == -1)
				wb.createNewInDatabase();
			else
				wb.saveToDatabase();

			counter = counter + 1;

		}
	}

	public void updateFromDatabase() throws Exception {

		loadFromDatabase(databaseID);

	}

	public void createNewInDatabase() throws Exception {
		Connection conn = DatabaseHelper.createConnection();

		String sql = "insert into WORD_LISTS(word_list_name) values(NULL)";

		PreparedStatement stmt = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);

		stmt.executeUpdate();

		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next())
			databaseID = rs.getInt(1);

		stmt.close();

		saveToDatabase();

	}

	public void loadFromDatabase(int databaseID) throws Exception {
		Connection conn = DatabaseHelper.createConnection();

		this.databaseID = databaseID;
		String sql = "select * from WORD_LISTS where id=" + databaseID;

		PreparedStatement stmt = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);

		ResultSet result = stmt.executeQuery();
		if (result.next()) {

			this.wordListName = result.getString("word_list_name");

			this.languageA = result.getString("languageA");

			this.languageB = result.getString("languageB");

		}

		stmt.close();

		Statement stmt2 = conn.createStatement();

		ResultSet result2 = stmt2
				.executeQuery("select count(id) from WORD_LIST_WORDS where word_list_id="
						+ databaseID);

		result2.next();
		numberOfWordBindings = result2.getInt(1);

		stmt2.close();
		wordBindings = new LinkedList<WordBinding>();
		wordBindingsInit = false;

	}

	public int getDatabaseID() {
		// TODO Auto-generated method stub
		return databaseID;
	}

	public String getWordListName() {
		return wordListName == null ? "" : wordListName;
	}

	public void setWordListName(String wordListName) {
		this.wordListName = wordListName;
	}

	public String getLanguageA() {
		return languageA;
	}

	public void setLanguageA(String languageA) {
		if (languageA.length() > 1)
			this.languageA = languageA.substring(0, 1).toUpperCase()
					+ languageA.substring(1, languageA.length()).toLowerCase();
		else
			this.languageA = languageA;

		for (WordBinding a : wordBindings)
			a.getWordA().setLanguage(this.languageA);
	}

	public String getLanguageB() {
		return languageB;
	}

	public void setLanguageB(String languageB) {
		if (languageB.length() > 1)
			this.languageB = languageB.substring(0, 1).toUpperCase()
					+ languageB.substring(1, languageB.length()).toLowerCase();
		else
			this.languageB = languageB;

		for (WordBinding b : wordBindings)
			b.getWordB().setLanguage(this.languageB);
	}

	public void removeFromDatabase() throws Exception {
		if (databaseID == -1)
			return;
		Connection conn = DatabaseHelper.createConnection();
		Statement stmt = conn.createStatement();

		stmt.execute("delete from WORD_LISTS where id=" + databaseID);

		for (WordBinding wb : wordBindings)
			wb.removeFromDatabase();

		stmt.close();

	}

	public static List<WordList> getWordListList() throws Exception {
		Connection conn = DatabaseHelper.createConnection();

		String sql = "select id from WORD_LISTS";

		Statement stmt = conn.createStatement();

		ResultSet result = stmt.executeQuery(sql);
		List<WordList> resultList = new LinkedList<WordList>();

		WordList wl;

		while (result.next()) {

			int id = result.getInt("id");
			wl = new WordList();
			wl.loadFromDatabase(id);
			resultList.add(wl);

		}

		stmt.close();

		return resultList;
	}

	public List<WordBinding> getWordBindings() throws Exception {

		if (wordBindingsInit == false) {

			Connection conn = DatabaseHelper.createConnection();

			// Create the word bindings
			Statement stmt2 = conn.createStatement();

			ResultSet result2 = stmt2
					.executeQuery("select id from WORD_LIST_WORDS where word_list_id="
							+ databaseID + "order by position ASC");

			while (result2.next()) {
				WordBinding wordBinding = new WordBinding(this);

				wordBinding.loadFromDatabase(result2.getInt("id"));

				wordBindings.add(wordBinding);

			}

			stmt2.close();
			wordBindingsInit = true;
		}
		return wordBindings;
	}

	public void deAttachFromDatabase() throws Exception {
		getWordBindings();

		databaseID = -1;

		// wordBindingsInit = false;

		for (WordBinding b : wordBindings)
			b.deAttachFromDatabase();

	}

	public int getNumberOfWordBindings() {
		// TODO Auto-generated method stub
		
		//if(numberOfWordBindings == -1)
		{
		Statement stmt2;
		try {
			stmt2 = DatabaseHelper.createConnection().createStatement();
		
			ResultSet result2 = stmt2
			.executeQuery("select count(id) from WORD_LIST_WORDS where word_list_id="
					+ databaseID);

	result2.next();
	numberOfWordBindings = result2.getInt(1);

	stmt2.close();
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return numberOfWordBindings;
	}

	public boolean sameLanguagesAs(WordList wl) {

		if ((languageA.toLowerCase().equals(wl.getLanguageA().toLowerCase()) && languageB
				.toLowerCase().equals(wl.getLanguageB().toLowerCase()))
				|| (languageA.toLowerCase().equals(
						wl.getLanguageB().toLowerCase()) && languageB
						.toLowerCase().equals(wl.getLanguageA().toLowerCase())))
			return true;
		else
			return false;

	}
	
	/**
	 * This can only be called if the binding has the same languages as this word list
	 */
	public void addWordBinding(WordBinding b) throws Exception{
		WordBinding newB = new WordBinding(this);

		
		if(b.getWordA().getLanguage().toLowerCase().equals(getLanguageA().toLowerCase())){
			newB.setWordA(b.getWordA());
			newB.setWordB(b.getWordB());
		}else if(b.getWordA().getLanguage().toLowerCase().equals(getLanguageB().toLowerCase())){
			newB.setWordB(b.getWordA());
			newB.setWordA(b.getWordB());
		}
		newB.setPosition(getNumberOfWordBindings());
		System.out.println("POS  ");
		System.out.println(getNumberOfWordBindings());
		newB.createNewInDatabase();
		
	}

	@Override
	public String toString() {
		
		return getWordListName();
	}
	
	

}
