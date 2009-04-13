/**
 * 
 */
package org.wlt.gui.wleditor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;

import org.wlt.data.Word;
import org.wlt.data.WordBinding;
import org.wlt.data.WordList;

/**
 * @author kjellw
 * 
 */
public class WordListEditorTable extends JTable {

	private boolean langAFirst = true;

	private WordList wordList;

	private List<WordBinding> wordBindings;

	private WordListEditorTableModel tableModel;

	private WordListEditorTable thisTable;

	public WordListEditorTable(WordList wordList) {
		thisTable = this;
		this.wordList = wordList;
		
		   setColumnSelectionAllowed(true);
		   setRowSelectionAllowed(true);
		
		try {
			this.wordBindings = wordList.getWordBindings();
			

			
			
		} catch (Exception e1) {
			
			JOptionPane.showMessageDialog(this,
					"Problems with connection to database\n" + e1.getMessage(),
					"Connection problem", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		this.tableModel = new WordListEditorTableModel();
		setModel(tableModel);
		
		if(getRowCount()==0){
			addRow();

		}

		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setRowSelectionAllowed(true);
		setColumnSelectionAllowed(true);
		setCellSelectionEnabled(true);
		
		addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent arg0) {
				checkForAutomaticAddRow();
				
			}

			public void keyReleased(KeyEvent arg0) {
				checkForAutomaticAddRow();
				
			}

			public void keyTyped(KeyEvent arg0) {
				checkForAutomaticAddRow();
				
			}
			
		});
		
		addMouseListener(new MouseAdapter(){

			public void mouseClicked(MouseEvent arg0) {
				checkForAutomaticAddRow();
				
			}


			public void mousePressed(MouseEvent arg0) {
				checkForAutomaticAddRow();
				
			}

			public void mouseReleased(MouseEvent arg0) {
				checkForAutomaticAddRow();
				
			}
			
		});
		getColumnModel().addColumnModelListener(new TableColumnModelListener() {

			public void columnMoved(TableColumnModelEvent e) {

				langAFirst = !getColumnModel().getColumn(0).getHeaderValue()
						.equals("Language B");

			}

			public void columnAdded(TableColumnModelEvent e) {
				// TODO Auto-generated method stub

			}

			public void columnMarginChanged(ChangeEvent e) {
				// TODO Auto-generated method stub

			}

			public void columnRemoved(TableColumnModelEvent e) {
				// TODO Auto-generated method stub

			}

			public void columnSelectionChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub

			}

		});
	}

	protected void checkForAutomaticAddRow() {

		if(getCellEditor()!=null){

			if(getEditingRow() ==(getRowCount()-1))
				addRow();
		}
			
		
	}

	class WordListEditorTableModel extends AbstractTableModel {

		public int getColumnCount() {
			return 2;
		}

		public int getRowCount() {
			return wordBindings.size();
		}

		public String getColumnName(int col) {
			if (col == 0)
				return "Language A";
			else
				return "Language B";
		}

		public Object getValueAt(int row, int col) {

			if (col == 0)
				return wordBindings.get(row).getWordA();
			else if (col == 1)
				return wordBindings.get(row).getWordB();

			return null;
		}

		public Class getColumnClass(int c) {
			if (c == 0)
				return String.class;
			else if (c == 1)
				return String.class;

			return null;

		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {
			if (value != null) {
				if (col == 0)
					wordBindings.get(row).getWordA().setWord(value.toString());
				if (col == 1)
					wordBindings.get(row).getWordB().setWord(value.toString());

			}
			fireTableCellUpdated(row, col);
		}

		public void update() {

		}

		public WordBinding getWordBindingAtRow(int row) {
			return wordBindings.get(row);
		}

		public void updateData() {
			try {
				wordBindings = wordList.getWordBindings();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(thisTable,
						"Problems with connection to database\n"
								+ e1.getMessage(), "Connection problem",
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			// Trigger gui update
			for (int row = 0; row < wordBindings.size(); row++)
				for (int col = 0; col < getColumnCount(); col++)
					setValueAt(null, row, col);

		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return true;
		}

	}

	public void updateData() {
		tableModel.updateData();

	}

	public WordBinding getElementAtRow(int row) {

		return tableModel.getWordBindingAtRow(row);
	}

	public List<WordBinding> getWordBindings() {
		return wordBindings;
	}

	public Word getWordAt(int col, int row) {
		if (col == 0)
			return getWordBindings().get(row).getWordA();
		else
			return getWordBindings().get(row).getWordB();
	}

	public boolean isLangAFirst() {
		return langAFirst;
	}

	public void addRow() {
		Word wordA = new Word();
		wordA.setLanguage(wordList.getLanguageA());
		wordA.setWord("");

		Word wordB = new Word();
		wordB.setLanguage(wordList.getLanguageB());
		wordB.setWord("");

		WordBinding newWordBinding = new WordBinding(wordList);

		newWordBinding.setWordA(wordA);

		newWordBinding.setWordB(wordB);
try{
		wordList.getWordBindings().add(newWordBinding);
	} catch (Exception e1) {
		JOptionPane.showMessageDialog(this,
				"Problems with connection to database\n"
						+ e1.getMessage(), "Connection problem",
				JOptionPane.ERROR_MESSAGE);
		e1.printStackTrace();
	}
		updateData();
		
	}

}
