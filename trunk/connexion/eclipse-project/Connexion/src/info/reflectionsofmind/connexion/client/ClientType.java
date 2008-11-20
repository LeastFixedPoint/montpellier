package info.reflectionsofmind.connexion.client;

import info.reflectionsofmind.connexion.client.local.DefaultGuiClient;
import info.reflectionsofmind.connexion.client.remote.HotSeatTransport;
import info.reflectionsofmind.connexion.server.local.IServer;
import info.reflectionsofmind.connexion.server.remote.IRemoteClient;

import javax.swing.JOptionPane;

public enum ClientType
{
	NONE
	{
		@Override
		public IRemoteClient connect(final IServer arg0) throws ConnectionFailedException
		{
			throw new ConnectionFailedException("Client type not selected.");
		}
	},

	LOCAL
	{
		@Override
		public IRemoteClient connect(final IServer server) throws ConnectionFailedException
		{
			final String name = JOptionPane.showInputDialog("Enter name:", "Client #" + localClientsNumber);
			localClientsNumber++;

			if (name == null) throw new ConnectionFailedException("Client cancelled.");

			final HotSeatTransport transport = new HotSeatTransport();
			transport.setServer(server);
			
			final DefaultGuiClient client = new DefaultGuiClient(transport.getRemoteServer(), name);
			transport.setClient(client);
			
			return transport.getRemoteClient();
		}
	},

	BOT
	{
		@Override
		public IRemoteClient connect(final IServer server) throws ConnectionFailedException
		{
			throw new ConnectionFailedException("AI not implemented yet.");
		}
	},

	SPECTATOR
	{
		@Override
		public IRemoteClient connect(final IServer server) throws ConnectionFailedException
		{
			throw new ConnectionFailedException("Spectation not implemented yet.");
		}
	},

	JABBER
	{
		@Override
		public IRemoteClient connect(final IServer server) throws ConnectionFailedException
		{
			throw new ConnectionFailedException("Game over XMPP not implemented yet.");
		}
	},

	ONLINE
	{
		@Override
		public IRemoteClient connect(final IServer arg0) throws ConnectionFailedException
		{
			throw new ConnectionFailedException("Online game not implemented yet.");
		}
	};

	private static int localClientsNumber = 1;

	public abstract IRemoteClient connect(IServer server) throws ConnectionFailedException;
}
