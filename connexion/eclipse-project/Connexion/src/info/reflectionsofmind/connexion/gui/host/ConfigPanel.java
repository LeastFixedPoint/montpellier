package info.reflectionsofmind.connexion.gui.host;

import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ConfigPanel extends JPanel
{
	private final JTextField nameField;

	public ConfigPanel(final HostGameDialog serverUI) throws HeadlessException
	{
		setLayout(new MigLayout("ins 0 6 6 6", "[grow]", "[]6[]"));
		setBorder(BorderFactory.createTitledBorder("Game parameters"));

		add(new JLabel("Game name:"), "grow, span");
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
