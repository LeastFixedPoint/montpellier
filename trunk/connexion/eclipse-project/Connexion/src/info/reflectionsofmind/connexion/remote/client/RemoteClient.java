package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

public final class RemoteClient implements IRemoteClient
{
	private final List<IStateListener> stateListeners = new ArrayList<IStateListener>();

	private State state = State.PENDING;
	private Player player;

	private final String name;
	private final INode node;

	public RemoteClient(final INode clientNode, final String name)
	{
		this.name = name;
		this.node = clientNode;
	}

	// ====================================================================================================
	// === COMMANDS
	// ====================================================================================================

	private State assertState(State... states)
	{
		for (State state : states)
		{
			if (this.state == state) return this.state;
		}

		throw new IllegalStateException(this.state.toString());
	}

	@Override
	public void setConnected()
	{
		final State previousState = assertState(State.PENDING);

		this.state = State.CONNECTED;
		
		fireStateChange(previousState);
	}

	public void setAcceptedAs(Player player)
	{
		final State previousState = assertState(State.CONNECTED);

		if (player != null)
		{
			this.player = player;
			this.state = State.ACCEPTED;
		}
		else
		{
			this.state = State.SPECTATOR;
		}
		
		fireStateChange(previousState);
	}

	public void setRejected()
	{
		final State previousState = assertState(State.ACCEPTED, State.SPECTATOR);

		this.player = null;
		this.state = State.CONNECTED;
		
		fireStateChange(previousState);
	}

	public void setDisconnected(DisconnectReason reason)
	{
		final State previousState = assertState(State.CONNECTED, State.ACCEPTED, State.SPECTATOR);

		this.player = null;
		this.state = State.DISCONNECTED;
		
		fireStateChange(previousState);
	}

	@Override
	public void sendEvent(final ServerToClientEvent event) throws TransportException
	{
		getNode().getTransport().send(this.node, event.encode());
	}

	// ====================================================================================================
	// === GETTERS
	// ====================================================================================================

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public INode getNode()
	{
		return this.node;
	}

	@Override
	public State getState()
	{
		return this.state;
	}

	@Override
	public Player getPlayer()
	{
		return this.player;
	}
	
	// ====================================================================================================
	// === LISTENERS
	// ====================================================================================================

	@Override
	public void addListener(final IStateListener listener)
	{
		this.stateListeners.add(listener);
	}
	
	private void fireStateChange(final State previousState)
	{
		for (IStateListener listener : this.stateListeners)
			listener.onAfterClientStateChange(this, previousState);
	}
}
