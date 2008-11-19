package info.reflectionsofmind.connexion.server;

import java.awt.HeadlessException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ConfigPanel extends JPanel
{
	private final ServerUI serverUI;
	private JTextField nameField;

	public ConfigPanel(ServerUI serverUI) throws HeadlessException
	{
		this.serverUI = serverUI;
		
		setLayout(new MigLayout("ins 0", "[240]", "[]6[]"));
		
		add(new JLabel("Game name:"), "span");
		nameField = new JTextField("Test game");
		add(nameField, "grow, span");
	}
	
	public String getGameName()
	{
		return this.nameField.getText();
	}
	
	public void disable()
	{
		this.nameField.setEnabled(false);
	}
}
