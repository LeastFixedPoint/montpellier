package info.reflectionsofmind.connexion.transport.hotseat;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.GameUtil;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.event.cts.ClientToServerDecoder;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_MessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClientDecoder;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.local.client.DefaultLocalClient;
import info.reflectionsofmind.connexion.local.client.IClient;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.exception.ClientConnectionException;
import info.reflectionsofmind.connexion.remote.client.AbstractRemoteClient;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient.IListener;
import info.reflectionsofmind.connexion.remote.server.AbstractRemoteServer;
import info.reflectionsofmind.connexion.remote.server.RemoteServerException;
import info.reflectionsofmind.connexion.remote.server.ServerConnectionException;
import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.IAddressee;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class HotSeatTransport extends AbstractTransport<HotSeatTransport.Addressee>
{
	private final RemoteServer remoteServer;
	private final List<RemoteClient> remoteClients = new ArrayList<RemoteClient>();;

	@Override
	protected void doStart() throws TransportException
	{
		final RemoteClient remoteClient = new RemoteClient(new DefaultLocalClient("Local client"));
		this.remoteClients.add(remoteClient);
		
		this.remoteServer = new RemoteServer();
	}

	@Override
	protected void doStop() throws TransportException
	{

	}

	@Override
	public void send(final Addressee to, final String message) throws TransportException
	{
		if (to instanceof RemoteClient)
		{
			to.accept(to, message);
		}
		
	}

	@Override
	public String getName()
	{
		return "Local";
	}

	private class RemoteClient extends AbstractRemoteClient implements Addressee
	{
		private final IClient client;

		public RemoteClient(final IClient client)
		{
			super(HotSeatTransport.this, HotSeatTransport.this.remoteServer.getServer())
			this.client = client;
		}

		public IClient getClient()
		{
			return this.client;
		}

		@Override
		public void accept(final Addressee from, final String message)
		{
			ServerToClientDecoder.decode(message).dispatch(this.client);
		}

		@Override
		public String getName()
		{
			return this.client.getName();
		}
	}

	private class RemoteServer extends AbstractRemoteServer implements Addressee
	{
		private final IServer server;

		public RemoteServer(final IServer server)
		{
			this.server = server;
		}

		@Override
		public void accept(final Addressee from, final String message)
		{
			ClientToServerDecoder.decode(message).dispatch((RemoteClient) from, this.server);
		}

		@Override
		public synchronized void sendTurn(final Turn turn) throws RemoteServerException
		{
			this.server.onTurn((IRemoteClient) HotSeatTransport.this.me, new ClientToServer_TurnEvent(turn));
		}

		@Override
		public void sendMessage(final String message) throws ServerConnectionException, RemoteServerException
		{
			this.server.onMessage((IRemoteClient) HotSeatTransport.this.me, new ClientToServer_MessageEvent(message));
		}

		@Override
		public synchronized void connect(final IClient client) throws ServerConnectionException, RemoteServerException
		{

		}
	}

	public interface Addressee extends IAddressee
	{
		void accept(Addressee from, String message);
	}
}
