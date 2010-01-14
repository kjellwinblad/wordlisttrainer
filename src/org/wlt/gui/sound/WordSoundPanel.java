/**
 * 
 */
package org.wlt.gui.sound;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javax.swing.border.LineBorder;

import org.wlt.api.sound.IsPlayingListener;
import org.wlt.api.sound.IsRecordingListener;
import org.wlt.api.sound.Player;
import org.wlt.api.sound.Recorder;
import org.wlt.data.Word;

import com.sun.org.omg.CORBA.Initializer;

/**
 * @author kjellw
 * 
 */
public class WordSoundPanel extends JPanel {

	private Word word;

	private boolean isRecording = false;

	private JButton recButton;

	private JButton playButton;

	private Color defaultColor;

	private WordSoundPanel thisPanel;

	private JLabel wordLabel;

	private JLabel languageLabel;

	private WordSoundList wordSoundList;

	public WordSoundPanel(Word word, WordSoundList wordSoundList) {

		this.wordSoundList = wordSoundList;

		this.word = word;

		thisPanel = this;
		defaultColor = this.getBackground();
		// try {
		// defaultColor = word.getSoundFile()==null ? : Color.GREEN;
		// } catch (Exception e) {
		// JOptionPane.showMessageDialog(this, "Could not read sound file: " +
		// e.getMessage());
		// e.printStackTrace();
		// }

		initialize();
	}

	private void initialize() {

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		setBorder(new LineBorder(Color.BLACK));

		setOpaque(true);

		setBackground(defaultColor);

		wordLabel = new JLabel(word.getWord());

		add(wordLabel);

		add(Box.createHorizontalGlue());

		languageLabel = new JLabel("(" + word.getLanguage() + ")");

		add(languageLabel);

		recButton = new JButton("record");

		add(recButton);

		recButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				recordOrStopRecord();
			}

		});

		playButton = new JButton("play");
		add(playButton);

		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playSound();

			}
		});

		Player.addIsPlayinggListener(new IsPlayingListener() {

			public void isPlaying(boolean recording) {
				recButton.setEnabled(!recording);
				playButton.setEnabled(!recording);
			}

		});
		Recorder.addIsRecordingListener(new IsRecordingListener() {

			public void isRecording(final boolean recording) {
				if (thisPanel.isRecording())
					return;

				recButton.setEnabled(!recording);
				playButton.setEnabled(!recording);

			}

		});

		setFocusable(true);
		addKeyListener(new KeyAdapter() {

			private Long timer = System.currentTimeMillis();

			private boolean started = false;

			public void keyPressed(KeyEvent e) {

			}

		});

	}

	protected void playSound() {

		final byte[] sound;
		try {
			sound = word.getSoundFile();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, "Could not read sound data: "
					+ e1.getMessage());

			e1.printStackTrace();

			return;
		}
		if (sound == null) {
			JOptionPane.showMessageDialog(this,
					"The word doesn't have any sound data", "No sound data",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		new Thread() {
			public void run() {

				try {
					Player.play(sound);
				} catch (final Exception e) {
					SwingUtilities.invokeLater(new Runnable() {

						public void run() {
							JOptionPane.showMessageDialog(thisPanel,
									"Could not play sound", e.getMessage(),
									JOptionPane.ERROR_MESSAGE);

						}

					});
					e.printStackTrace();
				}

			}
		}.start();

	}

	private void recordOrStopRecord() {
		if (isRecording() == false) {
			try {
				thisPanel.setRecording(true);
				Recorder.record();

				setBackground(Color.RED);
				recButton.setText("stop record");
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				thisPanel.setRecording(false);
				e1.printStackTrace();
			}

		} else {
			recButton.setText("stop");
			try {
				final Word recordedWord = word;

				final byte[] sound = Recorder.stopRecording();
				
				new Thread(new Runnable() {

					public void run() {
						try {
							
							

							recordedWord.setSoundFile(sound);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							thisPanel.setRecording(false);
							e1.printStackTrace();
						}
					}

				}).start();

				// if(sound!=null){

				// setBackground(Color.GREEN);

				// }

				recButton.setText("record");

				thisPanel.setRecording(false);

				setBackground(Color.GREEN);

				wordSoundList.selectNextItem();

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				thisPanel.setRecording(false);
				e1.printStackTrace();
			}
		}
	}

	synchronized public boolean isRecording() {
		return isRecording;
	}

	synchronized public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}

	public void fireKey(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_A && !isRecording) {

			recordOrStopRecord();
		} else if (e.getKeyCode() == KeyEvent.VK_S && isRecording) {

			recordOrStopRecord();
		} else if (e.isActionKey()) {

			recordOrStopRecord();
		}

		// if(isRecording && !started)
		// return;
		//		
		//
		//		
		// if(started){
		// synchronized (timer) {
		// timer = System.currentTimeMillis();
		// }
		// return;
		// }
		//
		// if(e.getKeyCode()==KeyEvent.VK_ENTER && selected && !isRecording){
		// isRecording = true;
		// recordOrStopRecord();
		// started = true;
		// new Thread(new Runnable(){
		//
		// @Override
		// public void run() {
		// while(true){
		// synchronized (timer) {
		// if((System.currentTimeMillis() - timer) > 1000 )
		// SwingUtilities.invokeLater(new Runnable(){
		//
		// @Override
		// public void run() {
		// System.out
		// .println("STOP");
		// recordOrStopRecord();
		// started = false;
		// }
		//									
		// });
		// }
		// Thread.yield();
		// }
		//					
		// }
		//				
		// }).start();
		//			
		// }

	}

	public Word getWord() {
		return word;

	}

	/**
	 * Sets the record panel to a new word. If the panel is in record mode an
	 * exception is thrown
	 * 
	 * @param word
	 */
	public void setWord(Word word) throws Exception {
		if (this.isRecording())
			throw new Exception("The word is being recorded");

		this.word = word;
		defaultColor = word.getSoundFile() == null ? this.defaultColor
				: Color.GREEN;
		setBackground(defaultColor);
		this.wordLabel.setText(word.getWord());
		this.languageLabel.setText("(" + word.getLanguage() + ")");
	}

}
