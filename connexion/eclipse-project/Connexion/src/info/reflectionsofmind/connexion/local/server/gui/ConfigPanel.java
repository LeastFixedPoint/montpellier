package info.reflectionsofmind.connexion.local.server.gui;

import java.awt.HeadlessException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ConfigPanel extends JPanel
{
	private final JTextField nameField;

	public ConfigPanel(final HostGameWindow serverUI) throws HeadlessException
	{
		setLayout(new MigLayout("ins 0", "[240]", "[]6[]"));

		add(new JLabel("Game name:"), "span");
		this.nameField = new JTextField("Test game");
		add(this.nameField, "grow, span");
	}

	public String getGameName()
	{
		return this.nameField.getText();
	}

	@Override
	public void setEnabled(final boolean enabled)
	{
		this.nameField.setEditable(enabled);
	}
}
