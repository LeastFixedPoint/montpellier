package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.server.IRemoteClient;
import info.reflectionsofmind.connexion.server.IServer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ClientsPanel extends JPanel implements IServer.IListener
{
	private final HostGameFrame window;
	private final List<ClientPanel> panels = new ArrayList<ClientPanel>();

	public ClientsPanel(final HostGameFrame window)
	{
		this.window = window;
		this.window.getServer().addListener(this);

		setBorder(BorderFactory.createTitledBorder("Clients"));
		setLayout(new MigLayout("ins 0", "[]", "[]"));

		getLayout().layoutContainer(this);
	}

	@Override
	public void onClientConnected(final IRemoteClient client)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				final ClientPanel panel = new ClientPanel(ClientsPanel.this, client);
				ClientsPanel.this.panels.add(panel);
				ClientsPanel.this.add(panel, "grow, span");
			}
		});
	}

	@Override
	public void onClientMessage(final IRemoteClient client, final String message)
	{
		// Does not react to messages
	}

	@Override
	public void onClientDisconnected(final IRemoteClient client)
	{
		final ClientPanel clientPanel = Iterables.find(this.panels, new Predicate<ClientPanel>()
		{
			@Override
			public boolean apply(final ClientPanel panel)
			{
				return panel.getClient() == client;
			}
		});

		this.panels.remove(clientPanel);
		remove(clientPanel);
	}

	public void removePanel(final ClientPanel panel)
	{
		if (this.panels.size() > 1)
		{
			remove(panel);
			this.panels.remove(panel);
			getLayout().layoutContainer(this);
		}
	}

	public HostGameFrame getHostGameDialog()
	{
		return this.window;
	}

	@Override
	public void setEnabled(final boolean enabled)
	{
		for (final ClientPanel panel : this.panels)
		{
			panel.setEnabled(enabled);
		}
	}
}