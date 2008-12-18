package info.reflectionsofmind.connexion.local.client;

import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.core.game.Player;

import java.util.ArrayList;
import java.util.List;

public class Client
{
	public enum State
	{
		CONNECTED, ACCEPTED, SPECTATOR, DISCONNECTED
	}

	private final List<IStateListener> stateListeners = new ArrayList<IStateListener>();

	private final String name;
	private State state = State.CONNECTED;
	private Player player;

	public Client(String name)
	{
		this.name = name;
	}

	// ============================================================================================
	// === STATE CHANGES
	// ============================================================================================

	private State assertState(State... states)
	{
		for (State state : states)
		{
			if (this.state == state) return this.state;
		}

		throw new IllegalStateException(this.state.toString());
	}

	public void setAccepted()
	{
		final State previousState = assertState(State.CONNECTED);

		this.player = new Player(getName());
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

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	public State getState()
	{
		return this.state;
	}

	public String getName()
	{
		return this.name;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	// ====================================================================================================
	// === LISTENERS
	// ====================================================================================================

	public void addListener(final IStateListener listener)
	{
		this.stateListeners.add(listener);
	}

	private void fireStateChange(final State previousState)
	{
		for (IStateListener listener : this.stateListeners)
			listener.onAfterClientStateChange(this, previousState);
	}

	public static interface IStateListener
	{
		/** This is called when client state changes. */
		void onAfterClientStateChange(Client client, State previousState);
	}
}
