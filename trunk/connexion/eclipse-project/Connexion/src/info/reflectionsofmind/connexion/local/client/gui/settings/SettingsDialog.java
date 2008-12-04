package info.reflectionsofmind.connexion.local.client.gui.settings;

import info.reflectionsofmind.connexion.MainWindow;
import info.reflectionsofmind.connexion.local.client.Settings;
import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class SettingsDialog extends JDialog
{
	private final Settings settings;

	private final JTextField playerNameField;
	private final JTextField jabberAddressField;

	public SettingsDialog(final MainWindow parent)
	{
		super(parent, "Connexion :: Settings", true);

		this.settings = parent.getApplication().getSettings();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout("", "[480, center]", "[][]6[][]6[]"));

		this.playerNameField = new JTextField(settings.getPlayerName());
		add(new JLabel("Player name"), "grow, span");
		add(this.playerNameField, "grow, span");

		this.jabberAddressField = new JTextField(settings.getJabberAddress().asString());
		add(new JLabel("Jabber address"), "grow, span");
		add(this.jabberAddressField, "grow, span");

		add(new JButton(new SaveAction("Save")), "split");
		add(new JButton(new CancelAction("Cancel")), "wrap");

		pack();
		setLocationRelativeTo(parent);
	}

	public class SaveAction extends AbstractAction
	{
		public SaveAction(final String name)
		{
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			SettingsDialog.this.settings.setJabberAddress(new JabberAddress(SettingsDialog.this.jabberAddressField.getText()));
			SettingsDialog.this.settings.setPlayerName(SettingsDialog.this.playerNameField.getText());

			dispose();
		}
	}

	public class CancelAction extends AbstractAction
	{
		public CancelAction(final String name)
		{
			super(name);
		}

		@Override
		public void actionPerformed(final ActionEvent event)
		{
			dispose();
		}
	}
}
