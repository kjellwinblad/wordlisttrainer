/*
 *	SimpleAudioPlayer.java
 *
 *	This file is part of jsresources.org
 */

/*
 * Copyright (c) 1999 - 2001 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
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

/*
 |<---            this code is formatted to fit into 80 columns             --->|
 */

package org.wlt.api.sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import javazoom.jlGui.BasicPlayer;

import org.xiph.speex.spi.Speex2PcmAudioInputStream;

/**
 * <titleabbrev>SimpleAudioPlayer</titleabbrev> <title>Playing an audio file
 * (easy)</title>
 * 
 * <formalpara><title>Purpose</title> <para>Plays a single audio file.</para></formalpara>
 * 
 * <formalpara><title>Usage</title> <cmdsynopsis> <command>java
 * SimpleAudioPlayer</command> <replaceable class="parameter">audiofile</replaceable>
 * </cmdsynopsis> </formalpara>
 * 
 * <formalpara><title>Parameters</title> <variablelist> <varlistentry> <term><option><replaceable
 * class="parameter">audiofile</replaceable></option></term> <listitem><para>the
 * name of the audio file that should be played</para></listitem>
 * </varlistentry> </variablelist> </formalpara>
 * 
 * <formalpara><title>Bugs, limitations</title>
 * 
 * <para>Only PCM encoded files are supported. A-law, &mu;-law, ADPCM, ogg
 * vorbis, mp3 and other compressed data formats are not supported. For playing
 * these, see <olink targetdoc="AudioPlayer" targetptr="AudioPlayer">AudioPlayer</olink>.</para>
 * 
 * </formalpara>
 * 
 * <formalpara><title>Source code</title> <para> <ulink
 * url="SimpleAudioPlayer.java.html">SimpleAudioPlayer.java</ulink> </para>
 * </formalpara>
 * 
 */
public class Player {

	public static final AudioFormat AUDIO_FORMAT;

	private static final int EXTERNAL_BUFFER_SIZE = 128000;

	private static boolean playing;

	private static List<IsPlayingListener> isPlayingListeners = new LinkedList<IsPlayingListener>();

//<<<<<<< .mine
	static {
//		float sampleRate = 8000;
//		int sampleSizeInBits = 16;
//		int channels = 1;
//		boolean signed = true;
//		boolean bigEndian = true;
		
	    float sampleRate = 8000;
	    int sampleSizeInBits = 8;
	    int channels = 1;
	    boolean signed = true;
	    boolean bigEndian = true;

		//AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
		//		44100.0F, 16, 2, 4, 44100.0F, false);
		//AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
		//		8000.0F, 16, 2, 4, 8000.0F, false);
	    
	   //AUDIO_FORMAT = new AudioFormat(
        //        AudioFormat.Encoding.PCM_SIGNED,
        //        8000.0F, 16, 2, 4, 8000.0F, false);
	    
	    AUDIO_FORMAT = new AudioFormat(8000, 16, 1, true, false);
	    	
	    	
	    	//new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
			//	44100.0F, 16, 2, 4, 44100.0F, false);

		//AUDIO_FORMAT = new AudioFormat(sampleRate,
		// sampleSizeInBits, channels, signed, bigEndian);
//=======
//	static {
//		float sampleRate = 8000;
//		int sampleSizeInBits = 8;
//		int channels = 1;
//		boolean signed = true;
//		boolean bigEndian = true;
//		AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
//				44100.0F, 16, 2, 4, 44100.0F, false);// new
//														// AudioFormat(sampleRate,
//		// sampleSizeInBits, channels, signed, bigEndian);
//>>>>>>> .r8

	}

	public static void addIsPlayinggListener(IsPlayingListener l) {
		isPlayingListeners.add(l);

	}

	public static void removeIsPlayingListener(IsPlayingListener l) {
		isPlayingListeners.remove(l);

	}

	public static synchronized void play(byte[] sound) throws Exception {
		System.out.println("play");
		setPlaying(true);
		// float sampleRate = 8000;
		// int sampleSizeInBits = 8;
		// int channels = 1;
		// boolean signed = true;
		// boolean bigEndian = true;
		// AudioFormat format = new AudioFormat(sampleRate,
		// sampleSizeInBits, channels, signed, bigEndian);

	
		InputStream input = new ByteArrayInputStream(sound);

//		BasicPlayer basicPlayer = new BasicPlayer();
//		
//		basicPlayer.setDataSource(input);
//		
//		basicPlayer.startPlayback();
//		
//		long length  = (long)basicPlayer.getTotalLengthInSeconds()*1000;
//		
//		Thread.currentThread().wait(length);
		
		

		
		//AudioInputStream ais = AudioSystem.getAudioInputStream (input);
		
		AudioInputStream ais =  new Speex2PcmAudioInputStream(input, AUDIO_FORMAT, AudioSystem.NOT_SPECIFIED);

		
		//AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(ais);
		
		System.out.println("Sound Length " + sound.length);
		
		AudioFormat audioFormat = ais.getFormat();
		
		
 System.out.println("AUDIO FORMAT " +audioFormat);
			
				//new AudioInputStream(input, AUDIO_FORMAT,
				//sound.length / AUDIO_FORMAT.getFrameSize());

		
		
		  int bytesPerFrame = 
			  audioFormat.getFrameSize();
			    if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
			    // some audio formats may have unspecified frame size
			    // in that case we may read any amount of bytes
			    bytesPerFrame = 1;
			  } 

		
		
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

		line.open(audioFormat);
		line.start();
		
		
		int bufferSize = (int) audioFormat.getSampleRate()
				* bytesPerFrame;
		byte buffer[] = new byte[bufferSize];

		int count;
		while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
			if (count > 0) {
				line.write(buffer, 0, count);
			}
		}
		line.drain();
		line.close();
		System.out.println("stop");
		setPlaying(false);

	}

	public synchronized static boolean isPlaying() {
		return Player.playing;
	}

	private synchronized static void setPlaying(boolean playing) {
		for (IsPlayingListener l : isPlayingListeners)
			l.isPlaying(playing);

		Player.playing = playing;
	}

}
