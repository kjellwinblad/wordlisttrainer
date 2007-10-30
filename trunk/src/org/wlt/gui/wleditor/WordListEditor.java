package org.wlt.gui.wleditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import org.wlt.data.WordList;

import com.jeta.forms.components.panel.FormPanel;
import com.sun.org.omg.CORBA.Initializer;
/**
 * 
 * @author kjellw
 *
 */
public class WordListEditor extends JDialog {

	private WordList wordList;

	private JSplitPane propertiesAndListSplitPane;
	private FormPanel propertiesPanel;
	private WordListEditorTablePanel wordListEitorTablePanel;

	private JTextField wordListNameTextField;

	private JTextField languageBTextField;

	private JTextField languageATextField;
	
	public WordListEditor(Frame owner, WordList wordList) {
		super(owner);
		owner.setEnabled(false);
		this.wordList = wordList;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		
		setTitle("Word List Editor");
		
		add(getPropertiesAndListSplitPane(), BorderLayout.CENTER);
		
		pack();
	}

	private JSplitPane getPropertiesAndListSplitPane() {
		 if(propertiesAndListSplitPane == null){
			 propertiesAndListSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			 
			 //Left
				JPanel editorPanelContainer = new JPanel();
				editorPanelContainer.setLayout(new BorderLayout());
				editorPanelContainer.add(getPropertiesPanel(), BorderLayout.CENTER);
				propertiesAndListSplitPane.setLeftComponent(editorPanelContainer);
				
				
				wordListEitorTablePanel = new WordListEditorTablePanel(wordList);
				
				propertiesAndListSplitPane.setRightComponent(wordListEitorTablePanel);
		 }
		 return propertiesAndListSplitPane;
	}

	private JPanel getPropertiesPanel() {
		if(propertiesPanel == null){
			propertiesPanel = new FormPanel( "org/wlt/gui/word_list_properties_form.jfrm" );
			
			
			wordListNameTextField = propertiesPanel.getTextField ("wordListName" );     
		    wordListNameTextField.setText(wordList.getWordListName());
		    wordListNameTextField.addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					wordList.setWordListName(wordListNameTextField.getText());
				}
		    });
		    
		    
		    languageATextField = propertiesPanel.getTextField("languageA");
		    languageATextField.setText(wordList.getLanguageA());
		    languageATextField.addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					wordList.setLanguageA(languageATextField.getText());
				}
		    });
		    
		    languageBTextField = propertiesPanel.getTextField("languageB");
		    languageBTextField.setText(wordList.getLanguageB());
		    languageBTextField.addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					wordList.setLanguageB(languageBTextField.getText());
				}
		    });
		}
		return propertiesPanel;
	}

	public WordList getWordList() {
		return wordList;
	}
	
}
