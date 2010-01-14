package org.wlt.importers;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wlt.data.Word;
import org.wlt.data.WordBinding;
import org.wlt.data.WordList;
import org.wlt.gui.wlselector.WordListSelectorPanel; 

import exlib.Base64;



public class XMLFileWordListImporter {

	private Component parentComponent;
	
	File fileToOpen;
	
	public void importt(WordListSelectorPanel wordListSelectorPanel){
		
		parentComponent = wordListSelectorPanel;
		
		if(!fetchFile())
			return;
		
		try {
			try {
				doImport();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(parentComponent, "Could not parse file " + e.getMessage());
				e.printStackTrace();
			}
		} catch (HeadlessException e) {
			JOptionPane.showMessageDialog(parentComponent, "Could not parse file " + e.getMessage());
			e.printStackTrace();
		}
		
		wordListSelectorPanel.update();
	}
	
	
	private void doImport() throws Exception{
		WordList newWordList = new WordList();
		
		DocumentBuilderFactory dbf = 
	          DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.parse(fileToOpen);
	        NodeList wordLists = doc.getElementsByTagName("wordList");
	        if(wordLists.getLength() < 1)
	        	throw new Exception("Could not find wordList element in XML file");
	        
	        Node wordList = wordLists.item(0);
	        
	        NodeList nodes = wordList.getChildNodes();

	        String languageA = null;
	        String languageB = null;
	        
	        
	        for(int n = 0; n < nodes.getLength();n++){
	        	Node node = nodes.item(n);
	        	if(node.getNodeName().equals("title")){
	        		newWordList.setWordListName(node.getTextContent());
	        	}else if(node.getNodeName().equals("languageA")){
	        		newWordList.setLanguageA(node.getTextContent());
	        		languageA = node.getTextContent();
	        	}else if(node.getNodeName().equals("languageB")){
	        		newWordList.setLanguageB(node.getTextContent());
	        		languageB = node.getTextContent();
	        	}else if(node.getNodeName().equals("wordBindings")){
	        		NodeList wordBindingNodes = node.getChildNodes();
	        		for(int u = 0; u < wordBindingNodes.getLength();u++){
	        			Node wordBindingItem = wordBindingNodes.item(u);
	        			if(wordBindingItem.getNodeName().equals("wordBinding")){
	        				WordBinding binding = processWordBinding(wordBindingItem, newWordList, languageA, languageB);
	        				newWordList.getWordBindings().add(binding);
	        			}
	        		} 
	        	}
	        }
	        
	        newWordList.createNewInDatabase();
	        newWordList.saveToDatabase();
		
	}


	private WordBinding processWordBinding(Node wordBindingItem, WordList newWordList, String languageA, String languageB) {
		
		NodeList nodes = wordBindingItem.getChildNodes();
		
		WordBinding binding = new WordBinding(newWordList);
		boolean firstWord = true;
		
		for(int n = 0; n < nodes.getLength();n++){
			Node node = nodes.item(n);
			
			if(node.getNodeName().equals("word") && firstWord){

				Word wordA = processWord(node);
				
				wordA.setLanguage(languageA);
				
				binding.setWordA(wordA);
				
				firstWord = false;
				
        	}else if(node.getNodeName().equals("word") && !firstWord){
				
				Word wordB = processWord(node);
				
				wordB.setLanguage(languageB);
				
				binding.setWordB(wordB);
        		
        	}
		}
		
		return binding;
	}


	private Word processWord(Node wordNode) {
		
		NodeList nodes = wordNode.getChildNodes();
		
		Word word = new Word();

		
		for(int n = 0; n < nodes.getLength();n++){
			Node node = nodes.item(n);
			
			if(node.getNodeName().equals("wordText")){
				
				word.setWord(node.getTextContent());
				
        	}else if(node.getNodeName().equals("wordSoundData")){
				
				try {
					word.setSoundFile(Base64.decode(node.getTextContent()));
				} catch (DOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        	}
		}
		
		
		
		return word;
	}


	private boolean fetchFile() {

		JFileChooser fileChooser = new JFileChooser(
				"Chose a file to import from...");


		fileChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);

		int returnVal = fileChooser.showSaveDialog(parentComponent);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			fileToOpen = fileChooser.getSelectedFile();

			if (fileToOpen == null || fileToOpen.isDirectory()) {
				JOptionPane.showMessageDialog(parentComponent,
						"The file has to be an ordinary file.");
				return false;
			}


			if(!fileToOpen.canRead())
				return false;
			else
				return true;

			
		} else
			return false;
	}
	
}
