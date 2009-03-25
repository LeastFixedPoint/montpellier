package info.reflectionsofmind.connexion.platform.core.client;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.fortress.core.game.Game;
import info.reflectionsofmind.connexion.fortress.core.game.Player;
import info.reflectionsofmind.connexion.fortress.core.game.Turn;
import info.reflectionsofmind.connexion.fortress.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.fortress.core.game.sequence.ITileSequence;
import info.reflectionsofmind.connexion.fortress.core.tile.Tile;
import info.reflectionsofmind.connexion.fortress.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.common.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.cts.ClientToServer_ChatMessageEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.cts.ClientToServer_DisconnectNoticeEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.IServerToClientEventListener;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClientEventDecoder;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_ChatMessageEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_ClientConnectedEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_ClientStateChangedEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.platform.core.common.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.platform.core.transport.IClientTransport;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class DefaultClient implements IClient, IClientTransport.IListener, IServerToClientEventListener
{
	// Basic fields

	private final IApplication application;
	private final List<IListener> listeners = new ArrayList<IListener>();
	private final ITileSource tileSource;
	private String name = "Anonymous";

	// Connected-state fields

	private final List<Participant> participants = new ArrayList<Participant>();
	private IClientTransport transport;
	private Participant participant;

	// Game-state fields

	private Turn unconfirmedTurn;
	private RemoteTileSequence sequence;
	private Game game;

	public DefaultClient(IApplication application)
	{
		this.application = application;

		try
		{
			this.tileSource = new DefaultTileSource(getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/tilelist/DefaultTileList.properties"));
		}
		catch (final IOException exception)
		{
			throw new RuntimeException(exception);
		}
		catch (final TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	// ============================================================================================
	// === COMMANDS
	// ============================================================================================

	private void send(final ClientToServerEvent event)
	{
		try
		{
			getTransport().send(event.encode());
		}
		catch (final TransportException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	public void connect(final IClientTransport transport)
	{
		this.transport = transport;
		getTransport().addListener(this);
		send(new ClientToServer_ClientConnectionRequestEvent(getName()));
	}

	public void disconnect(final DisconnectReason reason)
	{
		send(new ClientToServer_DisconnectNoticeEvent(reason));

		this.transport = null;
		this.participant = null;
		this.participants.clear();
		this.game = null;

		for (final IListener listener : this.listeners)
		{
			listener.onAfterConnectionBroken(reason);
		}
	}

	@Override
	public void sendChatMessage(final String message)
	{
		send(new ClientToServer_ChatMessageEvent(message));

		for (final IListener listener : this.listeners)
		{
			listener.onChatMessage(getParticipant(), message);
		}
	}

	@Override
	public void sendTurn(Turn turn)
	{
		this.unconfirmedTurn = turn;
		send(new ClientToServer_TurnEvent(turn.getDirection(), turn.getLocation(), turn.getMeepleType(), turn.getSectionIndex()));
	}

	// ============================================================================================
	// === TRANSPORT LISTENERS
	// ============================================================================================

	@Override
	public void onPacket(String contents)
	{
		ServerToClientEventDecoder.decode(contents).dispatch(this);
	}

	@Override
	public void onError(TransportException exception)
	{
	}

	// ============================================================================================
	// === EVENT HANDLERS
	// ============================================================================================

	@Override
	public void onClientConnected(final ServerToClient_ClientConnectedEvent event)
	{
		final Participant newClient = new Participant(event.getClientName());
		this.participants.add(newClient);
		fireParticipantConnected(newClient);
	}

	@Override
	public void onClientStateChanged(final ServerToClient_ClientStateChangedEvent event)
	{
		final Participant client = this.participants.get(event.getClientIndex());
		client.setState(event.getNewState());
	}

	@Override
	public void onClientDisconnected(final ServerToClient_ClientDisconnectedEvent event)
	{
		final Participant client = this.participants.get(event.getClientIndex());

		if (client == getParticipant())
		{
			this.transport = null;
			this.participant = null;
			this.participants.clear();
			this.game = null;

			fireConnectionBroken(event);
		}
		else
		{
			this.participants.remove(client);

			fireParticipantDisconnected(client);
		}
	}

	@Override
	public void onChatMessage(final ServerToClient_ChatMessageEvent event)
	{
		final Participant client = event.getClientIndex() == null ? null : this.participants.get(event.getClientIndex());

		fireChatMessage(event, client);
	}

	@Override
	public void onStart(final ServerToClient_GameStartedEvent event)
	{
		this.sequence = new RemoteTileSequence(event.getTotalNumberOfTiles());

		try
		{
			this.sequence.setCurrentTile(new Tile(event.getCurrentTileCode()));
		}
		catch (final TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}

		final List<Player> players = new ArrayList<Player>();

		for (final Participant client : getParticipants())
		{
			if (client.getState() == Participant.State.ACCEPTED)
			{
				players.add(new Player(client.getName()));
			}
		}

		this.game = new Game(this.sequence, players);

		fireGameStarted();
	}

	@Override
	public void onTurn(final ServerToClient_TurnEvent event)
	{
		final Turn turn = new Turn();
		turn.addTilePlacement(event.getLocation(), event.getDirection());
		if (event.getMeepleType() != null) turn.addMeeplePlacement(event.getMeepleType(), event.getSectionIndex());

		if (turn.equals(this.unconfirmedTurn)) return;

		try
		{
			getGame().doTurn(turn);
		}
		catch (final GameTurnException exception)
		{
			throw new RuntimeException(exception);
		}

		try
		{
			this.sequence.setCurrentTile(new Tile(event.getCurrentTileCode()));
		}
		catch (final TileCodeFormatException exception)
		{
			throw new RuntimeException(exception);
		}

		for (final IListener listener : this.listeners)
		{
			listener.onTurn(turn, event.getCurrentTileCode());
		}
	}

	@Override
	public void onConnectionAccepted(final ServerToClient_ConnectionAcceptedEvent event)
	{
		final Iterator<String> names = event.getExistingPlayers().iterator();
		final Iterator<State> states = event.getStates().iterator();

		while (names.hasNext() && states.hasNext())
		{
			this.participants.add(new Participant(names.next(), states.next()));
		}

		this.participant = this.participants.get(this.participants.size() - 1);

		fireConnectionAccepted();
	}

	// ============================================================================================
	// === EVENT FIRING
	// ============================================================================================

	private void fireParticipantConnected(final Participant newClient)
	{
		for (final IListener listener : getListeners())
		{
			listener.onClientConnected(newClient);
		}
	}

	private void fireConnectionBroken(final ServerToClient_ClientDisconnectedEvent event)
	{
		for (final IListener listener : getListeners())
		{
			listener.onAfterConnectionBroken(event.getReason());
		}
	}

	private void fireParticipantDisconnected(final Participant client)
	{
		for (final IListener listener : getListeners())
		{
			listener.onClientDisconnected(client);
		}
	}

	private void fireChatMessage(final ServerToClient_ChatMessageEvent event, final Participant client)
	{
		for (final IListener listener : getListeners())
		{
			listener.onChatMessage(client, event.getMessage());
		}
	}

	private void fireGameStarted()
	{
		for (final IListener listener : getListeners())
		{
			listener.onStart();
		}
	}

	private List<IListener> getListeners()
	{
		return ImmutableList.copyOf(this.listeners);
	}

	// ============================================================================================
	// === EVENT HANDLERS
	// ============================================================================================

	// ============================================================================================
	// === EVENT HANDLERS
	// ============================================================================================

	private void fireConnectionAccepted()
	{
		for (final IListener listener : this.listeners)
		{
			listener.onConnectionAccepted();
		}
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
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

	public IClientTransport getTransport()
	{
		return this.transport;
	}

	@Override
	public List<Participant> getParticipants()
	{
		return this.participants;
	}

	@Override
	public Participant getParticipant()
	{
		return this.participant;
	}

	public IApplication getApplication()
	{
		return this.application;
	}

	// ============================================================================================
	// === TILE SEQUENCE PROXY
	// ============================================================================================

	private final class RemoteTileSequence implements ITileSequence
	{
		private Tile currentTile = null;
		private boolean hasMoreTiles = true;
		private final Integer totalNumberOfTiles;

		public RemoteTileSequence(final Integer totalNumberOfTiles)
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
