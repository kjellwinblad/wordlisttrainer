/**
 * 
 */
package org.wlt.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.wlt.data.database.DatabaseHelper;
import org.wlt.gui.wlselector.WordListSelectorPanel;

import com.sun.org.omg.CORBA.Initializer;

/**
 * @author kjellw
 *
 */
public class WordListTrainer extends JFrame {

	private WordListTrainer thisFrame;

	public WordListTrainer() {
		thisFrame = this;
		initialize();
	}
	
	private void initialize() {
		
		setTitle("Word List Trainer");
		
		setLayout(new BorderLayout());
		
		add(new WordListSelectorPanel(this), BorderLayout.CENTER);
		
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WordListTrainer wt = new WordListTrainer();
		
		wt.setVisible(true);

	}

}
