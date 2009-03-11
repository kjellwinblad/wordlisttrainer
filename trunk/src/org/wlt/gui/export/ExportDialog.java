/**
 * 
 */
package org.wlt.gui.export;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.wlt.data.Word;
import org.wlt.export.HTMLFileExporter;
import org.wlt.export.WAVFileExporter;
import org.wlt.gui.sound.WordSoundEditorPanel;

/**
 * @author kjellw
 *
 */
public class ExportDialog extends JFrame {

	private List<Word> words;

	private ExportDialog thisPanel;

	private Component parentComponent;
	
	public ExportDialog(List<Word> words, Component parentComponent) {
		this.words = words;
		this.thisPanel = this;
		this.parentComponent = parentComponent;
		initialize();
	}

	private void initialize() {
		setTitle("Export selected words to...");
		
		JPanel exportButtonPanel = new JPanel();
		
		exportButtonPanel.setBorder(BorderFactory.createTitledBorder("Export to:"));
		
		
		exportButtonPanel.setLayout(new BoxLayout(exportButtonPanel, BoxLayout.Y_AXIS));

		add(exportButtonPanel, BorderLayout.CENTER);
		
		//Playlist with multiple sound files
		
		JButton playlistSoundFileExport = new JButton("Playlist with multiple sound files");
		
		playlistSoundFileExport.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
				
				PlaylistExportDialog exporter = new PlaylistExportDialog(words, parentComponent);
				
				exporter.setVisible(true);

							
			}
			
		});
		
		exportButtonPanel.add(playlistSoundFileExport);
		
		

		//WAW sound file
		
		
//		JButton wawSoundFileExport = new JButton("WAW Sound File");
//		
//		wawSoundFileExport.addActionListener(new ActionListener(){
//
//			public void actionPerformed(ActionEvent e) {
//				
//				setVisible(false);
//				
//				WAVFileExporter exporter = new WAVFileExporter();
//				
//				exporter.export(words, parentComponent);
//							
//			}
//			
//		});
//		
//		exportButtonPanel.add(wawSoundFileExport);
		
		JButton htmlFileExport = new JButton("HTML File");
		
		htmlFileExport.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
				
				HTMLFileExporter exporter = new HTMLFileExporter();
				
				exporter.export(words, parentComponent);
				
			}
			
		});
		
		exportButtonPanel.add(htmlFileExport);
		
		pack();
	}

	
}
