/**
 * 
 */
package org.wlt.gui.sound;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;

import org.wlt.data.Word;

import com.sun.org.omg.CORBA.Initializer;

/**
 * @author kjellw
 *
 */
public class WordSoundEditor extends JFrame {

	private List<Word> words;
	private WordSoundEditorPanel wordSoundEditorPanel;
	
	public WordSoundEditor(List<Word> words) {
		this.words = words;
		initialize();
	}

	private void initialize() {
		setTitle("Word Sound Editor");
		
		setLayout(new BorderLayout());
		
		wordSoundEditorPanel = new WordSoundEditorPanel(words);
		
		add(wordSoundEditorPanel, BorderLayout.CENTER);
		
		pack();
	}

	
	
}
