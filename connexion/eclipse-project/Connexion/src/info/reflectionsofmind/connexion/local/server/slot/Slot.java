package info.reflectionsofmind.connexion.local.server.slot;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.event.cts.ClientToServerDecoder;
import info.reflectionsofmind.connexion.event.cts.ClientToServerEvent;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

public class Slot
<NodeType extends INode, TransportType extends ITransport<NodeType>> // 
		implements // 
		ISlot<TransportType>, // 
		ITransport.IListener<NodeType>
{
	private final TransportType transport;
	private final IServer server;

	private State state = State.CLOSED;
	private NodeType clientNode;
	private IRemoteClient client;
	private Player player;
	private Exception error;

	private final List<IListener> listeners = new ArrayList<IListener>();

	public Slot(final IServer server, final TransportType transport)
	{
		this.server = server;
		this.transport = transport;

		setState(State.CLOSED);
	}

	public final void addListener(final IListener listener)
	{
		this.listeners.add(listener);
	}

	protected final void fireAfterStateChange(State previousState)
	{
		for (final IListener listener : this.listeners)
		{
			listener.onAfterSlotStateChange(previousState);
		}
	}

	public void onMessage(final NodeType from, final String message)
	{
		if (from != this.clientNode) return;

		final ClientToServerEvent event = ClientToServerDecoder.decode(message);

		if (event instanceof ClientToServer_ClientConnectionRequestEvent)
		{
			getServer().onConnectionRequest(getClient(), (ClientToServer_ClientConnectionRequestEvent) event);
		}
	}

	// ============================================================================================
	// === ACTIONS
	// ============================================================================================

	public final void open()
	{
		if (this.state != State.CLOSED) throw new IllegalStateException();

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
		
		fireAfterStateChange(State.OPEN);
	}

	public final void close()
	{
		if (this.state != State.OPEN) throw new IllegalStateException();
		
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
	}

	public final void accept(final Player player)
	{
		if (this.state != State.CONNECTED) throw new IllegalStateException();
		this.player = player;
		
		try
		{
			this.client.sendEvent(new ServerToClient_ConnectionAcceptedEvent(getServer()));
			this.state = State.ACCEPTED;
		}
		catch (TransportException exception)
		{
			setError(exception);
		}
	}

	public final void disconnect(final DisconnectReason reason)
	{
		if (this.state != State.CONNECTED && this.state != State.ACCEPTED) throw new IllegalStateException();

		try
		{
			final ServerToClient_PlayerDisconnectedEvent event = // 
				new ServerToClient_PlayerDisconnectedEvent(getServer(), getPlayer(), reason);
			
			this.transport.send(clientNode, event.encode());
			this.transport.removeListener(this);
			this.transport.stop();
			setState(State.CLOSED);
		}
		catch (TransportException exception)
		{
			setError(exception);
		}
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

	public void setError(final Exception error)
	{
		setState(State.ERROR);
		this.error = error;
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	public TransportType getTransport()
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
