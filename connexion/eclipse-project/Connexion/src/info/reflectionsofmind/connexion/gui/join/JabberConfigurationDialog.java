package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class JabberConfigurationDialog extends JDialog
{
	private final JTextField nameField;
	private final JTextField serverField;

	private String playerName;
	private JabberAddress server;
	
	public JabberConfigurationDialog(final JoinGameDialog owner)
	{
		super(owner, "Connexion :: Join game :: Jabber", true);

		setLayout(new MigLayout("", "[60]6[360]", "[]6[]6[]"));

		add(new JLabel("Name"), "grow");
		this.nameField = new JTextField("Shooshpanchick");
		add(this.nameField, "grow, span");

		add(new JLabel("Server"), "grow");
		this.serverField = new JTextField("shooshpanchick@gmail.com/connexion-server");
		add(this.serverField, "grow, span");

		add(new JButton(new AbstractAction("Join game")
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				joinGame();
			}
		}), "span, split, center");
		add(new JButton(new AbstractAction("Cancel")
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				cancel();
			}
		}), "");

		pack();
		setLocationRelativeTo(owner);
	}

	private void joinGame()
	{
		this.playerName = this.nameField.getText();
		this.server = new JabberAddress(this.serverField.getText());
		
		dispose();
	}
	
	public String getPlayerName()
	{
		return this.playerName;
	}

	private void cancel()
	{
		dispose();
	}
	
	public JabberAddress getServer()
	{
		return this.server;
	}
}
