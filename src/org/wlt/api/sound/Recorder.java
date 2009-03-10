/*
 * Copyright (c) 1999 - 2003 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright 
notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the 
distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.wlt.api.sound;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFileFormat.Type;

import org.xiph.speex.spi.Speex2PcmAudioInputStream;
import org.xiph.speex.spi.SpeexAudioFileWriter;
import org.xiph.speex.spi.SpeexEncoding;
import org.xiph.speex.spi.SpeexFileFormatType;
import org.xiph.speex.spi.SpeexFormatConvertionProvider;

/**
 * 
 */
public class Recorder extends Thread {
	private static List<IsRecordingListener> isRecordingListeners = new LinkedList<IsRecordingListener>();

	private TargetDataLine m_line;
	private AudioFormat m_targetType;
	private AudioInputStream m_audioInputStream;

	private ByteArrayOutputStream output = new ByteArrayOutputStream();
	private Exception e = null;

	private static boolean recording;

	private static Recorder recorder;

	public static void addIsRecordingListener(IsRecordingListener l) {
		isRecordingListeners.add(l);

	}

	public static void removeIsRecordingListener(IsRecordingListener l) {
		isRecordingListeners.remove(l);

	}

	public Recorder(TargetDataLine line, AudioFormat targetType) {
		m_line = line;
		m_audioInputStream = new AudioInputStream(line);
		m_targetType = targetType;
	}

	public static byte[] stopRecording() throws Exception {
if(isRecording()==false)
	throw new Exception("No recording started");
		
		return recorder.stopRecordingIN();

	}
	
	public byte[] stopRecordingIN() throws Exception {
		byte sound[];
		byte[] compressedData = null;
		try {
			
			System.out.println("START STOP");
			long time = System.currentTimeMillis();
			
			m_line.stop();
			
			System.out.println("STOP STOP");
			
			m_line.close();
			System.out.println(System.currentTimeMillis() - time);
			setRecording(false);
			externalTrigger = false;

			
System.out.println("stop");
			if (e != null)
				throw e;
			synchronized (runningLook) {
				
			
			sound = output.toByteArray();
			
			InputStream input = new ByteArrayInputStream(sound);
			
			AudioInputStream audioInputStream = new AudioInputStream(input, Player.AUDIO_FORMAT,
					sound.length / Player.AUDIO_FORMAT.getFrameSize());
			
			ByteArrayOutputStream compressedFileFormat = new ByteArrayOutputStream();
			AudioSystem.getAudioFileTypes();
			for (Type t : AudioSystem.getAudioFileTypes(audioInputStream)) {
				System.out.println(t);
			}
			
			
			SpeexEncoding speexEncode = new SpeexEncoding("SPEEX_Q8", 8, false);
			
			AudioFormat targetFormat = new AudioFormat(
					speexEncode,
					 Player.AUDIO_FORMAT.getSampleRate() ,
					        -1,
					        Player.AUDIO_FORMAT.getChannels(),  -1,  -1,  false); 
			
			audioInputStream =
				AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
			
			
			//SpeexFormatConvertionProvider converter = new SpeexFormatConvertionProvider();
			//AudioInputStream speexAudioInputStream = converter.getAudioInputStream(SpeexEncoding.SPEEX_Q8, audioInputStream);

			System.out.println(" Without the speex converter");
			
			for (Type t : AudioSystem.getAudioFileTypes(audioInputStream)) {
				System.out.println(t);
			}
			
			//System.out.println(" The speex converter streem");
			
			//for (Type t : AudioSystem.getAudioFileTypes(speexAudioInputStream)) {
			//	System.out.println(t);
			//}
			
			
			SpeexAudioFileWriter speexWriter = new SpeexAudioFileWriter();
			
			speexWriter.write(audioInputStream, SpeexFileFormatType.SPEEX, /*new File("/home/kjellw/Desktop/test.spx")*/compressedFileFormat);

			
			
			
			
			
			
			
			
//			   AudioFileFormat.Type fileType = null;
//				File audioFile = null;
//				fileType = SpeexFileFormatType.SPEEX;
//				audioFile = new File("sjunk.spx");
//
//				SpeexEncoding speexEncode = new SpeexEncoding("SPEEX_Q1", 3,
//						false);
//				AudioInputStream ais = new AudioInputStream(targetDataLine);
//				AudioFormat targetFormat = new AudioFormat(speexEncode,
//						audioFormat.getSampleRate(), -1, audioFormat
//								.getChannels(), -1, -1, false);
//
//				ais = AudioSystem.getAudioInputStream(targetFormat, ais);
//				try {
//					targetDataLine.open(audioFormat);
//
//					targetDataLine.start();
//
//					AudioSystem.write(ais, fileType, audioFile);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				} 
			
			
			
			
			
			
			
			
			
			
			System.out.println("SOUND LEN " + sound.length);
			
			compressedData = compressedFileFormat.toByteArray();
			System.out.println("Compressed Len " + compressedData.length);			
			}
			if (sound == null)
				throw new Exception("No recording done");

		} catch (Exception e) {
			setRecording(false);
			throw e;

		}
		
		return compressedData;

	}
	
	
private  Object runningLook = new Object();

private boolean externalTrigger;
	

public void run() {
		synchronized (runningLook) {
		m_line.start();
		externalTrigger = true;
		try {
			  int bufferSize = (int)m_targetType.getSampleRate() * 
			  m_targetType.getFrameSize();
			  byte buffer[] = new byte[bufferSize];

			  while (externalTrigger) {
			    int count = m_line.read(buffer, 0, buffer.length);
			    if (count > 0) {
			      output.write(buffer, 0, count);
			    }
			  }
			  output.close();
			
		} catch (Exception e) {
			this.e = e;
			
		}
		}
	}

	public static void record() throws Exception {
		if (isRecording())
			throw new Exception("Already recoding!");

		setRecording(true);
		try {


		    
		    DataLine.Info info = new DataLine.Info(
		    	    TargetDataLine.class, Player.AUDIO_FORMAT);
		    	  TargetDataLine line = (TargetDataLine)
		    	    AudioSystem.getLine(info);

		    	  line.open();
			recorder = new Recorder(line, Player.AUDIO_FORMAT);
			
			
			
			recorder.start();

		} catch (Exception e) {
			setRecording(false);
			throw e;

		}

	}

	private synchronized static boolean isRecording() {
		return recording;
	}

	private synchronized static void setRecording(boolean recording) {
		for (IsRecordingListener l : isRecordingListeners)
			l.isRecording(recording);

		Recorder.recording = recording;
	}
}