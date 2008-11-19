package info.reflectionsofmind.connexion.client;

import javax.swing.JOptionPane;

import info.reflectionsofmind.connexion.client.local.LocalClient;
import info.reflectionsofmind.connexion.server.IServer;

public enum ClientType
{
	NONE
	{
		@Override
		public IClient connect(IServer arg0) throws ConnectionFailedException
		{
			throw new ConnectionFailedException("Client type not selected.");
		}
	},
	
	LOCAL
	{
		@Override
		public IClient connect(IServer server) throws ConnectionFailedException
		{
			final String name = JOptionPane.showInputDialog("Enter name:", "Client #" + hashCode());
			
			if (name == null) throw new ConnectionFailedException("Client cancelled.");
			
			return new LocalClient(server, name);
		}
	},

	BOT
	{
		@Override
		public IClient connect(IServer server) throws ConnectionFailedException
		{
			throw new ConnectionFailedException("AI not implemented yet.");
		}
	},

	SPECTATOR
	{
		@Override
		public IClient connect(IServer server) throws ConnectionFailedException
		{
			throw new ConnectionFailedException("Spectation not implemented yet.");
		}
	},

	JABBER
	{
		@Override
		public IClient connect(IServer server) throws ConnectionFailedException
		{
			throw new ConnectionFailedException("Game over XMPP not implemented yet.");
		}
	};

	public abstract IClient connect(IServer server) throws ConnectionFailedException;
}
