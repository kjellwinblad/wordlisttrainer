package org.wlt.gui.dbconf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.wlt.data.database.DatabaseHelper;
import org.wlt.gui.WordListTrainer;
import org.wlt.settings.DatabaseSettings;
import org.wlt.settings.NetworkDatabaseSettings;

import com.jeta.forms.components.panel.FormPanel;

public class DatabaseSettingsDialog extends JDialog {

	private JPanel saveOptionPanel;
	private FormPanel propertiesPanel;

	DatabaseSettings databaseSettings = DatabaseSettings.getInstance();
	private JCheckBox useNetworkDatabaseCheckbox;
	private JTextField hostTextField;
	private JTextField portTextField;
	private JTextField databaseNameTextField;
	private JTextField userNameTextField;
	private JTextField passwordTextField;
	private WordListTrainer owner;

	public DatabaseSettingsDialog(WordListTrainer owner) {
		super(owner);
		this.owner = owner;
		owner.setEnabled(false);
		setTitle("Database Settings");
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		add(getPropertiesPanel(), BorderLayout.CENTER);
		add(getSaveOptionPanel(), BorderLayout.SOUTH);

		pack();
	}

	private JPanel getSaveOptionPanel() {

		if (saveOptionPanel == null) {
			saveOptionPanel = new JPanel();
			saveOptionPanel.setLayout(new FlowLayout());

			JButton tryConnectButton = new JButton("Try to connect...");
			tryConnectButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tryConnect();

				}

			});

			saveOptionPanel.add(tryConnectButton);

			JButton connectAndCloseButton = new JButton("Connect and Close");
			connectAndCloseButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					connectAndClose();
					
				}
				
			});
			saveOptionPanel.add(connectAndCloseButton);
		}
		return saveOptionPanel;
	}

	private void connectAndClose() {
		tryConnect();
		owner.setEnabled(true);
		owner.update();
		setVisible(false);		
	}

	private void tryConnect() {
		try {

			DatabaseSettings dbs = getSettingsFromGUI();
			if(dbs==null)
				return;
			
			DatabaseHelper.setDatabaseSettings(dbs);

			DatabaseHelper.createConnection();
			System.out.println("CONN SUCCESFUL");
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"Could not connect", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	private DatabaseSettings getSettingsFromGUI() {
		DatabaseSettings dbs = DatabaseSettings.getInstance();
		dbs.setCurrentDatabaseModeLocal(!useNetworkDatabaseCheckbox
				.isSelected());
		NetworkDatabaseSettings nds = dbs.getNetworkDatabaseSettings();
		nds.setHost(hostTextField.getText());
		try {
			long port = new Long(portTextField.getText());
			nds.setPortNumber(port);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"The port number has to be a number");
			return null;
		}
		nds.setDatabaseName(databaseNameTextField.getText());
		nds.setUserName(userNameTextField.getText());
		nds.setPassword(passwordTextField.getText());

		return dbs;
	}

	private FormPanel getPropertiesPanel() {
		if (propertiesPanel == null) {
			propertiesPanel = new FormPanel(
					"org/wlt/gui/dbconf/network_database_config.jfrm");

			useNetworkDatabaseCheckbox = propertiesPanel
					.getCheckBox("useNetworkDatabase");

			useNetworkDatabaseCheckbox.setSelected(!databaseSettings
					.isCurrentDatabaseModeLocal());
			useNetworkDatabaseCheckbox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setNetworkDatabaseMode(useNetworkDatabaseCheckbox
							.isSelected());
				}
			});


			hostTextField = propertiesPanel.getTextField("host");
			hostTextField.setText(databaseSettings.getNetworkDatabaseSettings()
					.getHost());

			portTextField = propertiesPanel.getTextField("port");
			portTextField.setText(new Long(databaseSettings
					.getNetworkDatabaseSettings().getPortNumber()).toString());

			databaseNameTextField = propertiesPanel
					.getTextField("databaseName");
			databaseNameTextField.setText(databaseSettings
					.getNetworkDatabaseSettings().getDatabaseName());

			userNameTextField = propertiesPanel.getTextField("userName");
			userNameTextField.setText(databaseSettings
					.getNetworkDatabaseSettings().getUserName());

			passwordTextField = propertiesPanel.getTextField("password");
			passwordTextField.setText(databaseSettings
					.getNetworkDatabaseSettings().getPassword());
			
			setNetworkDatabaseMode(useNetworkDatabaseCheckbox
					.isSelected());

		}
		return propertiesPanel;
	}

	private void setNetworkDatabaseMode(boolean enabled) {
		
		hostTextField.setEnabled(enabled);
		portTextField.setEnabled(enabled);
		databaseNameTextField.setEnabled(enabled);
		userNameTextField.setEnabled(enabled);
		passwordTextField.setEnabled(enabled);
		
	}
}
