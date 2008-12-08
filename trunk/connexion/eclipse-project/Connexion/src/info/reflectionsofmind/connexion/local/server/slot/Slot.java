package info.reflectionsofmind.connexion.local.server.slot;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.event.cts.ClientToServerDecoder;
import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientDisconnectedEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_MessageEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.ServerUtil;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.remote.client.RemoteClient;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

public final class Slot implements ISlot, ITransport.IListener
{
	private final IServer server;

	private State state = State.CLOSED;

	private ITransport transport;
	private IRemoteClient client;
	private Player player;
	private Exception error;

	private final List<IListener> listeners = new ArrayList<IListener>();

	public Slot(final IServer server)
	{
		this.server = server;
		this.server.addSlot(this);

		setState(State.CLOSED);
	}

	// ============================================================================================
	// === EVENT DISPATCHER
	// ============================================================================================

	public final void addListener(final IListener listener)
	{
		if (listener == null) throw new NullPointerException();

		this.listeners.add(listener);
	}

	protected final void fireAfterStateChange(State previousState)
	{
		for (final IListener listener : this.listeners)
		{
			listener.onAfterSlotStateChange(this, previousState);
		}
	}

	public void onMessage(final INode from, final String message)
	{
		final ClientToServerEvent event = ClientToServerDecoder.decode(message);

		if (getState() == State.OPEN)
		{
			for (ISlot slot : ServerUtil.getConnectedSlots(getServer()))
			{
				if (slot.getClient().getClientNode() == from) return;
			}

			if (event instanceof ClientToServer_ClientConnectionRequestEvent)
			{
				final String name = ((ClientToServer_ClientConnectionRequestEvent) event).getPlayerName();
				setClient(new RemoteClient(getTransport(), from, name));
				setState(State.CONNECTED);
				getServer().onConnectionRequest(getClient(), (ClientToServer_ClientConnectionRequestEvent) event);
			}
		}
		else if (getState() == State.CONNECTED || getState() == State.ACCEPTED)
		{
			if (getClient().getClientNode() != from) return;

			if (event instanceof ClientToServer_MessageEvent)
			{
				event.dispatch(getClient(), getServer());
			}

			if (event instanceof ClientToServer_ClientDisconnectedEvent)
			{
				event.dispatch(getClient(), getServer());
			}
		}
		else
		{
			throw new IllegalStateException(getState().toString());
		}
	}

	// ============================================================================================
	// === ACTIONS
	// ============================================================================================

	private void assertState(State... states)
	{
		for (State state : states)
		{
			if (this.state == state) return;
		}

		throw new IllegalStateException(this.state.toString());
	}

	public final void open()
	{
		assertState(State.CLOSED);

		this.transport.addListener(this);

		try
		{
			this.transport.start();
			setState(State.OPEN);
		}
		catch (final TransportException exception)
		{
			setError(exception);
		}

		fireAfterStateChange(State.CLOSED);
	}

	public final void close()
	{
		assertState(State.OPEN);

		this.transport.removeListener(this);

		try
		{
			this.transport.stop();
			setState(State.CLOSED);
		}
		catch (TransportException exception)
		{
			setError(exception);
		}

		fireAfterStateChange(State.OPEN);
	}

	public final void acceptAs(final Player player)
	{
		assertState(State.CONNECTED);

		setPlayer(player);

		try
		{
			getClient().sendEvent(new ServerToClient_ConnectionAcceptedEvent(getServer()));
			setState(State.ACCEPTED);
		}
		catch (TransportException exception)
		{
			setError(exception);
		}

		fireAfterStateChange(State.CONNECTED);
	}

	public final void disconnect(final DisconnectReason reason)
	{
		assertState(State.CONNECTED, State.ACCEPTED);
		final State previousState = getState();

		try
		{
			final ServerToClient_PlayerDisconnectedEvent event = // 
			new ServerToClient_PlayerDisconnectedEvent(getServer(), getPlayer(), reason);

			this.transport.send((INode) getClient().getClientNode(), event.encode());
			this.transport.removeListener(this);
			this.transport.stop();
			setState(State.CLOSED);
		}
		catch (TransportException exception)
		{
			setError(exception);
		}

		fireAfterStateChange(previousState);
	}

	// ============================================================================================
	// === SETTERS
	// ============================================================================================

	@Override
	public void setTransport(ITransport transport)
	{
		assertState(State.CLOSED);
		this.transport = transport;
	}

	private final void setClient(final IRemoteClient client)
	{
		this.client = client;
	}

	private final void setState(final State state)
	{
		this.state = state;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	private void setError(final Exception error)
	{
		setState(State.ERROR);
		this.error = error;
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	public ITransport getTransport()
	{
		return this.transport;
	}

	protected final IServer getServer()
	{
		return this.server;
	}

	public final State getState()
	{
		return this.state;
	}

	public final IRemoteClient getClient()
	{
		return this.client;
	}

	public final Player getPlayer()
	{
		return this.player;
	}

	public final Exception getError()
	{
		return this.error;
	}
}
