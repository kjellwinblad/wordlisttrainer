/**
 * 
 */
package org.wlt.gui.wleditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.json.JSONArray;
import org.json.JSONObject;
import org.wlt.data.Word;
import org.wlt.data.WordBinding;
import org.wlt.data.WordList;
import org.wlt.gui.export.ExportDialog;
import org.wlt.gui.sound.WordSoundEditor;

/**
 * @author kjellw
 * 
 */
public class WordListEditorTablePanel extends JPanel {

	private WordList wordList;
	WordListEditorTable wordListEditorTable;
	private JPanel controlPanel;
	private Dialog parentFrame;

	public WordListEditorTablePanel(WordList wordList, Dialog parentFrame) {
		this.wordList = wordList;
		this.parentFrame = parentFrame;

		initialize();
	}
	
	private static HashMap<String, String> langLangCodeMap = new HashMap<String, String>();
	
	static{
		langLangCodeMap.put("AFRIKAANS", "af");
		langLangCodeMap.put("ALBANIAN", "sq");
		langLangCodeMap.put("AMHARIC", "am");
		langLangCodeMap.put("ARABIC", "ar");
		langLangCodeMap.put("ARMENIAN", "hy");
		langLangCodeMap.put("AZERBAIJANI", "az");
		langLangCodeMap.put("BASQUE", "eu");
		langLangCodeMap.put("BELARUSIAN", "be");
		langLangCodeMap.put("BENGALI", "bn");
		langLangCodeMap.put("BIHARI", "bh");
		langLangCodeMap.put("BULGARIAN", "bg");
		langLangCodeMap.put("BURMESE", "my");
		langLangCodeMap.put("CATALAN", "ca");
		langLangCodeMap.put("CHEROKEE", "chr");
		langLangCodeMap.put("CHINESE", "zh");
		langLangCodeMap.put("CHINESE SIMPLIFIED", "zh-CN");
		langLangCodeMap.put("CHINESE TRADITIONAL", "zh-TW");
		langLangCodeMap.put("CROATIAN", "hr");
		langLangCodeMap.put("CZECH", "cs");
		langLangCodeMap.put("DANISH", "da");
		langLangCodeMap.put("DHIVEHI", "dv");
		langLangCodeMap.put("DUTCH", "nl");
		langLangCodeMap.put("ENGLISH", "en");
		langLangCodeMap.put("ESPERANTO", "eo");
		langLangCodeMap.put("ESTONIAN", "et");
		langLangCodeMap.put("FILIPINO", "tl");
		langLangCodeMap.put("FINNISH", "fi");
		langLangCodeMap.put("FRENCH", "fr");
		langLangCodeMap.put("GALICIAN", "gl");
		langLangCodeMap.put("GEORGIAN", "ka");
		langLangCodeMap.put("GERMAN", "de");
		langLangCodeMap.put("GREEK", "el");
		langLangCodeMap.put("GUARANI", "gn");
		langLangCodeMap.put("GUJARATI", "gu");
		langLangCodeMap.put("HEBREW", "iw");
		langLangCodeMap.put("HINDI", "hi");
		langLangCodeMap.put("HUNGARIAN", "hu");	
		langLangCodeMap.put("ICELANDIC", "is");	
		langLangCodeMap.put("INDONESIAN", "id");	
		langLangCodeMap.put("INUKTITUT", "iu");	
		langLangCodeMap.put("ITALIAN", "it");	
		langLangCodeMap.put("JAPANESE", "ja");	
		langLangCodeMap.put("KANNADA", "kn");	
		langLangCodeMap.put("KAZAKH", "kk");	
		langLangCodeMap.put("KHMER", "km");	
		langLangCodeMap.put("KOREAN", "ko");	
		langLangCodeMap.put("KURDISH", "ku");	
		langLangCodeMap.put("KYRGYZ", "ky");	
		langLangCodeMap.put("LAOTHIAN", "lo");	
		langLangCodeMap.put("LATVIAN", "lv");	
		langLangCodeMap.put("LITHUANIAN", "lt");
		langLangCodeMap.put("MACEDONIAN", "mk");
		langLangCodeMap.put("MALAY", "ms");
		langLangCodeMap.put("MALAYALAM", "ml");
		langLangCodeMap.put("MALTESE", "mt");
		langLangCodeMap.put("MARATHI", "mr");
		langLangCodeMap.put("MONGOLIAN", "mn");
		langLangCodeMap.put("NEPALI", "ne");
		langLangCodeMap.put("NORWEGIAN", "no");
		langLangCodeMap.put("ORIYA", "or");
		langLangCodeMap.put("PASHTO", "ps");
		langLangCodeMap.put("PERSIAN", "fa");
		langLangCodeMap.put("POLISH", "pl");
		langLangCodeMap.put("PORTUGUESE", "pt-PT");
		langLangCodeMap.put("PUNJABI", "pa");
		langLangCodeMap.put("ROMANIAN", "ro");
		langLangCodeMap.put("RUSSIAN", "ru");
		langLangCodeMap.put("SANSKRIT", "sa");
		langLangCodeMap.put("SERBIAN", "sr");
		langLangCodeMap.put("SINDHI", "sd");
		langLangCodeMap.put("SINHALESE", "si");
		langLangCodeMap.put("SLOVAK", "sk");
		langLangCodeMap.put("SLOVENIAN", "sl");
		langLangCodeMap.put("SPANISH", "es");
		langLangCodeMap.put("SWAHILI", "sw");
		langLangCodeMap.put("SWEDISH", "sv");
		langLangCodeMap.put("TAJIK", "tg");
		langLangCodeMap.put("TAMIL", "ta");
		langLangCodeMap.put("TAGALOG", "tl");
		langLangCodeMap.put("TELUGU", "te");
		langLangCodeMap.put("THAI", "th");
		langLangCodeMap.put("TIBETAN", "bo");
		langLangCodeMap.put("TURKISH", "tr");
		langLangCodeMap.put("UKRAINIAN", "uk");
		langLangCodeMap.put("URDU", "ur");
		langLangCodeMap.put("UZBEK", "uz");
		langLangCodeMap.put("UIGHUR", "ug");
		langLangCodeMap.put("VIETNAMESE", "vi");
		langLangCodeMap.put("UNKNOWN", "");

	}
	

	private void initialize() {
		setLayout(new BorderLayout());
		JPanel wordListEditorTablePanel = new JPanel();
		wordListEditorTablePanel.setLayout(new BorderLayout());
		wordListEditorTable = new WordListEditorTable(wordList);
		JScrollPane wordListEditorTableScrollPane = new JScrollPane(
				wordListEditorTable);

		wordListEditorTable.setFillsViewportHeight(true);
		wordListEditorTablePanel.add(wordListEditorTableScrollPane,
				BorderLayout.CENTER);

		add(wordListEditorTablePanel, BorderLayout.CENTER);

		add(getControlPanel(), BorderLayout.SOUTH);
		

		
		

	}

	private JPanel getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new JPanel();
			controlPanel.setLayout(new BorderLayout());
JToolBar toolBar = new JToolBar();
			JButton addButton = new JButton();

			addButton.setToolTipText("Add Word Binding");
			
			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addWordBinding();
				}
			});

		    addButton.setIcon(new ImageIcon(getClass().getResource("/images/edit_add.png"), "add"));

			toolBar.add(addButton);
			
			JButton upButton = new JButton();

			upButton.setToolTipText("Move selected row(s) up in the list");
			
			upButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moveSelectedUp();
				}

			});

			upButton.setIcon(new ImageIcon(getClass().getResource("/images/1uparrow.png"), "up"));

			toolBar.add(upButton);
			
			JButton downButton = new JButton();

			downButton.setToolTipText("Move selected row(s) down in the list");
			
			downButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moveSelectedDown();
				}
			});

			downButton.setIcon(new ImageIcon(getClass().getResource("/images/1downarrow.png"), "add"));

			toolBar.add(downButton);
			
			//ADD copy button
			
			JButton copyButton = new JButton();

			copyButton.setToolTipText("Copy selected row(s) to other word list");
			
			copyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					copySelectedRows();
				}
			});

			copyButton.setIcon(new ImageIcon(getClass().getResource("/images/editcopy.png"), "add"));

			toolBar.add(copyButton);
			
			//END ADD copy button
			

			JButton openInRecorEditorButton = new JButton(
					);
			
			openInRecorEditorButton.setToolTipText("Open Selected In Sound Editor");
			openInRecorEditorButton.setIcon(new ImageIcon(getClass().getResource("/images/gnome-sound-recorder32.png"), "record sounds"));

			openInRecorEditorButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openSelectedInSoundEditor();
				}
			});

			toolBar.add(openInRecorEditorButton);

			JButton exportWordListButton = new JButton(
					);
			
			exportWordListButton.setToolTipText("Export Selected");
			
			exportWordListButton.setIcon(new ImageIcon(getClass().getResource("/images/revert.png"), "export selected"));

			exportWordListButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exportWordList();
				}
			});

			toolBar.add(exportWordListButton);

			JButton translateButton = new JButton();

			
			translateButton.setToolTipText("(Alt-T) Translate all missing words with the help of Google Translate API");
			
			translateButton.setMnemonic('T');
			
			
			translateButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					translate();
				}

			});

			translateButton.setIcon(new ImageIcon(getClass().getResource("/images/toggle_log.png"), "translate"));


					
			
			toolBar.add(translateButton);
			
			JButton deleteButton = new JButton();
			
			deleteButton.setToolTipText("Delete Selected Item(s)");

			deleteButton.setIcon(new ImageIcon(getClass().getResource("/images/editdelete.png"), "delete selected"));
			
			final Component thisComp = this;
			
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					if(JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(thisComp, "Are you sure that you want to delete the selected row(s)?"))
						deleteSelectedWordBindings();
				}
			});

			toolBar.add(deleteButton);
			
			controlPanel.add(BorderLayout.CENTER, toolBar);
		
			JLabel googleJLabel = new JLabel("<html><i>Powered by </i></html>");
			
			JLabel googleLogo = new JLabel();

			googleLogo.setIcon(new ImageIcon(getClass().getResource("/images/w.png"), "Google logo"));
			googleJLabel.add(googleLogo);
			//googleJLabel.setIcon(new ImageIcon(getClass().getResource("/images/w.png"), "Google logo"));
			
			JPanel googlePanel = new JPanel();
			
			googlePanel.setLayout(new BoxLayout(googlePanel, BoxLayout.X_AXIS));
			googlePanel.add(googleJLabel);
			googlePanel.add(googleLogo);
			
			googlePanel.setToolTipText("The translate functionality is powered by Google Translate API http://code.google.com/apis/ajaxlanguage/");

			controlPanel.add(BorderLayout.EAST, googlePanel);
		}

		return controlPanel;
	}

	private void moveSelectedDown() {
		
		List<WordBinding> bindings = null;
	
		ArrayList<Integer> newSelectionList = new ArrayList<Integer>();
		
		try {
			bindings = wordList.getWordBindings();
			int rows[] = wordListEditorTable.getSelectedRows();
			int size = bindings.size();
			
			ArrayList<Integer> selectedRowsList = new ArrayList<Integer>();
			
			for(int n : rows)
				selectedRowsList.add(n);
		
			Collections.sort(selectedRowsList);
			Collections.reverse(selectedRowsList);
			
			
			for(int row : selectedRowsList){
				if(row < size-1){
				
					WordBinding b = bindings.get(row);
					bindings.remove(row);
					bindings.add(row + 1, b);
					
					newSelectionList.add(row + 1);
					
				}				
			}
				wordListEditorTable.clearSelection();
			

			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
		wordListEditorTable.updateData();
		
		//wordListEditorTable.clearSelection();
		
		for(int n : newSelectionList){

			wordListEditorTable.changeSelection(n, 0, true, false);
			wordListEditorTable.changeSelection(n, 1, true, true);
		}

		//wordListEditorTable.updateUI();

	}

	private void moveSelectedUp() {
		List<WordBinding> bindings = null;
		
		ArrayList<Integer> newSelectionList = new ArrayList<Integer>();
		
		try {
			bindings = wordList.getWordBindings();
			int rows[] = wordListEditorTable.getSelectedRows();
			int size = bindings.size();
			
			ArrayList<Integer> selectedRowsList = new ArrayList<Integer>();
			
			for(int n : rows)
				selectedRowsList.add(n);
		
			Collections.sort(selectedRowsList);
			//Collections.reverse(selectedRowsList);
			
			
			for(int row : selectedRowsList){
				if(row > 0){
				
					WordBinding b = bindings.get(row);
					bindings.remove(row);
					bindings.add(row - 1, b);
					
					newSelectionList.add(row - 1);
					
				}				
			}
				wordListEditorTable.clearSelection();
			

			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
		wordListEditorTable.updateData();
		
		//wordListEditorTable.clearSelection();
		
		for(int n : newSelectionList){

			wordListEditorTable.changeSelection(n, 0, true, false);
			wordListEditorTable.changeSelection(n, 1, true, true);
		}
		
	}

	private void translate() {
		int rowCount =wordListEditorTable.getRowCount();
		
		if(wordListEditorTable.getCellEditor()!=null)
			wordListEditorTable.getCellEditor().stopCellEditing();
		
		
		
		for(int n = 0; n < rowCount; n++){
			
			
			
			WordBinding wb = wordListEditorTable.getElementAtRow(n);
			boolean doTranslate = false;
			
			String fromLangCode = null;
			String toLangCode = null;
			String targetLanguage = null;
			String wordToTranslate = null;
			if(wb.getWordA().getWord().equals("") && !wb.getWordB().getWord().equals("")){
				
				fromLangCode = langLangCodeMap.get(wb.getWordB().getLanguage().toUpperCase());
				if(fromLangCode == null){
					
					showCantFindLanguageDialog(wb.getWordB().getLanguage());
					
					return;
				}
				
	
					toLangCode = langLangCodeMap.get(wb.getWordA().getLanguage().toUpperCase());

					if(toLangCode == null){
						
						showCantFindLanguageDialog(wb.getWordA().getLanguage());
						
						return;
					}
				
					wordToTranslate = wb.getWordB().getWord();
				targetLanguage = "Language A";
				doTranslate = true;
				
			}
			
			if(wb.getWordB().getWord().equals("") && !wb.getWordA().getWord().equals("")){
				
				fromLangCode = langLangCodeMap.get(wb.getWordA().getLanguage().toUpperCase());
				if(fromLangCode == null){
					
					showCantFindLanguageDialog(wb.getWordA().getLanguage());
					
					return;
				}
				
	
					toLangCode = langLangCodeMap.get(wb.getWordB().getLanguage().toUpperCase());

					if(toLangCode == null){
						
						showCantFindLanguageDialog(wb.getWordB().getLanguage());
						
						return;
					}
					wordToTranslate = wb.getWordA().getWord();
				targetLanguage = "Language B";
				doTranslate = true;
				
			}
			
			if(doTranslate){
				String result = makeQuery(fromLangCode, toLangCode, wordToTranslate);
				
				wordListEditorTable.setValueAt(result, n, wordListEditorTable.getColumnName(0).equals(targetLanguage) ? 0 : 1);
				
				
			}
			
			
		}
			
		wordListEditorTable.requestFocus();
	}

	private void showCantFindLanguageDialog(String language) {
		JOptionPane.showMessageDialog(this, "Could not much " + language + " with any language supported by Google Translate API\n" + 
				"Please see the list of supported languages in the language selection component\n" +
				"and contact kjellwinblad@gmail.com if you belive that you use a language that is\n" +
				"supported by Google Translate API but that is not listed in the language selection\n" +
				"component. (Note that you need to use the English name of the language)");	
	}

	protected void openSelectedInSoundEditor() {

		if(getSelectedWords().size()==0)
			JOptionPane.showMessageDialog(this, "No words are selected. Some words have to be selectet to open in sound editor.");
		else{
		
		List<Word> words = getSelectedWords();

		new WordSoundEditor(words).setVisible(true);
		}

		
		
	}

	private List<Word> getSelectedWords() {
		
		int cols[] = wordListEditorTable.getSelectedColumns();
		int rows[] = wordListEditorTable.getSelectedRows();

		if(cols.length==2 && !wordListEditorTable.isLangAFirst()){
			cols[0]= 1;
			cols[1]= 0;
		}
		
		List<Word> words = new LinkedList<Word>();

		for (int row : rows)
			for (int col : cols)
				words.add(wordListEditorTable.getWordAt(col, row));
		
		return words;
	}

	private void addWordBinding() {
		wordListEditorTable.addRow();

	}
	
	
	private void copySelectedRows() {
		
		int rows[] = wordListEditorTable.getSelectedRows();
		
		if(wordListEditorTable.getSelectedRows().length==0)
			return;
		
		new CopyDialog(parentFrame, wordList, rows).setVisible(true);
		
	}

	private void deleteSelectedWordBindings() {
		int rows[] = wordListEditorTable.getSelectedRows();
		List<WordBinding> deleteRows = new LinkedList<WordBinding>();
		WordBinding wb;
		for (int row : rows) {
			try {

				wb = wordListEditorTable.getWordBindings().get(row);
				deleteRows.add(wb);
				wb.removeFromDatabase();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(),
						"Could not delete word list(s)",
						JOptionPane.ERROR_MESSAGE);

				e.printStackTrace();
			}

		}
		for (WordBinding wobi : deleteRows)
			wordListEditorTable.getWordBindings().remove(wobi);

		wordListEditorTable.updateData();
		wordListEditorTable.repaint();

	}
	
	private void exportWordList() {
		if(getSelectedWords().size()==0)
			JOptionPane.showMessageDialog(this, "No words are selected. Some words have to be selectet for exporting.");
		else
		    new ExportDialog(getSelectedWords(), this).setVisible(true);
		
	



		}
	
	

	

// Put your website here



private String makeQuery(String languageA, String languageB, String wordToTranslate) {

	
	String HTTP_REFERER = "http://wordlisttrainer.googlecode.com";
 //System.out.println("\nQuerying for " + query);

	String translateString = "";
	
 try
 {
  // Convert spaces to +, etc. to make a valid URL

  
  String lang1 = URLEncoder.encode(languageA, "UTF-8");

  String lang2 = URLEncoder.encode(languageB, "UTF-8");

String word = URLEncoder.encode(wordToTranslate, "UTF-8");

URL url = new URL("http://ajax.googleapis.com/ajax/services/language/translate?v=1.0&q=" + word + "&langpair=" + lang1 + "%7C" + lang2);
 
URLConnection connection = url.openConnection();

connection.addRequestProperty("Referer", HTTP_REFERER);

  // Get the JSON response
  String line;
  StringBuilder builder = new StringBuilder();
  BufferedReader reader = new BufferedReader(
    new InputStreamReader(connection.getInputStream()));
  while((line = reader.readLine()) != null) {
   builder.append(line);
  }

  String response = builder.toString();
  JSONObject json = new JSONObject(response);

  

  translateString =
    json.getJSONObject("responseData").getString("translatedText");

 }
 catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Could not translate the word "+ wordToTranslate + "with the help of Google Translate API" +
		                              "Possible reasons are that you are thet you are not connected to the internet and that the service is down.\n" +
		                              "Error Message: " + e.getMessage());
  e.printStackTrace();
 }
 if(translateString==null)
	 translateString = "";
 
 return translateString;
 
}



}
