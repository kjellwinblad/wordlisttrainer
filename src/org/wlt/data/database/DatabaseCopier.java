//<<<<<<< .mine
package org.wlt.data.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.wlt.data.WordList;
import org.wlt.gui.StatusNotifier;

public class DatabaseCopier {

	private DatabaseHelper.DatabaseMode from;

	private DatabaseHelper.DatabaseMode to;	
	
	private AtomicBoolean stopCoping = new AtomicBoolean(false);
	
	private StatusNotifier notifier;
 

	public DatabaseCopier(DatabaseHelper.DatabaseMode from, DatabaseHelper.DatabaseMode to, StatusNotifier notifer) {
		this.from = from;
		this.to = to;
		this.notifier = notifer;
		
	}

	
	public String getTargetName() {
		return from.toString();
	}



	public String getDestName() {
		return to.toString();
	}


	public void stopCoping(){
		stopCoping.set(true);
	}
	
	public void startCoping(List<WordList> selectedWordLists) throws Exception{
		stopCoping.set(false);
		
		//Connection fromConn = from.equals(DatabaseHelper.DatabaseMode.Local) ? DatabaseHelper.createLocalConnection() : DatabaseHelper.createNetworkConnection();
		
		//Connection toConn = to.equals(DatabaseHelper.DatabaseMode.Local) ? DatabaseHelper.createLocalConnection() : DatabaseHelper.createNetworkConnection();
		
		//Copy all wordlists
		
//		Statement stmt = fromConn.createStatement();
//
//		ResultSet result = stmt.executeQuery("select * from WORD_LISTS");
//		
//		if(result.next()){
//		
//		this.word = result.getString("word");
//		
//		this.language = result.getString("language");
//		
//		this.comment = result.getString("wordcomment");
//		
//		soundFile = result.getBytes("sound");
//
//		}
//		
//		stmt.close();
//		
//		
//		//Copy all word bindings
//		
//		//Copy all word bindings
		

		
		double process = 0.0;
		
		double processStep = 1.0 / (double)selectedWordLists.size();
		
		for(WordList l : selectedWordLists){
		
			
			
		DatabaseHelper.setDbMode(from);
		
		//l.updateFromDatabase();

		l.deAttachFromDatabase();
		
		DatabaseHelper.setDbMode(to);
		
		l.createNewInDatabase();
		

		
		notifier.setStatusMessage("Copying word list: " + l.getWordListName());
		
		notifier.setProcessStatus(process);
		
		process = process + processStep;
		
		if(stopCoping.get())
			break;
		
		}
		
		DatabaseHelper.setDbMode(from);
	}

	
	
}
//=======
//package org.wlt.data.database;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import org.wlt.data.WordList;
//import org.wlt.gui.StatusNotifier;
//
//public class DatabaseCopier {
//
//	private DatabaseHelper.DatabaseMode from;
//
//	private DatabaseHelper.DatabaseMode to;	
//	
//	private AtomicBoolean stopCoping = new AtomicBoolean(false);
//	
//	private StatusNotifier notifier;
// 
//
//	public DatabaseCopier(DatabaseHelper.DatabaseMode from, DatabaseHelper.DatabaseMode to, StatusNotifier notifer) {
//		this.from = from;
//		this.to = to;
//		this.notifier = notifer;
//		
//	}
//
//	
//	public String getTargetName() {
//		return from.toString();
//	}
//
//
//
//	public String getDestName() {
//		return to.toString();
//	}
//
//
//	public void stopCoping(){
//		stopCoping.set(true);
//	}
//	
//	public void startCoping(List<WordList> selectedWordLists) throws Exception{
//		stopCoping.set(false);
//		
//		//Connection fromConn = from.equals(DatabaseHelper.DatabaseMode.Local) ? DatabaseHelper.createLocalConnection() : DatabaseHelper.createNetworkConnection();
//		
//		//Connection toConn = to.equals(DatabaseHelper.DatabaseMode.Local) ? DatabaseHelper.createLocalConnection() : DatabaseHelper.createNetworkConnection();
//		
//		//Copy all wordlists
//		
////		Statement stmt = fromConn.createStatement();
////
////		ResultSet result = stmt.executeQuery("select * from WORD_LISTS");
////		
////		if(result.next()){
////		
////		this.word = result.getString("word");
////		
////		this.language = result.getString("language");
////		
////		this.comment = result.getString("wordcomment");
////		
////		soundFile = result.getBytes("sound");
////
////		}
////		
////		stmt.close();
////		
////		
////		//Copy all word bindings
////		
////		//Copy all word bindings
//		
//		System.out.print("FROM: " );
//		System.out.println(from);
//		System.out.print("TO: " );
//		System.out.println(to);
//		
//		double process = 0.0;
//		
//		double processStep = 1.0 / (double)selectedWordLists.size();
//		
//		for(WordList l : selectedWordLists){
//		
//			
//			
//		DatabaseHelper.setDbMode(from);
//		
//		l.updateFromDatabase();
//		
//		DatabaseHelper.setDbMode(to);
//		
//		l.deAttachFromDatabase();
//		
//		l.createNewInDatabase();
//		
//		System.out.print("COPIES: " );
//		System.out.println(l.getWordListName());
//		
//		notifier.setStatusMessage("Copying word list: " + l.getWordListName());
//		
//		notifier.setProcessStatus(process);
//		
//		process = process + processStep;
//		
//		if(stopCoping.get())
//			break;
//		
//		}
//		
//		DatabaseHelper.setDbMode(from);
//	}
//
//	
//	
//}
//>>>>>>> .r8
