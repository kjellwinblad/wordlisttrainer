package org.wlt.export;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat.Type;
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
import org.wlt.api.sound.Player;
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

	        	if(b.getWordA().getSoundFile()!= null){
	        	Element word1SoundData = doc.createElement("wordSoundData");

	        	InputStream inputStream = new ByteArrayInputStream(b.getWordA().getSoundFile());
	        	

				AudioFormat format = Player.AUDIO_FORMAT;

				long lLengthInFrames = b.getWordA().getSoundFile().length / format.getFrameSize();
				javax.sound.sampled.AudioInputStream ais = new javax.sound.sampled.AudioInputStream(inputStream,
															format,
															lLengthInFrames);

				
				
				int	nWrittenBytes = 0;
				try
				{
					Type targetFileType= AudioFileFormat.Type.WAVE;
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					nWrittenBytes = AudioSystem.write(ais, targetFileType, outputStream);
					word1SoundData.setTextContent(Base64.encode(outputStream.toByteArray(), 80)); 
		        	word1.appendChild(word1SoundData);
					
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

			

	        	
	        	
	        	}
	        	
	        	//word2
	        	Element word2 = doc.createElement("word");
	        	wordBinding.appendChild(word2);
	        	
	        	Element word2Text = doc.createElement("wordText");
	        	word2Text.setTextContent((b.getWordB().getWord()));
	        	word2.appendChild(word2Text);

	        	if(b.getWordB().getSoundFile()!= null){
		        	Element word2SoundData = doc.createElement("wordSoundData");

		        	InputStream inputStream = new ByteArrayInputStream(b.getWordB().getSoundFile());
		        	

					AudioFormat format = Player.AUDIO_FORMAT;

					long lLengthInFrames = b.getWordB().getSoundFile().length / format.getFrameSize();
					javax.sound.sampled.AudioInputStream ais = new javax.sound.sampled.AudioInputStream(inputStream,
																format,
																lLengthInFrames);

					
					
					int	nWrittenBytes = 0;
					try
					{
						Type targetFileType= AudioFileFormat.Type.WAVE;
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						nWrittenBytes = AudioSystem.write(ais, targetFileType, outputStream);
						word2SoundData.setTextContent(Base64.encode(outputStream.toByteArray(), 80)); 
			        	word2.appendChild(word2SoundData);
						
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
	        	}
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
	        	e.printStackTrace();
	        } catch (TransformerException e) {
	        	e.printStackTrace();
	        }

	        JOptionPane.showMessageDialog(parentComponent,words.getWordListName() + " has succesfully been exported to an XML-file.");
	        
	        
	}



	private boolean fetchFile() {

		JFileChooser fileChooser = new JFileChooser(
				"Chose a file to export to...");

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
