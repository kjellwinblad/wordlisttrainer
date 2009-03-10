/**
 * 
 */
package org.wlt.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import org.wlt.data.WordList;
import org.wlt.data.database.DatabaseCopier;
import org.wlt.data.database.DatabaseHelper;
import org.wlt.export.XMLFileWordListExporter;
import org.wlt.gui.dbconf.DatabaseSettingsDialog;
import org.wlt.gui.wlselector.WordListSelectorPanel;
import org.wlt.importers.XMLFileWordListImporter;
import org.wlt.settings.DatabaseSettings;

import com.sun.org.omg.CORBA.Initializer;

/**
 * @author kjellw
 *
 */
public class WordListTrainer extends JFrame {

	private WordListTrainer thisFrame;
	private JMenuBar mainMenu;
	private WordListSelectorPanel wordListSelectorPanel;

	public WordListTrainer() {
		thisFrame = this;
		File propDir = new File(System.getProperty("user.home")
				+ File.separator + DatabaseSettings.PROPERTIES_DIR);
		
		boolean firstTimeProgramRun = !propDir.exists();
		
		DatabaseHelper.setDatabaseSettings(DatabaseSettings.getInstance());
		
		if (firstTimeProgramRun){
						try {
							DatabaseHelper.createLocalDatabaseInDefaultLocation();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		}
		
		
		initialize();

	}
	
	private void initialize() {
		
		setJMenuBar(getMainMenu());
		
		setTitle("Word List Trainer");
		
		setLayout(new BorderLayout());
		 wordListSelectorPanel = new WordListSelectorPanel(this);
		add(wordListSelectorPanel, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showOptionDialog(thisFrame, "Are you sure?", "Exit Word List Trainer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if(result == JOptionPane.YES_OPTION){
					DatabaseHelper.shutdown();
					System.exit(0);	
				}
			}
		});
		
		pack();
		
	}

	private JMenuBar getMainMenu() {
		if(mainMenu == null){
			mainMenu = new JMenuBar();
			
			JMenu databaseMenu = new JMenu("Database");
			
			mainMenu.add(databaseMenu);
			
			JMenuItem remoteDatabaseSettings = new JMenuItem("Database settings...");
			databaseMenu.add(remoteDatabaseSettings);
			remoteDatabaseSettings.addActionListener(new ActionListener(){
 
				public void actionPerformed(ActionEvent e) {
					new DatabaseSettingsDialog(thisFrame).setVisible(true);
					
				}
				
			});

			databaseMenu.add(new JSeparator());
			
//			ButtonGroup databaseConnectionTypeGroup = new ButtonGroup();
//
//			JRadioButtonMenuItem localDatabase = new JRadioButtonMenuItem("Local Database");
//			databaseConnectionTypeGroup.add(localDatabase);
//			localDatabase.setSelected(DatabaseSettings.getInstance().isCurrentDatabaseModeLocal());
//			databaseMenu.add(localDatabase);
//			
//			JRadioButtonMenuItem networkDatabase = new JRadioButtonMenuItem("Network Database");
//			databaseConnectionTypeGroup.add(networkDatabase);
//			networkDatabase.setSelected(!DatabaseSettings.getInstance().isCurrentDatabaseModeLocal());
//			databaseMenu.add(networkDatabase);
//			
//			databaseMenu.add(new JSeparator());
			
			JMenuItem copyLocal = new JMenuItem("Copy Selected to Network/Local DB");
			
			copyLocal.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					
					final String infoText;
					
					
					
					if (DatabaseHelper.getDbMode() == DatabaseHelper.DatabaseMode.Network)
						infoText = "Copying from network database to local database...";
					else
						infoText = "Copying from local database to network database...";

						
					CancelAbleProcess copyProcess = new CancelAbleProcess(){

						private StatusNotifier notifer = null;
						
						public void setStatusNotifier(StatusNotifier n) {

							notifer = n;
						}

						public void run() {
							
							DatabaseCopier copier = null;
							
								
							if (DatabaseHelper.getDbMode() == DatabaseHelper.DatabaseMode.Network){
								copier = new DatabaseCopier(DatabaseHelper.DatabaseMode.Network,
										DatabaseHelper.DatabaseMode.Local, notifer);
								
								notifer.setStatusMessage(infoText);
							
							}else{
							
								copier = new DatabaseCopier(DatabaseHelper.DatabaseMode.Local,
										DatabaseHelper.DatabaseMode.Network, notifer);
															
								notifer.setStatusMessage(infoText);
							
							}
							
							List<WordList> selectedWordLists = wordListSelectorPanel.getSelected();
							
							try {
								copier.startCoping(selectedWordLists);
								
								wordListSelectorPanel.update();
								thisFrame.repaint();
								
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(thisFrame, "Error when trying to copy: " + e1.getMessage());
								e1.printStackTrace();
							}
							
						}
						
					};

					
					CancelAbleProcessDialog dialog = new CancelAbleProcessDialog(thisFrame,
							copyProcess, infoText);
					
					dialog.setVisible(true);
					
				}
				
			});
			
			databaseMenu.add(copyLocal);
			
			JMenu exportInportMenu = new JMenu("Export & Import");
			

			
			JMenuItem exportMenuItem = new JMenuItem("Export selected list to XML file...");
			exportMenuItem.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					List<WordList> selectedWordLists= wordListSelectorPanel.getSelected();
					if(selectedWordLists.size()>0){
						WordList  selectedWordList = selectedWordLists.get(0);
						XMLFileWordListExporter xmlFileExporter = new XMLFileWordListExporter();
						xmlFileExporter.export(selectedWordList, thisFrame);
					}else
						JOptionPane.showMessageDialog(thisFrame, "No word list is selected.");
				}
				
			});
			exportInportMenu.add(exportMenuItem);
			JMenuItem inportMenuItem = new JMenuItem("Import word list from XML file...");
			inportMenuItem.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					XMLFileWordListImporter importer =  new XMLFileWordListImporter();
					importer.importt(wordListSelectorPanel);
					
				}
				
			});
			exportInportMenu.add(inportMenuItem);
			mainMenu.add(exportInportMenu);
			mainMenu.add(Box.createHorizontalGlue());
			JMenu helpMenu = new JMenu("Help");
			JMenuItem helpMenuItem = new JMenuItem("Help...");
			helpMenuItem.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(thisFrame, "No help is currently available here. Please see\n" +
															 "http://wordlisttrainer.googlecode.com for help\n" +
															 "and to get the latest version.");
					
				}
				
			});
			
			helpMenu.add(helpMenuItem);
			
			JMenuItem aboutMenuItem = new JMenuItem("About...");
			aboutMenuItem.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(thisFrame, "<html><h1>World List Trainer</h1> \n" +
															"<html><i>Developer:</i> Kjell Winblad (kjellwinblad@gmail.com)\n" +
															"<html><i>License:</i> GNU General Public License v3\n" +
															"\n" +
															"Full source, downloads and other information are available at the web site:\n" +
															"http://wordlisttrainer.googlecode.com\n"
															 +"The project is currently looking for more developers.");
					
				}
				
			});
			
			helpMenu.add(aboutMenuItem);
			
			mainMenu.add(helpMenu);
		}
		
		return mainMenu;
	}
	
	public void update(){
		//System.out.println("Update call");
		//new Exception().printStackTrace();
		wordListSelectorPanel.update();
		//System.out.println("Update call finished");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WordListTrainer wt = new WordListTrainer();
		
		wt.setVisible(true);

	}

}
