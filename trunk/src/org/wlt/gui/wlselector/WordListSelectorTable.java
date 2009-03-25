/**
 * 
 */
package org.wlt.gui.wlselector;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.wlt.data.WordList;

import sun.awt.windows.ThemeReader;

/**
 * @author kjellw
 *
 */
public class WordListSelectorTable extends JTable {


	private WordListSelectorTable thisTable;
	
	private WordListSelectorTableModel tableModel;
	
	public WordListSelectorTable() {
		
		thisTable = this;
		initialize();
	}

	private void initialize() {
		tableModel = new WordListSelectorTableModel();
		setModel(tableModel);
		tableModel.update();
	}
	
	public void updateData(){

		tableModel.update();
		repaint();

	}
	
	public WordList getElementAtRow(int row){
		return tableModel.getWordListAtRow(row);
	}
	
    class WordListSelectorTableModel extends AbstractTableModel {
        private String[] columnNames =  {
            	"Word List Name",
                "Language A",
                "Language B",
                "# of Items"};
        
        private List<WordList> wordListList = new LinkedList<WordList>();

        public int getColumnCount() {
            return columnNames.length;
        }
        
        

        public int getRowCount() {
            return wordListList.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            
        	if(col==0)
            	return wordListList.get(row).getWordListName();
            else if (col==1)
            	return wordListList.get(row).getLanguageA();
            else if (col==2)
            	return wordListList.get(row).getLanguageB();
            else if (col==3)
            	return wordListList.get(row).getNumberOfWordBindings();
        	
        	
        	return null;
        }

        public Class getColumnClass(int c) {
            if(c == 0)
            	return String.class;
            else if(c==1)
            	return String.class;
            else if(c==2)
            	return String.class;
            else if(c==3)
            	return Integer.class;
            else
            	return null;
            
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            
            fireTableCellUpdated(row, col);
        }
        
        public void update(){
        	try {
				wordListList = WordList.getWordListList();
				
			//Trigger gui update
				for(int row = 0; row < wordListList.size(); row++)
					for(int col = 0; col < getColumnCount(); col++)
						setValueAt(null, row, col);
				
        	} catch (Exception e) {
				JOptionPane.showMessageDialog(thisTable, e.getMessage(), "Could not get word list", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
			
        }
        
        public WordList getWordListAtRow(int row){
        	return wordListList.get(row);
        }

    }


}
