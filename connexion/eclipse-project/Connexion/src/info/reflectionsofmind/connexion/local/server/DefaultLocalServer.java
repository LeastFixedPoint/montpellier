package info.reflectionsofmind.connexion.local.server;

import info.reflectionsofmind.connexion.core.board.geometry.IGeometry;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.game.exception.GameTurnException;
import info.reflectionsofmind.connexion.core.game.sequence.RandomTileSequence;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_MessageEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_TurnEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_GameStartedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerConnectedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_TurnEvent;
import info.reflectionsofmind.connexion.local.server.slot.ISlot;
import info.reflectionsofmind.connexion.local.settings.Settings;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.tilelist.DefaultTileSource;
import info.reflectionsofmind.connexion.tilelist.ITileSource;
import info.reflectionsofmind.connexion.tilelist.TileData;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.transport.hotseat.ServerHotSeatTransport;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class DefaultLocalServer implements IServer, IRemoteClient.IListener
{
	private final List<ITransport<?>> transports = new ArrayList<ITransport<?>>();
	private final List<ISlot<?>> slots = new ArrayList<ISlot<?>>();
	private final Settings settings;

	private Game game;
	private ITileSource tileSource;
	
	public DefaultLocalServer(final Settings settings)
	{
		this.settings = settings;
		
		this.transports.add(new ServerHotSeatTransport(this));
		this.transports.add(new JabberTransport(settings.getJabberAddress()));
	}

	// ====================================================================================================
	// === IMPLEMENTATION
	// ====================================================================================================

	public void addSlot(ISlot<?> slot)
	{
		if (this.game != null) throw new RuntimeException("Game already started.");
		this.slots.add(slot);
	}

	private void sendStartEvents(final Tile initialTile)
	{
		for (final ISlot<?> slot : this.slots)
		{
			if (slot.getState() == ISlot.State.CONNECTED)
			{
				try
				{
					slot.getClient().sendEvent(new ServerToClient_GameStartedEvent(getGame()));
				}
				catch (TransportException exception)
				{
					throw new RuntimeException();
					// TODO Handle disconnect on game start
				}
			}
		}
	}

	private void createGame(final String name)
	{
		try
		{
			this.tileSource = new DefaultTileSource(getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/tilelist/DefaultTileList.properties"));

			final List<Tile> tiles = new ArrayList<Tile>();

			for (final TileData tileData : this.tileSource.getTiles())
			{
				tiles.add(new Tile(tileData.getCode()));
			}

			this.game = new Game(name, new RandomTileSequence(tiles), ServerUtil.getPlayers(this));
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

	private void placeInitialTile() throws GameTurnException
	{
		final Turn turn = new Turn(false);
		final IGeometry geometry = getGame().getBoard().getGeometry();
		turn.addTilePlacement(geometry.getInitialLocation(), geometry.getDirections().get(0));
		getGame().doTurn(turn);
	}

	// ============================================================================================
	// === ACTIONS
	// ============================================================================================

	@Override
	public synchronized void startGame(final String name)
	{
		createGame(name);

		final Tile initialTile = this.game.getCurrentTile();

		try
		{
			placeInitialTile();
		}
		catch (GameTurnException exception)
		{
			throw new RuntimeException(exception);
		}

		sendStartEvents(initialTile);
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

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
	public List<ISlot<?>> getSlots()
	{
		return ImmutableList.copyOf(this.slots);
	}

	@Override
	public List<ITransport<?>> getTransports()
	{
		return ImmutableList.copyOf(this.transports);
	}
	
	@Override
	public Settings getSettings()
	{
		return this.settings;
	}

	// ============================================================================================
	// === HANDLERS
	// ============================================================================================

	@Override
	public void onConnectionRequest(IRemoteClient sender, ClientToServer_ClientConnectionRequestEvent event)
	{
		final ISlot<?> senderSlot = ServerUtil.getSlotByClient(this, sender);

		final Player player = new Player(event.getPlayerName());
		senderSlot.accept(player);

		try
		{
			sender.sendEvent(new ServerToClient_ConnectionAcceptedEvent(this));
		}
		catch (TransportException exception)
		{
			senderSlot.disconnect(DisconnectReason.CONNECTION_FAILURE);
		}

		for (ISlot<?> slot : ServerUtil.getConnectedSlots(this))
		{
			if (slot != senderSlot)
			{
				try
				{
					slot.getClient().sendEvent(new ServerToClient_PlayerConnectedEvent(player));
				}
				catch (TransportException exception)
				{
					senderSlot.disconnect(DisconnectReason.CONNECTION_FAILURE);
				}
			}
		}
	}

	@Override
	public void onMessage(IRemoteClient sender, ClientToServer_MessageEvent event)
	{
	}

	@Override
	public synchronized void onTurn(final IRemoteClient sender, final ClientToServer_TurnEvent event)
	{
		try
		{
			this.game.doTurn(event.getTurn());
		}
		catch (final GameTurnException exception)
		{
			ServerUtil.getSlotByClient(this, sender).disconnect(DisconnectReason.DESYNCHRONIZATION);
			return;
		}

		for (final IRemoteClient client : ServerUtil.getClients(this))
		{
			try
			{
				final Turn turn = (client == sender) ? null : event.getTurn();
				client.sendEvent(new ServerToClient_TurnEvent(turn, getGame().getCurrentTile().getCode()));
			}
			catch (final TransportException exception)
			{
				ServerUtil.getSlotByClient(this, sender).disconnect(DisconnectReason.CONNECTION_FAILURE);
			}
		}
	}

	@Override
	public synchronized void onDisconnect(final IRemoteClient sender, ClientToServer_ClientDisconnectedEvent event)
	{
		throw new UnsupportedOperationException("Disconnects not supported yet.");
	}
}
