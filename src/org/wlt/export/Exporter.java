package org.wlt.export;

import java.awt.Component;
import java.awt.Frame;
import java.util.List;

import org.wlt.data.Word;

public interface Exporter {

	public void export(List<Word> words, Component frame);
	
}
