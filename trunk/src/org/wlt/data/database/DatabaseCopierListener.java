package org.wlt.data.database;

public interface DatabaseCopierListener {
	
	public void progressUpdate(double progress);

	public void messageUpdate(String message);
	
	public void ready();
	
}
