package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.common.Client.State;
import info.reflectionsofmind.connexion.event.stc.ServerToClientEvent;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;

import java.util.ArrayList;
import java.util.List;

public final class RemoteClient implements IRemoteClient
{
	private final List<IStateListener> stateListeners = new ArrayList<IStateListener>();

	private State state = null;

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
		final State previousState = assertState((State) null);

		this.state = State.CONNECTED;

		fireStateChange(previousState);
	}

	public void setAccepted()
	{
		final State previousState = assertState(State.CONNECTED);

		this.state = State.ACCEPTED;

		fireStateChange(previousState);
	}

	public void setSpectator()
	{
		final State previousState = assertState(State.CONNECTED);

		this.state = State.SPECTATOR;

		fireStateChange(previousState);
	}

	public void setRejected()
	{
		final State previousState = assertState(State.ACCEPTED, State.SPECTATOR);

		this.state = State.CONNECTED;

		fireStateChange(previousState);
	}

	public void setDisconnected(DisconnectReason reason)
	{
		final State previousState = assertState(State.CONNECTED, State.ACCEPTED, State.SPECTATOR);

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
