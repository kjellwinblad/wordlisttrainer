/**
 * 
 */
package org.wlt.export;

import java.awt.Component;
import java.util.List;

import org.wlt.data.Word;
import org.wlt.data.WordList;

/**
 * Classes that uses this interface can export entire WordLists
 * 
 * @author kjellw
 *
 */
public interface WordListExporter {
	public void export(WordList words, Component frame);
}
