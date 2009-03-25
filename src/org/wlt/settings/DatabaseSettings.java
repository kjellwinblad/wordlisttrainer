package org.wlt.settings;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DatabaseSettings {
	
	public static final String PROPERTIES_DIR = ".wordlisttrainer";
	
	
	//If true then a local database is used otherwise a network database is used
	private boolean currentDatabaseMode = true;
	
	private NetworkDatabaseSettings networkDatabaseSettings = new NetworkDatabaseSettings();
	
	private File localDatabaseLocation = new File(System.getProperty("user.home")
			+ File.separator + PROPERTIES_DIR + File.separator
			+ "LocalDB");

	
	private static DatabaseSettings instance;
	
	public static DatabaseSettings getInstance() {

		if (instance == null) {
			if (!createInstanceFromFile())
				instance = new DatabaseSettings();
		}

		return instance;
	}

	private static boolean createInstanceFromFile() {

		XStream xstream = new XStream(new DomDriver());

		File propertiesFile = new File(System.getProperty("user.home")
				+ File.separator + PROPERTIES_DIR + File.separator
				+ "properties.xml");

		if (!propertiesFile.exists()) {
			// There is no properties file.
			return false;
		}

		try {
			FileReader reader = new FileReader(propertiesFile);

			instance = (DatabaseSettings) xstream.fromXML(reader);
			reader.close();
		} catch (Exception e) {
			System.err
					.println("Not possible to read properties file, because of the following reason:"
							+ e.getMessage());
			return false;
		}

		return true;

	}

	
	public boolean saveToPropertiesFile() {

		XStream xstream = new XStream(new DomDriver());
		File propertiesDir = new File(System.getProperty("user.home")
				+ File.separator + PROPERTIES_DIR + File.separator);
		File propertiesFile = new File(System.getProperty("user.home")
				+ File.separator + PROPERTIES_DIR + File.separator
				+ "properties.xml");
		if (!propertiesDir.exists()) {
			if (!propertiesDir.mkdir())
				return false;
		}

		if (!propertiesFile.exists()) {
			// There is no properties file.
			try {
				propertiesFile.createNewFile();
			} catch (IOException e) {
				// TODO
				e.printStackTrace();
				return false;
			}
		}

		try {
			FileWriter writer = new FileWriter(propertiesFile);

			xstream.toXML(getInstance(), writer);
			writer.close();
		} catch (Exception e) {
			System.err
					.println("Not possible to write properties file, because of the followin reason:"
							+ e.getMessage());
			return false;
		}

		return true;
	}

	public void setCurrentDatabaseModeLocal(boolean currentDatabaseMode) {
		this.currentDatabaseMode = currentDatabaseMode;
		saveToPropertiesFile();
	}

	public NetworkDatabaseSettings getNetworkDatabaseSettings() {
		return networkDatabaseSettings;
	}

	public boolean isCurrentDatabaseModeLocal() {
		// TODO Auto-generated method stub
		return currentDatabaseMode;
	}

	public File getLocalDatabaseLocation() {
		return localDatabaseLocation;
	}

	public void setLocalDatabaseLocation(File localDatabaseLocation) {
		this.localDatabaseLocation = localDatabaseLocation;
	}

}
