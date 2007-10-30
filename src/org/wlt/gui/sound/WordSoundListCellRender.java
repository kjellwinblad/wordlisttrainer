package org.wlt.gui.sound;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

import org.wlt.data.Word;

public class WordSoundListCellRender extends JPanel  implements ListCellRenderer {

	private JLabel wordLabel = new JLabel();
	
	JLabel languageLabel = new JLabel();
	
	private Word word;
	
	private Color defaultColor;

	public WordSoundListCellRender(){
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		setBorder(new LineBorder(Color.BLACK));
		
		add(wordLabel);
		
		add(Box.createHorizontalGlue());
		
		add(languageLabel);
	}
	
	public JPanel getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		this.word = (Word)value;
		setOpaque(true);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());           
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
//		defaultColor = isSelected ? Color.BLUE : word.getSoundFile()== null ? this.getBackground() : Color.GREEN;
        
        //setOpaque(true);
		initialize();
		
		return this;
	}
	

	private void initialize() {

		wordLabel.setText(word.getWord());
		
		
		
		languageLabel.setText("(" + word.getLanguage() + ")");
	
	}

}

