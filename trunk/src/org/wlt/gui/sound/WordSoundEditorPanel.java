package org.wlt.gui.sound;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.wlt.api.sound.Player;
import org.wlt.data.Word;

public class WordSoundEditorPanel extends JPanel{

	private WordSoundList wordSoundList;
	
	private WordSoundPanel wordSoundPanel;
	
	private JPanel controlPanel;
	
	private List<Word> words;

	private JButton playListButton;
	
	public WordSoundEditorPanel(List<Word> words) {
		this.words = words;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		
		wordSoundList = new WordSoundList(words);
		
		JScrollPane listScrollPane = new JScrollPane(wordSoundList);
		
		wordSoundPanel = new WordSoundPanel(words.get(0), wordSoundList);
		
		add(wordSoundPanel, BorderLayout.NORTH);

		add(listScrollPane, BorderLayout.CENTER);

		wordSoundList.addListSelectionListener(new ListSelectionListener(){

			
			public void valueChanged(ListSelectionEvent e) {
				try{
					wordSoundPanel.setWord(words.get(wordSoundList.getSelectedIndex()));
				}catch(Exception exep){
					exep.printStackTrace();
				}
			}
			
		});
		
		add(getControlPanel(), BorderLayout.SOUTH);
		
	}
	
	
	private JPanel getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new JPanel();
			controlPanel.setLayout(new FlowLayout());

			playListButton = new JButton("Play Word List");

			playListButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					playWordList();
				}
			});

			controlPanel.add(playListButton);

		}

		return controlPanel;
	}
	
	private AtomicBoolean isPlaying = new AtomicBoolean(false);
	private AtomicBoolean stop = new AtomicBoolean(false);
	private long playStart;
	private void playWordList() {
		
		
		if(isPlaying.get())
			stop.set(true);
		else{
			isPlaying.set(true);
			playListButton.setText("Stop Playing Word List");
		new Thread(new Runnable(){
			

			public void run() {
				for(Word word :words){
					final Word w = word;
					
					try {
					
						SwingUtilities.invokeAndWait(new Runnable(){
							public void run() {

								wordSoundList.setSelectedValue(w, true);
								wordSoundPanel.playSound();
								playStart = System.currentTimeMillis();
								
							}	
						});
					
					} catch (Exception e) {
						JOptionPane.showMessageDialog(wordSoundPanel, "Could not play sound");
						e.printStackTrace();
					}
					while((System.currentTimeMillis() - playStart < 200) || Player.isPlaying())
						Thread.yield();
					

					
					if(stop.get()){
						break;
						
					}
					
				}
				stop.set(false);
				
				try {
					SwingUtilities.invokeAndWait(new Runnable(){
						public void run() {
							playListButton.setText("Play Word List");
						}	
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isPlaying.set(false);	
			}
			
		}).start();

		
		}
		
	}

	private void exportWordList() {
		// TODO Auto-generated method stub
		
	}
}


