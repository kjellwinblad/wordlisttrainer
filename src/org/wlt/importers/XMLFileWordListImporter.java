package org.wlt.importers;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wlt.data.WordBinding;
import org.wlt.data.WordList;
import org.wlt.gui.wlselector.WordListSelectorPanel; 



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

	        
	        for(int n = 0; n < nodes.getLength();n++){
	        	Node node = nodes.item(n);
	        	if(node.getNodeName().equals("title")){
	        		newWordList.setWordListName(node.getTextContent());
	        	}else if(node.getNodeName().equals("languageA")){
	        		newWordList.setLanguageA(node.getTextContent());
	        	}else if(node.getNodeName().equals("languageB")){
	        		newWordList.setLanguageA(node.getTextContent());
	        	}else if(node.getNodeName().equals("wordBindings")){
	        		NodeList wordBindingNodes = node.getChildNodes();
	        		for(int u = 0; u < wordBindingNodes.getLength();u++){
	        			Node wordBindingItem = wordBindingNodes.item(u);
	        			if(wordBindingItem.getNodeName().equals("wordBinding")){
	        				WordBinding binding = processWordBinding(wordBindingItem);
	        				newWordList.getWordBindings().add(binding);
	        			}
	        		} 
	        	}
	        }
	        
	        newWordList.createNewInDatabase();
	        newWordList.saveToDatabase();
		
	}


	private WordBinding processWordBinding(Node wordBindingItem) {
		// TODO Auto-generated method stub
		return null;
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
