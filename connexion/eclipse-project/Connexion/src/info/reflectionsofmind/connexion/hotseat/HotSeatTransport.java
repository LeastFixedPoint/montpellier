package info.reflectionsofmind.connexion.hotseat;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.GameUtil;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_MessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent.Reason;
import info.reflectionsofmind.connexion.local.client.IClient;
import info.reflectionsofmind.connexion.local.server.ServerSideDisconnectReason;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.exception.ClientConnectionException;
import info.reflectionsofmind.connexion.remote.client.AbstractRemoteClient;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.server.AbstractRemoteServer;
import info.reflectionsofmind.connexion.remote.server.IRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServerException;
import info.reflectionsofmind.connexion.remote.server.ServerConnectionException;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class HotSeatTransport
{
	private IRemoteClient remoteClient;
	private IRemoteServer remoteServer;

	public void setServer(IServer server)
	{
		this.remoteServer = new RemoteServer(server);
	}

	public void setClient(IClient client)
	{
		this.remoteClient = new RemoteClient(client);
	}

	public IRemoteClient getRemoteClient()
	{
		if (this.remoteClient == null) throw new RuntimeException("Client is not set yet.");

		return this.remoteClient;
	}

	public IRemoteServer getRemoteServer()
	{
		if (this.remoteServer == null) throw new RuntimeException("Server is not set yet.");

		return this.remoteServer;
	}

	private class RemoteClient extends AbstractRemoteClient
	{
		private final IClient client;

		public RemoteClient(IClient client)
		{
			this.client = client;
		}

		@Override
		public synchronized void acceptConnection(List<Player> existingPlayers)
		{
			final List<String> playerNames = Lists.transform(existingPlayers, new Function<Player, String>()
			{
				@Override
				public String apply(Player player)
				{
					return player.getName();
				}
			});

			client.onConnectionAccepted(new ServerToClient_ConnectionAcceptedEvent(playerNames));
		}
		
		@Override
		public void sendPlayerConnected(Player player)
		{
			client.onPlayerConnect(new ServerToClient_PlayerConnectedEvent(player.getName()));
		}
		
		@Override
		public void sendPlayerDisconnected(int playerIndex, ClientSideDisconnectReason reason)
		{
			client.onPlayerDisconnect(new ServerToClient_PlayerDisconnectedEvent(playerIndex, reason));
		}

		@Override
		public synchronized void sendStart(Game game) throws ClientConnectionException
		{
			final ServerToClient_GameStartedEvent event = new ServerToClient_GameStartedEvent( //
					game.getName(), GameUtil.getInitialTile(game).getCode(), // 
					game.getCurrentTile().getCode(), //
					game.getSequence().getTotalNumberOfTiles());

			client.onStart(event);
		}

		@Override
		public synchronized void sendTurn(Turn turn, Tile nextCurrentTile) throws ClientConnectionException
		{
			final ServerToClient_TurnEvent event = new ServerToClient_TurnEvent(turn, nextCurrentTile.getCode());

			client.onTurn(event);
		}
		
		@Override
		public void sendMessage(int playerIndex, String message) throws ClientConnectionException
		{
			client.onMessage(new ServerToClient_MessageEvent(playerIndex, message));
		}

		@Override
		public synchronized void disconnect(ServerSideDisconnectReason reason) throws ClientConnectionException
		{
			throw new UnsupportedOperationException("Disconnects not supported yet");
		}

		@Override
		public String getName()
		{
			return client.getName();
		}
	}

	private class RemoteServer extends AbstractRemoteServer
	{
		private final IServer server;

		public RemoteServer(IServer server)
		{
			this.server = server;
		}

		@Override
		public synchronized void sendTurn(final Turn turn) throws RemoteServerException
		{
			this.server.onTurn(HotSeatTransport.this.remoteClient, new ClientToServer_TurnEvent(turn));
		}
		
		@Override
		public void sendMessage(String message) throws ServerConnectionException, RemoteServerException
		{
			this.server.onMessage(HotSeatTransport.this.remoteClient, new ClientToServer_MessageEvent(message));
		}

		@Override
		public synchronized void connect(IClient client) throws ServerConnectionException, RemoteServerException
		{

		}
	}
}
