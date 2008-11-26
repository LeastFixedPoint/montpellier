package info.reflectionsofmind.connexion.local.client.gui.join;

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
	private final JTextField serverField;
	private final JTextField clientField;

	private JabberAddress server;
	private JabberAddress client;
	
	public JabberConfigurationDialog(final JoinGameFrame owner)
	{
		super(owner, "Connexion :: Join game :: Jabber", true);

		setLayout(new MigLayout("", "[60]6[360]", "[]6[]6[]"));

		add(new JLabel("Server"), "grow");
		this.serverField = new JTextField("shooshpanchick@gmail.com/connexion-server");
		add(this.serverField, "grow, span");

		add(new JLabel("Client"), "grow");
		this.clientField = new JTextField("connexion:connexion@binaryfreedom.info:5222/connexion-client");
		add(this.clientField, "grow, span");

		add(new JButton(new AbstractAction("Join game")
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				joinGame();
			}
		}), "span, split, center");
		add(new JButton(new AbstractAction("Join game")
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
		this.server = new JabberAddress(this.serverField.getText());
		this.client = new JabberAddress(this.clientField.getText());
		
		dispose();
	}

	private void cancel()
	{
		dispose();
	}

	public JabberAddress getServer()
	{
		return this.server;
	}

	public void setServer(JabberAddress server)
	{
		this.server = server;
	}

	public JabberAddress getClient()
	{
		return this.client;
	}

	public void setClient(JabberAddress client)
	{
		this.client = client;
	}
}
