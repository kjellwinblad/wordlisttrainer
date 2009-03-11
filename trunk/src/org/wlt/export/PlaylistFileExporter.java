//<<<<<<< .mine
package org.wlt.export;

import java.awt.Component;
import java.awt.Frame;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.wlt.api.sound.Player;
import org.wlt.data.Word;
import org.xiph.speex.spi.Speex2PcmAudioInputStream;
import org.xiph.speex.spi.SpeexFileFormatType;

public class PlaylistFileExporter implements Exporter {

	private File fileToSaveTo;

	private Component parentComponent;

	private List<Word> words;

	private int soundLength;

	private Type soundType;

	private boolean showLanguage;

	private File outputDir;

	public void export(List<Word> words, Component parentComponent,
			Type soundType, boolean showLanguage, File outputDir)
			throws Exception {

		this.soundType = soundType;

		this.showLanguage = showLanguage;

		this.outputDir = outputDir;

		this.parentComponent = parentComponent;

		this.words = words;

		// Check if a word miss sound...
		try {
			if (!checkForMissingSound())
				return;

			
			new Thread(new Runnable(){
			
				

				public void run() {
					saveSoundDataToFile();
					
				}
			
			}).start();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(parentComponent,
					"Error when copying: " + e.getMessage());

			e.printStackTrace();
		}

	}

	private void saveSoundDataToFile()  {
		
		try{
		System.out.println("SAVE SOUND DATA");

		int s = words.size();
		int prefixLength = (s < 10 ? 1 : (s < 100 ? 2 : (s < 1000 ? 3
				: (s < 10000 ? 4 : 7))));

		int cont = 0;
		for (Word w : words) {

			cont++;

			byte[] sound = w.getSoundFile();

			String wordText = w.getWord();

			String wordLanguage = (showLanguage ? ("(" + w.getLanguage() + ")")
					: "");

			InputStream input = null;
			
			if (soundType == AudioFileFormat.Type.WAVE) {
				input = getByteArrayInputStreamForWAVE(sound);
			} else if (soundType == AudioFileFormat.Type.AU) {
				input = getByteArrayInputStreamForAU(sound);
			} else if (soundType == AudioFileFormat.Type.AIFF) {
				input = getByteArrayInputStreamForAIFF(sound);
			} else if (soundType == SpeexFileFormatType.SPEEX) {
				input = new ByteArrayInputStream(sound);
			}

			int toFillWithZeros = prefixLength - (cont + "").length();

			File soundFileToWriteTo = new File(outputDir,
					zeroString(toFillWithZeros) + cont + "_" + wordText
							+ wordLanguage + "." + soundType.getExtension());

			OutputStream outputStream = new FileOutputStream(soundFileToWriteTo);
			
			int read = 0;
			
			while ((read = input.read()) != -1)
				outputStream.write(read);

			outputStream.flush();

			outputStream.close();

		}
		
		}catch (Exception e) {
			JOptionPane.showMessageDialog(parentComponent, "Could not create word list: " + e.getMessage());
			
			e.printStackTrace();
		}

		JOptionPane.showMessageDialog(parentComponent,
				"Successfully wrote files to dir: \n" + outputDir);

	}

	private String zeroString(int toFillWithZeros) {
		// TODO Auto-generated method stub
		String zeros = "";
		for (int i = 0; i < toFillWithZeros; i++) {
			zeros = zeros + "0";
		}

		return zeros;
	}

	private InputStream getByteArrayInputStreamForAIFF(byte[] sound) throws Exception{
		
		InputStream input = new ByteArrayInputStream(sound);
		
		AudioInputStream ais =  new Speex2PcmAudioInputStream(input, Player.AUDIO_FORMAT,  100000/*AudioSystem.NOT_SPECIFIED*/);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		AudioSystem.write(ais, AudioFileFormat.Type.AIFF, output);
		
		return new ByteArrayInputStream(output.toByteArray());
		
	}

	private InputStream getByteArrayInputStreamForAU(byte[] sound) throws Exception {

		InputStream input = new ByteArrayInputStream(sound);
		
		AudioInputStream ais =  new Speex2PcmAudioInputStream(input, Player.AUDIO_FORMAT,  100000/*AudioSystem.NOT_SPECIFIED*/);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		AudioSystem.write(ais, AudioFileFormat.Type.AU, output);
		
		return new ByteArrayInputStream(output.toByteArray());
		
	}

	private InputStream getByteArrayInputStreamForWAVE(byte[] sound) throws Exception {
		
		InputStream input = new ByteArrayInputStream(sound);
		
	
		
		AudioInputStream ais =  new Speex2PcmAudioInputStream(input, Player.AUDIO_FORMAT, 100000/*AudioSystem.NOT_SPECIFIED*/);
		
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		AudioSystem.write(ais, AudioFileFormat.Type.WAVE, output);
		
		return new ByteArrayInputStream(output.toByteArray());

	}

	private boolean checkForMissingSound() throws Exception {

		for (Word w : words) {
			byte[] soundFileData = w.getSoundFile();
			if (soundFileData == null) {

				JOptionPane
						.showMessageDialog(
								parentComponent,
								"The selected word \""
										+ w.getWord()
										+ "\" does not have any sound. Please, fix this and try again.");

				return false;

			} else
				soundLength = soundLength + soundFileData.length;

		}

		return true;
	}

	private static AudioInputStream convertEncoding(
			AudioFormat.Encoding targetEncoding, AudioInputStream sourceStream) {
		return AudioSystem.getAudioInputStream(targetEncoding, sourceStream);
	}

	public void export(List<Word> words, Component frame) {
		// TODO Auto-generated method stub

	}

}
