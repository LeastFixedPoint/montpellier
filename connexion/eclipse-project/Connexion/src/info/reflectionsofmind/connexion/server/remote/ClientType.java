package info.reflectionsofmind.connexion.server.remote;

import info.reflectionsofmind.connexion.server.local.IServer;
import info.reflectionsofmind.connexion.server.remote.connector.BotConnector;
import info.reflectionsofmind.connexion.server.remote.connector.HotSeatConnector;
import info.reflectionsofmind.connexion.server.remote.connector.IClientConnector;
import info.reflectionsofmind.connexion.server.remote.connector.JabberConnector;

public enum ClientType
{
	NONE
	{
		@Override
		public IClientConnector getConnector(final IServer server)
		{
			throw new UnsupportedOperationException("Client type not selected.");
		}
	},

	LOCAL
	{
		@Override
		public IClientConnector getConnector(final IServer server)
		{
			return new HotSeatConnector(server);
		}
	},

	BOT
	{
		@Override
		public IClientConnector getConnector(final IServer server)
		{
			return new BotConnector(server);
		}
	},

	JABBER
	{
		@Override
		public IClientConnector getConnector(final IServer server)
		{
			return new JabberConnector(server);
		}
	};

	public abstract IClientConnector getConnector(final IServer server);
}
