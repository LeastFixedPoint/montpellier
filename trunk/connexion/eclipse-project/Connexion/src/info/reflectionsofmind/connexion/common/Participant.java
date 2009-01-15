package info.reflectionsofmind.connexion.common;

import info.reflectionsofmind.connexion.util.INamed;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class Participant implements INamed
{
	public enum State
	{
		CONNECTED, ACCEPTED, SPECTATOR;

		public static List<State> mapValueOf(List<String> strings)
		{
			return Lists.transform(strings, new Function<String, State>()
			{
				@Override
				public State apply(String string)
				{
					return valueOf(string);
				}
			});
		}
	}

	private final List<IStateListener> stateListeners = new ArrayList<IStateListener>();

	private final String name;
	private State state;

	public Participant(String name)
	{
		this(name, State.CONNECTED);
	}

	public Participant(String name, State state)
	{
		this.name = name;
		this.state = state;
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

	public void setState(State newState)
	{
		switch (newState)
		{
			case CONNECTED:
				setRejected();
				break;

			case ACCEPTED:
				setAccepted();
				break;

			case SPECTATOR:
				setSpectator();
				break;

			default:
				throw new RuntimeException("Unkown state [" + newState + "].");
		}

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

	// ====================================================================================================
	// === LISTENERS
	// ====================================================================================================

	public void addStateListener(final IStateListener listener)
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
		void onAfterClientStateChange(Participant client, State previousState);
	}
}
