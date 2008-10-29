package org.wlt.export;

import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.wlt.data.Word;

public class HTMLFileExporter implements Exporter {

	private File fileToSaveTo;

	private Component parentComponent;

	private List<Word> words;

	public void export(List<Word> words, Component frame) {
		parentComponent = frame;
		this.words = words;

		try {

			if (!fetchFile())
				return;
			System.out.println("FILE FETCHED");
			saveWordDataToFile();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(parentComponent,
					"Error when saving file: " + e.getMessage());

			e.printStackTrace();
		}

	}

	private void saveWordDataToFile() throws Exception {
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(fileToSaveTo));

		StringBuffer strBuf = new StringBuffer();

		String languageA = words.size() > 0 ? words.get(0).getLanguage() : "";

		String languageB = words.size() > 1 ? words.get(1).getLanguage() : "";

		strBuf.append("<HTML>\n");
		strBuf.append("<HEAD><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"</HEAD>\n");
		strBuf.append("<BODY>\n");
		strBuf.append("<TABLE  border=\"1\">\n");
		strBuf.append("<THEAD>\n");
		strBuf.append("<TR> \n");
		strBuf.append("<TD>\n");
		strBuf.append("<b>" + languageA + "</b>");
		strBuf.append("</TD>\n");
		strBuf.append("<TD>\n");
		strBuf.append("<b>" + languageB + "</b>");
		strBuf.append("</TD>\n");
		strBuf.append("</THEAD>\n");

		int n = 0;
		while ((n + 2) < words.size()) {
			strBuf.append("<TR>\n");
			String word1 = words.get(n).getWord();
			String word2 = words.get(n + 1).getWord();
			strBuf.append("<TD>\n");
			strBuf.append(word1);
			strBuf.append("</TD>\n");
			strBuf.append("<TD>\n");
			strBuf.append(word2);
			strBuf.append("</TD>\n");

			n = n + 2;
		}
		strBuf.append("</TABLE>\n");

		strBuf.append("</BODY>\n");
		strBuf.append("</HTML>\n");

		out.write(strBuf.toString().getBytes("UTF-16"));

		out.close();
	}

	private boolean fetchFile() {

		JFileChooser fileChooser = new JFileChooser(
				"Chose a file to export to...");

//		FileFilter fileFilter = new FileFilter() {
//
//			public boolean accept(File f) {
//
//				if (f.isDirectory())
//					return true;
//
//				if (f.getName().endsWith(".html")
//						|| f.getName().endsWith(".htm")
//						|| f.getName().endsWith(".HTM")
//						|| f.getName().endsWith(".HTML")
//						|| f.isDirectory())
//					return true;
//
//				return false;
//
//			}
//
//			@Override
//			public String getDescription() {
//				// TODO Auto-generated method stub
//				return "HTML Files";
//			}
//
//		};

//		fileChooser.addChoosableFileFilter(fileFilter);

		fileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);

		int returnVal = fileChooser.showSaveDialog(parentComponent);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			fileToSaveTo = fileChooser.getSelectedFile();

			if (fileToSaveTo == null || fileToSaveTo.isDirectory()) {
				JOptionPane.showMessageDialog(parentComponent,
						"The file has to be an ordinary file.");
				return false;
			}

			if (!(fileToSaveTo.getName().endsWith(".html")
					|| fileToSaveTo.getName().endsWith(".HTML")
					|| fileToSaveTo.getName().endsWith(".HTM") || fileToSaveTo
					.getName().endsWith(".htm")))
				fileToSaveTo = new File(fileToSaveTo.getParentFile(),
						fileToSaveTo.getName() + ".html");

			try {
				fileToSaveTo.createNewFile();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		} else
			return false;
	}

}
