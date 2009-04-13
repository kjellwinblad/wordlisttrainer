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
import org.xiph.speex.spi.SpeexAudioFileWriter;
import org.xiph.speex.spi.SpeexEncoding;
import org.xiph.speex.spi.SpeexFileFormatType;

public class PlaylistFileExporter implements Exporter {

	private File fileToSaveTo;

	private Component parentComponent;

	private List<Word> words;

	private int soundLength;

	private Type soundType;

	private boolean showLanguage;

	private File outputDir;

	private boolean addSilentSoundStartAfterFirst;

	private double addSilentSoundStartAfterFirstSilentTime;

	private boolean addSilentSoundStartAfterFirstIncludeLengthOfNext;

	private boolean addSilentSoundStartAfterSecond;

	private double addSilentSoundStartAfterSecondSilentTime;

	private boolean addSilentSoundStartAfterSecondIncludeLengthOfNext;

	public void export(List<Word> words, Component parentComponent,
			Type soundType, boolean showLanguage,
			boolean addSilentSoundStartAfterFirst,
			double addSilentSoundStartAfterFirstSilentTime,
			boolean addSilentSoundStartAfterFirstIncludeLengthOfNext,
			boolean addSilentSoundStartAfterSecond,
			double addSilentSoundStartAfterSecondSilentTime,
			boolean addSilentSoundStartAfterSecondIncludeLengthOfNext,
			File outputDir) throws Exception {

		this.addSilentSoundStartAfterFirst = addSilentSoundStartAfterFirst;
		this.addSilentSoundStartAfterFirstSilentTime = addSilentSoundStartAfterFirstSilentTime;
		this.addSilentSoundStartAfterFirstIncludeLengthOfNext = addSilentSoundStartAfterFirstIncludeLengthOfNext;
		this.addSilentSoundStartAfterSecond = addSilentSoundStartAfterSecond;
		this.addSilentSoundStartAfterSecondSilentTime = addSilentSoundStartAfterSecondSilentTime;
		this.addSilentSoundStartAfterSecondIncludeLengthOfNext = addSilentSoundStartAfterSecondIncludeLengthOfNext;

		this.soundType = soundType;

		this.showLanguage = showLanguage;

		this.outputDir = outputDir;

		this.parentComponent = parentComponent;

		this.words = words;

		// Check if a word miss sound...
		try {
			if (!checkForMissingSound())
				return;

			new Thread(new Runnable() {

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

	private void saveSoundDataToFile() {

		try {

			int s = words.size();
			int prefixLength = (s < 10 ? 1 : (s < 100 ? 2 : (s < 1000 ? 3
					: (s < 10000 ? 4 : 7))));

			int cont = 0;

			boolean afterFirst = true;

			for (int n = 0; n < words.size(); n++) {

				Word w = words.get(n);

				cont++;

				byte[] sound = w.getSoundFile();

				String wordText = w.getWord();

				String wordLanguage = (showLanguage ? ("(" + w.getLanguage() + ")")
						: "");

				InputStream input = getInputStream(soundType, sound);

				int toFillWithZeros = prefixLength - (cont + "").length();

				File soundFileToWriteTo = new File(outputDir,
						zeroString(toFillWithZeros) + cont + "_"
								+ wordText.replace(' ', '_').replace('/', '|')
								+ wordLanguage + "." + soundType.getExtension());

				writeToFile(soundFileToWriteTo, input);

				if (afterFirst && addSilentSoundStartAfterFirst) {

					double lengthInSecondsSound = 0;
					if (addSilentSoundStartAfterFirstIncludeLengthOfNext) {
						
						int nextSoundLength = 0;
						
						if((n+1)<words.size())
							nextSoundLength = words.get(n+1).getSoundFile().length;
						
						
						lengthInSecondsSound = (((double) (8 * nextSoundLength)) / Player.AUDIO_FORMAT
								.getSampleSizeInBits())
								/ Player.AUDIO_FORMAT.getSampleRate();
					}

					double lengthOfSilenceSec = addSilentSoundStartAfterFirstSilentTime
							+ lengthInSecondsSound;

					if (lengthOfSilenceSec > 0) {

						// Produce the silent sound file

						cont++;

						produceSilentFile(cont, lengthOfSilenceSec, wordText, wordLanguage, prefixLength);

					}
				}
				
				if(!afterFirst && addSilentSoundStartAfterSecond) {

					double lengthInSecondsSound = 0;
					if (addSilentSoundStartAfterSecondIncludeLengthOfNext) {
						lengthInSecondsSound = (((double) (8 * sound.length)) / Player.AUDIO_FORMAT
								.getSampleSizeInBits())
								/ Player.AUDIO_FORMAT.getSampleRate();
					}

					double lengthOfSilenceSec = addSilentSoundStartAfterSecondSilentTime
							+ lengthInSecondsSound;

					if (lengthOfSilenceSec > 0) {

						// Produce the silent sound file

						cont++;

						produceSilentFile(cont, lengthOfSilenceSec, wordText, wordLanguage, prefixLength);

					}
				}

				if (afterFirst)
					afterFirst = false;
				else
					afterFirst = true;

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(parentComponent,
					"Could not create word list: " + e.getMessage());

			e.printStackTrace();
		}

		JOptionPane.showMessageDialog(parentComponent,
				"Successfully wrote files to dir: \n" + outputDir);

	}

	private void produceSilentFile(int cont, double lengthOfSilenceSec,
			String wordText, String wordLanguage, int prefixLength)
			throws Exception {

		int toFillWithZeros = prefixLength - (cont + "").length();

		File silentSoundFile = new File(outputDir, zeroString(toFillWithZeros)
				+ cont + "_" + wordText.replace(' ', '_').replace('/', '|')
				+ "(silence)" + wordLanguage + "." + soundType.getExtension());

		byte silentSound[] = new byte[(int) (lengthOfSilenceSec
				* Player.AUDIO_FORMAT.getSampleRate()
				* ((double) Player.AUDIO_FORMAT.getSampleSizeInBits()) / 8)];

		InputStream input = getInputStream(soundType, silentSound);

		writeToFile(silentSoundFile, input);

	}

	private InputStream getInputStream(Type soundType, byte[] sound)
			throws Exception {
		InputStream input = null;

		if (soundType == AudioFileFormat.Type.WAVE) {
			input = getByteArrayInputStreamForWAVE(sound);
		} else if (soundType == AudioFileFormat.Type.AU) {
			input = getByteArrayInputStreamForAU(sound);
		} else if (soundType == AudioFileFormat.Type.AIFF) {
			input = getByteArrayInputStreamForAIFF(sound);
		} else if (soundType == SpeexFileFormatType.SPEEX) {
			input = getByteArrayInputStreamForSPEEX(sound);
		}

		return input;

	}

	private void writeToFile(File soundFileToWriteTo, InputStream input)
			throws Exception {

		OutputStream outputStream = new FileOutputStream(soundFileToWriteTo);

		int read = 0;

		while ((read = input.read()) != -1)
			outputStream.write(read);

		outputStream.flush();

		outputStream.close();

	}

	private InputStream getByteArrayInputStreamForSPEEX(byte[] sound)
			throws Exception {

		InputStream input = new ByteArrayInputStream(sound);

		AudioInputStream ais = new AudioInputStream(input, Player.AUDIO_FORMAT,
				sound.length / Player.AUDIO_FORMAT.getFrameSize());

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		SpeexEncoding speexEncode = new SpeexEncoding("SPEEX_Q8", 8, false);

		AudioFormat targetFormat = new AudioFormat(speexEncode,
				Player.AUDIO_FORMAT.getSampleRate(), -1, Player.AUDIO_FORMAT
						.getChannels(), -1, -1, false);

		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
				targetFormat, ais);

		SpeexAudioFileWriter speexWriter = new SpeexAudioFileWriter();

		speexWriter.write(audioInputStream, SpeexFileFormatType.SPEEX, output);

		return new ByteArrayInputStream(output.toByteArray());

	}

	private String zeroString(int toFillWithZeros) {
		// TODO Auto-generated method stub
		String zeros = "";
		for (int i = 0; i < toFillWithZeros; i++) {
			zeros = zeros + "0";
		}

		return zeros;
	}

	private InputStream getByteArrayInputStreamForAIFF(byte[] sound)
			throws Exception {

		InputStream input = new ByteArrayInputStream(sound);

		AudioInputStream ais = new AudioInputStream(input, Player.AUDIO_FORMAT,
				sound.length / Player.AUDIO_FORMAT.getFrameSize());

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		AudioSystem.write(ais, AudioFileFormat.Type.AIFF, output);

		return new ByteArrayInputStream(output.toByteArray());

	}

	private InputStream getByteArrayInputStreamForAU(byte[] sound)
			throws Exception {

		InputStream input = new ByteArrayInputStream(sound);

		AudioInputStream ais = new AudioInputStream(input, Player.AUDIO_FORMAT,
				sound.length / Player.AUDIO_FORMAT.getFrameSize());

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		AudioSystem.write(ais, AudioFileFormat.Type.AU, output);

		return new ByteArrayInputStream(output.toByteArray());

	}

	private InputStream getByteArrayInputStreamForWAVE(byte[] sound)
			throws Exception {

		InputStream input = new ByteArrayInputStream(sound);

		AudioInputStream ais = new AudioInputStream(input, Player.AUDIO_FORMAT,
				sound.length / Player.AUDIO_FORMAT.getFrameSize());

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
