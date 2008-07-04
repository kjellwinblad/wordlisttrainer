package org.wlt.export;

import java.awt.Component;
import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.wlt.api.sound.Player;
import org.wlt.data.Word;

public class WAVFileExporter implements Exporter{

	private File fileToSaveTo;
	
	private Component parentComponent;
	
	private List<Word> words;
	
	public void export(List<Word> words, Component parentComponent) {
		
		this.parentComponent = parentComponent;
		
		this.words = words;
		
		//Check if a word miss sound...
		
		if(!checkForMissingSound())
			return;
		
		//Find a file to export to
		
		if(!fetchFile())
			return;
		
		saveSoundDataToFile();
		

	}

	private void saveSoundDataToFile() {
		
		AudioFileFormat.Type targetFileType = AudioFileFormat.Type.WAVE;
		
//		AudioFormat targetAudioFormat = new AudioFormat(Player.AUDIO_FORMAT.getEncoding(),
//				Player.AUDIO_FORMAT.getSampleRate(),
//				AudioSystem.NOT_SPECIFIED,
//				Player.AUDIO_FORMAT.getChannels(),
//				AudioSystem.NOT_SPECIFIED,
//				AudioSystem.NOT_SPECIFIED,
//				Player.AUDIO_FORMAT.isBigEndian());
//		
//		InputStream wordSoundInputStream = new InputStream(){
//
//			private int currentWord = 0;
//			
//			private int currentByte = -1;
//			
//			private byte currentByteArray[] = words.get(0).getSoundFile();
//			
//			@Override
//			public int read() throws IOException {
//				currentByte++;
//				
//				if(currentByte < currentByteArray.length)
//					return currentByteArray[currentByte];
//				else{
//					
//					currentWord++;
//					
//					if(currentWord < words.size()){
//						
//						currentByteArray = words.get(currentWord).getSoundFile(); 
//						
//						currentByte = 0;
//						
//						return currentByteArray[currentByte];
//						
//					}else
//						return -1;
//					
//				}
//			}
//			
//		};
		//Test if data correct
		int len = 0;
		for(Word word : words)
			len = len  + word.getSoundFile().length;
		
		byte[] soundByteArray = new byte[len];
		int cont = 0;
		for(Word w: words){
			
			byte[] toCopy = w.getSoundFile();
			
			for(int n = 0; n < toCopy.length ; n++){
				soundByteArray[cont] = toCopy[n];
				cont++;
			}
			
		}
		
//		try {
//			Player.play(soundByteArray);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		//Calculate audio length
		long audioLength = 0;
		
		for(Word word : words)
			audioLength = audioLength + word.getSoundFile().length / Player.AUDIO_FORMAT.getFrameSize();
		
		System.out.println(Player.AUDIO_FORMAT);
		System.out.println(Player.AUDIO_FORMAT.getFrameSize());
		AudioInputStream audioStream = new AudioInputStream(new ByteArrayInputStream(soundByteArray),
				Player.AUDIO_FORMAT,
                audioLength);
		
	//	audioStream = AudioSystem.getAudioInputStream(Encoding.PCM_UNSIGNED, audioStream);
		
		
		
		//Do the writing
		
		long nWrittenBytes;
		
		try {
			nWrittenBytes = AudioSystem.write(audioStream, targetFileType, fileToSaveTo);
		} catch (IOException e) {
			

			JOptionPane.showMessageDialog(parentComponent, "Failed to write to file: " + e.getMessage());
			
			e.printStackTrace();
			
			return;
		}
		
		JOptionPane.showMessageDialog(parentComponent, "Successfully wrote " + nWrittenBytes + " bytes to file: \n"+ fileToSaveTo.getAbsolutePath());
		
	}

	private boolean checkForMissingSound() {

		for(Word w : words){
			
			if(w.getSoundFile()==null){
				
				JOptionPane.showMessageDialog(parentComponent, "The selected word \"" + w.getWord() + "\" does not have any sound. Please, fix this and try again.");
			
				return false;
			
			}
		
		}
		
		return true;
	}

	private boolean fetchFile() {
		
		JFileChooser fileChooser = new JFileChooser("Chose a file to export to...");
		
		FileFilter fileFilter = new FileFilter(){

			
			public boolean accept(File f) {
				
				if(f.isDirectory())
					return false;
				
				if(f.getName().endsWith(".WAV") ||
						f.getName().endsWith(".wav"))
					return true;
				
				return false;
				
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "WAW Files";
			}
			
		};
		
		fileChooser.addChoosableFileFilter(fileFilter);
		
		fileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
		
	    int returnVal = fileChooser.showSaveDialog(parentComponent);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	
	    	fileToSaveTo = fileChooser.getSelectedFile();
	    	
	    	if(fileToSaveTo == null || fileToSaveTo.isDirectory()){
	    		JOptionPane.showMessageDialog(parentComponent, "The file has to be an ordinary file.");
	    		return false;
	    	}
	    	
	       if(!(fileToSaveTo.getName().endsWith(".wav") ||
	    		   fileToSaveTo.getName().endsWith(".WAV")))
	    	   fileToSaveTo = new File(fileToSaveTo.getParentFile(), fileToSaveTo.getName() + ".wav");
	    	   
	    	
//	    	try {
	    		
	    		try {
					fileToSaveTo.createNewFile();
					return true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
	    		
	    		
	    		
//				FileOutputStream wawFileOutputStream = new FileOutputStream(toSaveTo);
//				
//				for(Word w : words){
//					wawFileOutputStream.write(w.getSoundFile());
//				}
//				
//				wawFileOutputStream.close();
				
//	    	} catch (FileNotFoundException e) {
//				JOptionPane.showMessageDialog(parentComponent, "Could not find file");
//				e.printStackTrace();
//			} catch (IOException e) {
//				JOptionPane.showMessageDialog(parentComponent, "Could not write to file");
//				e.printStackTrace();
//			}
	    	
	    		   
	    }else
	    	return false;
	}

	private static AudioInputStream convertEncoding(
			AudioFormat.Encoding targetEncoding,
			AudioInputStream sourceStream)
		{
			return AudioSystem.getAudioInputStream(targetEncoding,
												   sourceStream);
		}
	
}