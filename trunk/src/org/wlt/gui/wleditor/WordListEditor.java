package org.wlt.gui.wleditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ListDataListener;

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

	private JComboBox languageBTextField;

	private JComboBox languageATextField;
	
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
				
				
				wordListEitorTablePanel = new WordListEditorTablePanel(wordList, this);
				
				propertiesAndListSplitPane.setRightComponent(wordListEitorTablePanel);
		 }
		 return propertiesAndListSplitPane;
	}

	private JPanel getPropertiesPanel() {
		if(propertiesPanel == null){
			propertiesPanel = new FormPanel( "org/wlt/gui/word_list_properties_form.jfrm" );
			
			
			
			
			String[] langs = {"Albanian",
			"Arabic",
			"Bulgarian",
			"Catalan",
			"Chinese",
			"Chinese Simplified",
			"Chinese Traditional",
			"Croatian",
			"Czech",
			"Danish",
			"Dutch",
			"English",
			"Estonian",
			"Filipino",
			"Finnish",
			"French",
			"Galician",
			"German",
			"Greek",
			"Hebrew",
			"Hindi",
			"Hungarian",
			"Indonesian",
			"Italian",
			"Japanese",
			"Korean",
			"Latvian",
			"Lithuanian",
			"Maltese",
			"Norwegian",
			"Polish",
			"Portuguese",
			"Romanian",
			"Russian",
			"Serbian",
			"Slovak",
			"Slovenian",
			"Spanish",
			"Swedish",
			"Thai",
			"Turkish",
			"Ukrainian",
			"Vietnamese"};
			
			
			wordListNameTextField = propertiesPanel.getTextField ("wordListName" );     
		    wordListNameTextField.setText(wordList.getWordListName());
		    wordListNameTextField.addKeyListener(new KeyAdapter(){
				
		    	@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
		    		wordList.setWordListName(wordListNameTextField.getText());
				}

		    });
		    
		    
		    languageATextField = propertiesPanel.getComboBox("languageA");
		    
		    languageATextField.setModel(new DefaultComboBoxModel(langs));

		    
		    languageATextField.getEditor().setItem(wordList.getLanguageA());
		    
		    ((JTextField)languageATextField.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter(){
		    	@Override
				public void keyReleased(KeyEvent e) {
					wordList.setLanguageA(languageATextField.getEditor().getItem().toString());
				}
		    });
		    
		    languageATextField.addItemListener(new ItemListener(){

				public void itemStateChanged(ItemEvent arg0) {
					
					wordList.setLanguageA(languageATextField.getEditor().getItem().toString());
				}
		    	
		    });
		    
		    languageBTextField = propertiesPanel.getComboBox("languageB");
		    
		    languageBTextField.setModel(new DefaultComboBoxModel(langs));
		    
		    languageBTextField.getEditor().setItem(wordList.getLanguageB());
		    
		    ((JTextField)languageBTextField.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter(){
		    	@Override
				public void keyReleased(KeyEvent e) {
					wordList.setLanguageB(languageBTextField.getEditor().getItem().toString());
				}
		    });
		    
		    languageBTextField.addItemListener(new ItemListener(){

				public void itemStateChanged(ItemEvent arg0) {
					wordList.setLanguageB(languageBTextField.getEditor().getItem().toString());
					
				}
		    	
		    });
		}
		return propertiesPanel;
	}

	public WordList getWordList() {
		return wordList;
	}
	
}
