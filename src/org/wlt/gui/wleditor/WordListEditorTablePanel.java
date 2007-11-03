/**
 * 
 */
package org.wlt.gui.wleditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.wlt.data.Word;
import org.wlt.data.WordBinding;
import org.wlt.data.WordList;
import org.wlt.gui.sound.WordSoundEditor;

/**
 * @author kjellw
 * 
 */
public class WordListEditorTablePanel extends JPanel {

	private WordList wordList;
	WordListEditorTable wordListEditorTable;
	private JPanel controlPanel;

	public WordListEditorTablePanel(WordList wordList) {
		this.wordList = wordList;

		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		JPanel wordListEditorTablePanel = new JPanel();
		wordListEditorTablePanel.setLayout(new BorderLayout());
		wordListEditorTable = new WordListEditorTable(wordList);
		JScrollPane wordListEditorTableScrollPane = new JScrollPane(
				wordListEditorTable);

		wordListEditorTable.setFillsViewportHeight(true);
		wordListEditorTablePanel.add(wordListEditorTableScrollPane,
				BorderLayout.CENTER);

		add(wordListEditorTablePanel, BorderLayout.CENTER);

		add(getControlPanel(), BorderLayout.SOUTH);
	}

	private JPanel getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new JPanel();
			controlPanel.setLayout(new FlowLayout());

			JButton addButton = new JButton("Add Word Binding");

			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addWordBinding();
				}
			});

			controlPanel.add(addButton);

			JButton openInRecorEditorButton = new JButton(
					"Open selected in sound editor");

			openInRecorEditorButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openSelectedInSoundEditor();
				}
			});

			controlPanel.add(openInRecorEditorButton);

			JButton deleteButton = new JButton("Delete Selected Item(s)");

			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteSelectedWordBindings();
				}
			});

			controlPanel.add(deleteButton);
		}

		return controlPanel;
	}

	protected void openSelectedInSoundEditor() {
		int cols[] = wordListEditorTable.getSelectedColumns();
		int rows[] = wordListEditorTable.getSelectedRows();

		if(cols.length==2 && !wordListEditorTable.isLangAFirst()){
			cols[0]= 1;
			cols[1]= 0;
		}
		
		List<Word> words = new LinkedList<Word>();

		for (int row : rows)
			for (int col : cols)
				words.add(wordListEditorTable.getWordAt(col, row));

		new WordSoundEditor(words).setVisible(true);
	}

	private void addWordBinding() {
		Word wordA = new Word();
		wordA.setLanguage(wordList.getLanguageA());
		wordA.setWord("");

		Word wordB = new Word();
		wordB.setLanguage(wordList.getLanguageB());
		wordB.setWord("");

		WordBinding newWordBinding = new WordBinding(wordList);

		newWordBinding.setWordA(wordA);

		newWordBinding.setWordB(wordB);

		wordList.getWordBindings().add(newWordBinding);

		wordListEditorTable.updateData();

	}

	private void deleteSelectedWordBindings() {
		int rows[] = wordListEditorTable.getSelectedRows();
		List<WordBinding> deleteRows = new LinkedList<WordBinding>();
		WordBinding wb;
		for (int row : rows) {
			try {

				wb = wordListEditorTable.getWordBindings().get(row);
				deleteRows.add(wb);
				wb.removeFromDatabase();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(),
						"Could not delete word list(s)",
						JOptionPane.ERROR_MESSAGE);

				e.printStackTrace();
			}

		}
		for (WordBinding wobi : deleteRows)
			wordListEditorTable.getWordBindings().remove(wobi);

		wordListEditorTable.updateData();
		wordListEditorTable.repaint();

	}

}