package info.reflectionsofmind.connexion.local.server.slot;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.server.ServerSideDisconnectReason;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSlot implements ISlot
{
	private State state = State.INIT;
	private IServer server;
	private Player player;
	private IRemoteClient client;
	private Exception error;

	private final List<IListener> listeners = new ArrayList<IListener>();

	// ============================================================================================
	// === LISTENERS
	// ============================================================================================

	public final void addListener(final IListener listener)
	{
		this.listeners.add(listener);
	}

	protected final void fireConnected()
	{
		for (final IListener listener : this.listeners)
		{
			listener.onConnected();
		}
	}

	// ============================================================================================
	// === ACTIONS
	// ============================================================================================

	public final void init(final IServer server)
	{
		if (this.state != State.INIT) throw new IllegalStateException();
		if (server == null) throw new NullPointerException();
		this.server = server;
		setState(State.CLOSED);
	}

	public final void open()
	{
		if (this.state != State.CLOSED) throw new IllegalStateException();
		doOpen();
	}

	protected abstract void doOpen();

	public final void close()
	{
		if (this.state != State.OPEN) throw new IllegalStateException();
		doClose();
	}

	protected void doClose()
	{
		setState(State.CLOSED);
	}

	public final void accept(final Player player)
	{
		if (this.state != State.CONNECTED) throw new IllegalStateException();
		this.player = player;
		doAccept();
	}

	protected void doAccept()
	{
		setState(State.ACCEPTED);
	}

	public final void spectate()
	{
		if (this.state != State.CONNECTED) throw new IllegalStateException();
		doSpectate();
	}

	protected void doSpectate()
	{
		setState(State.SPECTATOR);
	}

	public final void disconnect(final ServerSideDisconnectReason reason)
	{
		if (this.state != State.CONNECTED && this.state != State.ACCEPTED) throw new IllegalStateException();
		doDisconnect(reason);
	}

	protected void doDisconnect(ServerSideDisconnectReason reason)
	{
		setState(State.CLOSED);
	}

	// ============================================================================================
	// === SETTERS
	// ============================================================================================

	protected final void setClient(final IRemoteClient client)
	{
		this.client = client;
	}

	protected final void setState(final State state)
	{
		this.state = state;
	}
	
	public void setError(Exception error)
	{
		setState(State.ERROR);
		this.error = error;
	}
	
	// ============================================================================================
	// === GETTERS
	// ============================================================================================

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
