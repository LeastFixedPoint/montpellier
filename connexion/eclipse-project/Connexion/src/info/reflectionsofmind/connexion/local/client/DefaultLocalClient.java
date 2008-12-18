package info.reflectionsofmind.connexion.local.client;

import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ChatMessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_DisconnectNoticeEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.event.stc.IServerToClientEventListener;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEventDecoder;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ChatMessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ClientStateChangedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.local.Settings;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class DefaultLocalClient implements ILocalClient, ITransport.IListener, IServerToClientEventListener
{
	// Basic fields

	private final Settings settings;
	private final List<IListener> listeners = new ArrayList<IListener>();
	private final List<ITransport> transports = new ArrayList<ITransport>();
	private final ITileSource tileSource;

	// Connected-state fields

	private final List<Client> clients = new ArrayList<Client>();
	private INode serverNode;
	private Client client;

	// Game-state fields

	private RemoteTileSequence sequence;
	private Game game;

	public DefaultLocalClient(final Settings settings)
	{
		this.settings = settings;
		this.transports.add(new JabberTransport(settings.getJabberAddress()));

		try
		{
			this.tileSource = new DefaultTileSource(getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/tilelist/DefaultTileList.properties"));
		}
		catch (IOException exception)
		{
			throw new RuntimeException(exception);
		}
		catch (TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	// ============================================================================================
	// === COMMANDS
	// ============================================================================================

	private void send(ClientToServerEvent event)
	{
		try
		{
			getServerNode().send(event.encode());
		}
		catch (TransportException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	public void connect(final INode serverNode)
	{
		try
		{
			serverNode.getTransport().start();
		}
		catch (TransportException exception)
		{
			throw new RuntimeException();
		}
		
		this.serverNode = serverNode;
		getServerNode().getTransport().addListener(this);
		send(new ClientToServer_ClientConnectionRequestEvent(getSettings().getClientName()));
	}

	public void disconnect(DisconnectReason reason)
	{
		send(new ClientToServer_DisconnectNoticeEvent(reason));
		
		try
		{
			this.serverNode.getTransport().stop();
		}
		catch (TransportException exception)
		{
			exception.printStackTrace();
		}
		
		this.serverNode = null;
		this.client = null;
		this.clients.clear();
		this.game = null;

		for (IListener listener : this.listeners)
		{
			listener.onConnectionBroken(reason);
		}
		
	}

	@Override
	public void sendChatMessage(String message)
	{
		send(new ClientToServer_ChatMessageEvent(message));

		for (IListener listener : this.listeners)
		{
			listener.onChatMessage(getClient(), message);
		}
	}

	@Override
	public void sendLastTurn(Game game)
	{
		send(new ClientToServer_TurnEvent(getGame().getTurns().get(getGame().getTurns().size() - 1)));
	}

	// ============================================================================================
	// === EVENT HANDLERS
	// ============================================================================================

	@Override
	public void onTransportMessage(final INode from, final String message)
	{
		ServerToClientEventDecoder.decode(message).dispatch(this);
	}

	@Override
	public void onConnectionAccepted(final ServerToClient_ConnectionAcceptedEvent event)
	{
		final Iterator<String> names = event.getExistingPlayers().iterator();
		final Iterator<State> states = event.getStates().iterator();

		while (names.hasNext() && states.hasNext())
		{
			this.clients.add(new Client(names.next(), states.next()));
		}

		for (final IListener listener : this.listeners)
		{
			listener.onConnectionAccepted();
		}
	}

	@Override
	public void onClientConnected(final ServerToClient_ClientConnectedEvent event)
	{
		final Client newClient = new Client(event.getClientName());
		this.clients.add(newClient);

		for (final IListener listener : this.listeners)
		{
			listener.onClientConnected(newClient);
		}
	}

	@Override
	public void onClientStateChanged(final ServerToClient_ClientStateChangedEvent event)
	{
		final Client client = this.clients.get(event.getClientIndex());
		client.setState(event.getNewState());
	}

	@Override
	public void onClientDisconnected(final ServerToClient_ClientDisconnectedEvent event)
	{
		final Client client = this.clients.get(event.getClientIndex());
		this.clients.remove(client);

		for (final IListener listener : this.listeners)
		{
			listener.onClientDisconnected(client);
		}
	}

	@Override
	public void onChatMessage(final ServerToClient_ChatMessageEvent event)
	{
		final Client client = this.clients.get(event.getClientIndex());

		for (final IListener listener : this.listeners)
		{
			listener.onChatMessage(client, event.getMessage());
		}
	}

	@Override
	public void onStart(final ServerToClient_GameStartedEvent event)
	{
		this.sequence = new RemoteTileSequence(event.getTotalNumberOfTiles());
		
		try
		{
			this.sequence.setCurrentTile(new Tile(event.getCurrentTileCode()));
		}
		catch (TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}
		
		final List<Player> players = new ArrayList<Player>();
		
		for (Client client : getClients())
		{
			if (client.getState() == Client.State.ACCEPTED)
			{
				players.add(new Player(client.getName()));
			}
		}
		
		this.game = new Game(this.sequence, players);
	}

	@Override
	public void onTurn(final ServerToClient_TurnEvent event)
	{
		if (event.getTurn() != null)
		{
			try
			{
				getGame().doTurn(event.getTurn());
			}
			catch (GameTurnException exception)
			{
				throw new RuntimeException(exception);
			}
		}
		
		try
		{
			this.sequence.setCurrentTile(new Tile(event.getCurrentTileCode()));
		}
		catch (TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	@Override
	public String getName()
	{
		return this.settings.getClientName();
	}

	@Override
	public Game getGame()
	{
		return this.game;
	}

	@Override
	public ITileSource getTileSource()
	{
		return this.tileSource;
	}

	@Override
	public INode getServerNode()
	{
		return this.serverNode;
	}

	@Override
	public List<Client> getClients()
	{
		return this.clients;
	}

	@Override
	public Client getClient()
	{
		return this.client;
	}

	@Override
	public List<ITransport> getTransports()
	{
		return ImmutableList.copyOf(this.transports);
	}

	@Override
	public Settings getSettings()
	{
		return this.settings;
	}
	
	// ============================================================================================
	// === TILE SEQUENCE PROXY
	// ============================================================================================

	private final class RemoteTileSequence implements ITileSequence
	{
		private Tile currentTile = null;
		private boolean hasMoreTiles = true;
		private final Integer totalNumberOfTiles;

		public RemoteTileSequence(Integer totalNumberOfTiles)
		{
			this.totalNumberOfTiles = totalNumberOfTiles;
		}

		public void setCurrentTile(final Tile tile)
		{
			this.currentTile = tile;
		}

		public void setNoMoreTiles()
		{
			this.hasMoreTiles = false;
		}

		@Override
		public Tile getCurrentTile()
		{
			return this.currentTile;
		}

		@Override
		public void nextTile()
		{
			this.currentTile = null;
		}

		@Override
		public boolean hasMoreTiles()
		{
			return this.hasMoreTiles;
		}

		@Override
		public Integer getTotalNumberOfTiles()
		{
			return this.totalNumberOfTiles;
		}
	}

	// ====================================================================================================
	// === SELF-LISTENERS
	// ====================================================================================================

	@Override
	public void addListener(final IListener listener)
	{
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(final IListener listener)
	{
		this.listeners.remove(listener);
	}
}
