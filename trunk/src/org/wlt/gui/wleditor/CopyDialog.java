/**
 * 
 */
package org.wlt.gui.wleditor;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.wlt.data.WordBinding;
import org.wlt.data.WordList;

import com.jeta.forms.components.panel.FormPanel;

/**
 * @author kjellw
 * 
 */
public class CopyDialog extends JDialog {

	private FormPanel contentPanel;

	private WordList wlToCopyFrom;

	private JComboBox copyToComboBox;

	private AbstractButton copyButton;

	private int[] rowsToCopy;

	private CopyDialog thisComp;

	private JProgressBar progressBar;

	public CopyDialog(Dialog parentComp, WordList wlToCopyFrom, int[] rowsToCopy) {
		super(parentComp, "Copy selected rows to...");

		this.thisComp = this;
		this.wlToCopyFrom = wlToCopyFrom;
		
		this.rowsToCopy = rowsToCopy;
		initialize();
	}

	private void initialize() {

		contentPanel = new FormPanel("org/wlt/gui/wleditor/copy_rows.jfrm");

		copyToComboBox = contentPanel.getComboBox("copyToComboBox");
		
		progressBar = contentPanel.getProgressBar("progBar");
		
		// Find word lists that we can copy to
		ArrayList<WordList> wordListsAbleToCopyTo = new ArrayList<WordList>();
		try {
			List<WordList> wordLists = WordList.getWordListList();

			for (WordList wl : wordLists) {
				if (wl.sameLanguagesAs(wlToCopyFrom)
						&& wl.getDatabaseID() != wlToCopyFrom.getDatabaseID())
					wordListsAbleToCopyTo.add(wl);

			}
			
			 

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
		
		
		WordList[] lists = new WordList[wordListsAbleToCopyTo.size()];
		copyToComboBox.setModel(new DefaultComboBoxModel(wordListsAbleToCopyTo.toArray(lists)));

		copyButton = contentPanel.getButton("copyButton");
		
		copyButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				
				try {
					startCopy();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(thisComp, e.getMessage());
					e.printStackTrace();
				}
				
				
			}
			
		});

		add(contentPanel);

		pack();
	}
private float copied = 0;
	private void startCopy() throws Exception{
		final List<WordBinding> wordBindings = wlToCopyFrom.getWordBindings();
		
		if(copyToComboBox.getSelectedItem() instanceof WordList){
			
			final WordList toCopyTo = (WordList)copyToComboBox.getSelectedItem();
			final double totalNrRows = rowsToCopy.length;
			
			
			new Thread(new Runnable(){

				public void run() {
					

					for(int row :rowsToCopy){
						
						
						WordBinding wbToCopy = wordBindings.get(row);
						try {
							toCopyTo.addWordBinding(wbToCopy);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						SwingUtilities.invokeLater(new Runnable(){
							
							public void run() {
								copied = copied + 1;
								progressBar.setValue((int)((copied / totalNrRows)*100));
								
							}
							
						});
						}
					
					SwingUtilities.invokeLater(new Runnable(){
						
						public void run() {
							setVisible(false);
						}
						
					});
					
					
				}
				
			}).start();
			

			
			
		}else
			setVisible(false);
		
	}

}
