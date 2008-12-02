package info.reflectionsofmind.connexion.local.server.gui;

import java.awt.HeadlessException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ConfigPanel extends JPanel
{
	private JTextField nameField;

	public ConfigPanel(HostGameWindow serverUI) throws HeadlessException
	{
		setLayout(new MigLayout("ins 0", "[240]", "[]6[]"));
		
		add(new JLabel("Game name:"), "span");
		nameField = new JTextField("Test game");
		add(nameField, "grow, span");
	}
	
	public String getGameName()
	{
		return this.nameField.getText();
	}
	
	public void fade()
	{
		this.nameField.setEditable(false);
	}
}
