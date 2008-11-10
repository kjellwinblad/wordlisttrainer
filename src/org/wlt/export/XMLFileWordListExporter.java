package org.wlt.export;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wlt.data.Word;
import org.wlt.data.WordBinding;
import org.wlt.data.WordList;

import exlib.Base64;

public class XMLFileWordListExporter implements WordListExporter {

	private File fileToSaveTo;

	private Component parentComponent;

	private WordList words;
	
	public void export(WordList words, Component frame) {
		parentComponent = frame;
		this.words = words;

		try {

			if (!fetchFile())
				return;

			saveListDataToFile();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(parentComponent,
					"Error when saving file: " + e.getMessage());

			e.printStackTrace();
		}

	}
	
	
	
	private void saveListDataToFile() throws Exception{
	    Document doc;
	    Element wordList;
	    Element title;
	    Element languageA;
	    Element languageB;
	    Element wordBindings;
	    
	    DocumentBuilderFactory dbf = 
	          DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        doc = db.newDocument();

	        wordList = doc.createElement("wordList");
	        doc.appendChild(wordList);
	        
	        title = doc.createElement("title");
	        title.setTextContent(words.getWordListName());
	        wordList.appendChild(title);

	        languageA = doc.createElement("languageA");
	        languageA.setTextContent(words.getLanguageA());
	        wordList.appendChild(languageA);
	        
	        languageB = doc.createElement("languageB");
	        languageB.setTextContent(words.getLanguageB());
	        wordList.appendChild(languageB);
	        
	        wordBindings = doc.createElement("wordBindings");
	        wordList.appendChild(wordBindings);
	        List<WordBinding> wordBindingsList = words.getWordBindings();
	        for(WordBinding b : wordBindingsList){
	        	
	        	Element wordBinding = doc.createElement("wordBinding");
	        	wordBindings.appendChild(wordBinding);
	        	//word 1
	        	Element word1 = doc.createElement("word");
	        	wordBinding.appendChild(word1);
	        	
	        	Element word1Text = doc.createElement("wordText");
	        	word1Text.setTextContent(b.getWordA().getWord());
	        	word1.appendChild(word1Text);

	        	Element word1SoundData = doc.createElement("wordSoundData");
	        	word1SoundData.setTextContent(Base64.encode(b.getWordA().getSoundFile(), 80)); 
	        	word1.appendChild(word1SoundData);
	        	
	        	//word2
	        	Element word2 = doc.createElement("word");
	        	wordBinding.appendChild(word2);
	        	
	        	Element word2Text = doc.createElement("wordText");
	        	word2Text.setTextContent((b.getWordB().getWord()));
	        	word2.appendChild(word2Text);

	        	Element word2SoundData = doc.createElement("wordSoundData");
	        	word2SoundData.setTextContent(Base64.encode(b.getWordB().getSoundFile(), 80)); 
	        	word2.appendChild(word2SoundData);
	        }
	        
	        
	        
	        
	        
	        
	        try {
	            // Create a transformer
	            Transformer xformer = TransformerFactory.newInstance().newTransformer();
	        
	            // Set the public and system id
	            //xformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "publicId");
	            //xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "systemId");

	            //xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-16");
	            xformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "wordSoundData");
	            // Write the DOM document to a file
	            Source source = new DOMSource(doc);
	            
	            Result result = new StreamResult(fileToSaveTo);
	            xformer.transform(source, result);
	            
	        } catch (TransformerConfigurationException e) {
	        } catch (TransformerException e) {
	        }

	        
	        
	        
	}



	private boolean fetchFile() {

		JFileChooser fileChooser = new JFileChooser(
				"Chose a file to export to...");

//		FileFilter fileFilter = new FileFilter() {
//
//			public boolean accept(File f) {
//
//				if (f.isDirectory())
//					return true;
//
//				if (f.getName().endsWith(".html")
//						|| f.getName().endsWith(".htm")
//						|| f.getName().endsWith(".HTM")
//						|| f.getName().endsWith(".HTML")
//						|| f.isDirectory())
//					return true;
//
//				return false;
//
//			}
//
//			@Override
//			public String getDescription() {
//				// TODO Auto-generated method stub
//				return "HTML Files";
//			}
//
//		};

//		fileChooser.addChoosableFileFilter(fileFilter);

		fileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);

		int returnVal = fileChooser.showSaveDialog(parentComponent);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			fileToSaveTo = fileChooser.getSelectedFile();

			if (fileToSaveTo == null || fileToSaveTo.isDirectory()) {
				JOptionPane.showMessageDialog(parentComponent,
						"The file has to be an ordinary file.");
				return false;
			}

			if (!(fileToSaveTo.getName().endsWith(".xml")
					|| fileToSaveTo.getName().endsWith(".XML")))
				fileToSaveTo = new File(fileToSaveTo.getParentFile(),
						fileToSaveTo.getName() + ".xml");

			try {
				fileToSaveTo.createNewFile();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		} else
			return false;
	}

}
