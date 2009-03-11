package org.wlt.gui.export;

import java.awt.BorderLayout;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.spi.FormatConversionProvider;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JComboBox;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JCheckBox;

import org.wlt.api.sound.Player;
import org.wlt.data.Word;
import org.wlt.export.PlaylistFileExporter;


public class PlaylistExportDialog extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JComboBox jComboBox = null;

	private Component thisComp = this;
	
	private JPanel jPanel1 = null;

	private JLabel jLabel = null;

	private JButton jButton = null;

	private JPanel jPanel2 = null;

	private JCheckBox jCheckBox = null;

	private JButton jButton1 = null;
	
	private File exportDir = new File(System.getProperty("user.home"));
	
	private List<Word> words;
	
	private Component parentComponent;

	/**
	 * This is the default constructor
	 */
	public PlaylistExportDialog() {
		super();
		initialize();
	}

	public PlaylistExportDialog(List<Word> words, Component parentComponent) {
		this();
		this.words = words;
		this.parentComponent = parentComponent;
		
		
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 290);
		this.setContentPane(getJContentPane());
		jLabel.setText(exportDir.getAbsolutePath());
		
		jComboBox.setEditable(false);

		for (Type t : AudioSystem.getAudioFileTypes()) {	
			jComboBox.addItem(t);
		}
		
		
		this.setTitle("Playlist Exporter");
		pack();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BoxLayout(getJContentPane(), BoxLayout.Y_AXIS));
			jContentPane.add(getJPanel(), null);
			jContentPane.add(getJPanel1(), null);
			jContentPane.add(getJPanel2(), null);
			jContentPane.add(getJButton1(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new FlowLayout());
			jPanel.setBorder(BorderFactory.createTitledBorder(null, "Output Sound File Format", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			jPanel.add(getJComboBox(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
		}
		return jComboBox;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel = new JLabel();
			jLabel.setText("JLabel");
			jPanel1 = new JPanel();
			jPanel1.setLayout(new FlowLayout());
			jPanel1.setBorder(BorderFactory.createTitledBorder(null, "Output Directory", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			jPanel1.add(jLabel, null);
			jPanel1.add(getJButton(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Chose Different");
			jButton.setActionCommand("Chose Different...");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					JFileChooser fileChooser = new JFileChooser("Chose a directory to export to...");
					

					
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					
				    int returnVal = fileChooser.showSaveDialog(thisComp);
				    
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	
				    	File fileToSaveTo = fileChooser.getSelectedFile();
				    	
				    	if(fileToSaveTo == null){

				    		return;
				    	}
				    	
			    	   
				    	
//				    	try {
				    		

								if(fileToSaveTo.exists() || fileToSaveTo.mkdir()){
									jLabel.setText(fileToSaveTo.getAbsolutePath());
									pack();
									exportDir = fileToSaveTo;
								}
									


				
				
				
				
				    }}});
		}
		return jButton;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new FlowLayout());
			jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Include Language In File Names", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			jPanel2.add(getJCheckBox(), null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
		}
		return jCheckBox;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Start Export...");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					PlaylistFileExporter exporter = new PlaylistFileExporter();
					try {
						System.out.println("START EXPORT");
						exporter.export(words, parentComponent, (Type)jComboBox.getSelectedItem(), jCheckBox.isSelected(), exportDir);
						thisComp.setVisible(false);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return jButton1;
	}

}  //  @jve:decl-index=0:visual-constraint="14,29"
