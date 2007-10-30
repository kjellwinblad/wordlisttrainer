/**
 * 
 */
package org.wlt.gui.wleditor;

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
		this.wordBindings = wordList.getWordBindings();
		this.tableModel = new WordListEditorTableModel();
		setModel(tableModel);
		
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setRowSelectionAllowed(true);
		setColumnSelectionAllowed(true);
		setCellSelectionEnabled(true);
		getColumnModel().addColumnModelListener(new TableColumnModelListener(){

			@Override
			public void columnMoved(TableColumnModelEvent e) {
				
				langAFirst = !getColumnModel().getColumn(0).getHeaderValue().equals("Language B");
				
			}

			@Override
			public void columnAdded(TableColumnModelEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void columnMarginChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void columnRemoved(TableColumnModelEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void columnSelectionChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	
    class WordListEditorTableModel extends AbstractTableModel {
        


        public int getColumnCount() {
            return 2;
        }
        
        

        public int getRowCount() {
            return wordBindings.size();
        }

        public String getColumnName(int col) {
            if(col == 0)
            	return "Language A";
            else
            	return "Language B";
        }

        public Object getValueAt(int row, int col) {
            
        	if(col==0)
            	return wordBindings.get(row).getWordA();
            else if (col==1)
            	return wordBindings.get(row).getWordB();
        	
        	
        	return null;
        }

        public Class getColumnClass(int c) {
            if(c == 0)
            	return String.class;
            else if(c==1)
            	return String.class;

            return null;
            
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if(value!=null) {
        	if(col==0)
            	wordBindings.get(row).getWordA().setWord(value.toString());
            if(col==1)
            	wordBindings.get(row).getWordB().setWord(value.toString());
        	
            }
            fireTableCellUpdated(row, col);
        }
        
        public void update(){

			
        }
        
        public WordBinding getWordBindingAtRow(int row){
        	return wordBindings.get(row);
        }



		public void updateData() {
 
				wordBindings = wordList.getWordBindings();
				
			//Trigger gui update
				for(int row = 0; row < wordBindings.size(); row++)
					for(int col = 0; col < getColumnCount(); col++)
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
	
	public Word getWordAt(int col, int row){
		if(col==0)
			return getWordBindings().get(row).getWordA();
		else
			return getWordBindings().get(row).getWordB();
	}

	public boolean isLangAFirst() {
		return langAFirst;
	}


	

}
