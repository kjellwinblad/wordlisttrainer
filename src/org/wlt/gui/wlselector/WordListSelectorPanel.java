/**
 * 
 */
package org.wlt.gui.wlselector;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import org.wlt.data.Word;
import org.wlt.data.WordBinding;
import org.wlt.data.WordList;
import org.wlt.gui.wleditor.WordListEditor;

;

/**
 * @author kjellw
 *
 */
public class WordListSelectorPanel extends JPanel {

	private JPanel thisPanel;
	
	private Frame owner;
	
	private WordListSelectorTable wordListTable;

	public WordListSelectorPanel(Frame owner){
		this.owner = owner;
		thisPanel = this;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		JTable table = getWordListTable();
		JScrollPane scrollPane = new JScrollPane(table);
		//table.setFillsViewportHeight(true);
		
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		
		JToolBar toolBar = new JToolBar();
		BoxLayout boxLayout = new BoxLayout(controlPanel, BoxLayout.X_AXIS);
		controlPanel.setLayout(boxLayout);
		
		JButton addButton = new JButton();
		addButton.setToolTipText("Add Word List");
		addButton.setIcon(new ImageIcon(getClass().getResource("/images/edit_add.png"), "add word list"));
		
		
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				addWordList();
			}
		});
		
		toolBar.add(addButton);
		
		JButton editButton = new JButton();
		editButton.setToolTipText("Edit Selected Item");
		editButton.setIcon(new ImageIcon(getClass().getResource("/images/pencil.png"), "edit selected"));
		
		
		
		editButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				editWordList();
			}
		});
		
		toolBar.add(editButton);
		
		JButton deleteButton = new JButton();
		deleteButton.setToolTipText("Delete Selected Item(s)");
		deleteButton.setIcon(new ImageIcon(getClass().getResource("/images/editdelete.png"), "delete list(s)"));
		
		
		deleteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog(thisPanel, "Are you sure that you want to delete the selected word list(s)?"))
					deleteWordList();
			}
		});
		
		toolBar.add(deleteButton);
		
		controlPanel.add(toolBar);
		
		add(controlPanel, BorderLayout.SOUTH);
		

		
	}
	
	private void addWordList() {
		WordList wordList = new WordList();

		wordList.setLanguageA("Swedish");
		wordList.setLanguageB("Russian");
		
		wordList.setWordListName("New List");
		
		try {
			wordList.createNewInDatabase();
			
			wordListTable.updateData();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Could not create word list", JOptionPane.ERROR_MESSAGE);

			e.printStackTrace();
		}
		
		
	}
	
	private void editWordList() {
		if(wordListTable.getSelectedRow()!=-1){
		WordList selectedWordList = wordListTable.getElementAtRow(wordListTable.getSelectedRow());
		final WordListEditor wlEditor = new WordListEditor(owner, selectedWordList);
		wlEditor.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				try {
					wlEditor.getWordList().saveToDatabase();

					wordListTable.updateData();
					wordListTable.repaint();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(thisPanel, e1.getMessage(), "Could not save word list", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
				owner.setEnabled(true);
				
			}	
		});
		wlEditor.setVisible(true);
		}
	}
	
	private void deleteWordList() {
		int rows[] = getWordListTable().getSelectedRows();
		for(int row : rows){
			try {
				wordListTable.getElementAtRow(row).removeFromDatabase();
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Could not delete word list(s)", JOptionPane.ERROR_MESSAGE);

				e.printStackTrace();
			}
			
		}
		
		wordListTable.updateData();
		wordListTable.repaint();
		
	}

	private WordListSelectorTable getWordListTable() {
		
		if(wordListTable == null)
			wordListTable = new WordListSelectorTable();
		
		return wordListTable;
	}

	public void update() {
		getWordListTable().updateData();
	}

	public List<WordList> getSelected() {

		List<WordList> selected = new LinkedList<WordList>();
		
		int[] selectedRows = wordListTable.getSelectedRows();
		
		for(int n : selectedRows){
			WordList selectedWordList = wordListTable.getElementAtRow(n);
		
			selected.add(selectedWordList);
		}
		
		return selected;
	}
	
	
}
