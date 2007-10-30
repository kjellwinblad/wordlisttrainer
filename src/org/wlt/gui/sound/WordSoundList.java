package org.wlt.gui.sound;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.wlt.data.Word;

public class WordSoundList extends JList{

	private List<Word> words;
	
	public WordSoundList(List<Word> words) {
		this.words = words;

		initialize();
	}

	private void initialize() {
		
		setCellRenderer(new WordSoundListCellRender());
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		Word wordsArray[] = new Word[words.size()];
		
		wordsArray = words.toArray(wordsArray);
		
		setListData(wordsArray);
		
		setSelectedIndex(0);
	}

	public void selectNextItem() {
		System.out.println("SLECT NEXT ITEM");
		if(getSelectedIndex()== words.size()-1)
			setSelectedIndex(0);
		else
			setSelectedIndex(getSelectedIndex() + 1);
		
	}
	 

}
