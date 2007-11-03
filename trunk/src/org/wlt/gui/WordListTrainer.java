/**
 * 
 */
package org.wlt.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import org.wlt.data.database.DatabaseHelper;
import org.wlt.gui.dbconf.DatabaseSettingsDialog;
import org.wlt.gui.wlselector.WordListSelectorPanel;
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
		DatabaseHelper.setDatabaseSettings(DatabaseSettings.getInstance());
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

				@Override
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
			
			JMenuItem copyLocal = new JMenuItem("Copy local to network...");
			databaseMenu.add(copyLocal);
			
			JMenuItem copyNetwork = new JMenuItem("Copy network to local...");
			databaseMenu.add(copyNetwork);
		}
		
		return mainMenu;
	}
	
	public void update(){
		wordListSelectorPanel.update();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WordListTrainer wt = new WordListTrainer();
		
		wt.setVisible(true);

	}

}
