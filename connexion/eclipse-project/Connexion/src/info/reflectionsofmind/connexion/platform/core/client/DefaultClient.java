package info.reflectionsofmind.connexion.platform.core.client;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.platform.core.client.game.IClientGame;
import info.reflectionsofmind.connexion.platform.core.client.game.IClientGameFactory;
import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IClientInitInfo;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_Action;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.AbstractCTSMessage;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_Chat;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_ConnectionRequest;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_DisconnectNotice;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.ISTCMessageTarget;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessageDecoder;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_Change;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_Chat;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_ConnectionAccepted;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_GameStarted;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_ParticipantConnected;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_ParticipantDisconnected;
import info.reflectionsofmind.connexion.platform.core.common.message.stc.STCMessage_ParticipantStateChanged;
import info.reflectionsofmind.connexion.platform.core.transport.IClientTransport;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;
import info.reflectionsofmind.connexion.util.AbstractListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultClient extends AbstractListener<IClient.IListener> implements IClient, IClientTransport.IListener, ISTCMessageTarget
{
	// Basic fields

	private final List<IListener> listeners = new ArrayList<IListener>();
	private final IApplication application;
	private String name = "Anonymous";

	// Connected-state fields

	private final List<Participant> participants = new ArrayList<Participant>();
	private IClientTransport transport;
	private Participant participant;

	// Game-state fields

	private IClientGameFactory<?> gameFactory;
	private IClientGame<IClientInitInfo, IAction, IChange, IClientGame.IListener> game;

	public DefaultClient(IApplication application)
	{
		this.application = application;

		// this.tileSource = new
		// DefaultTileSource(getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/tilelist/DefaultTileList.properties"));
	}

	// ============================================================================================
	// === COMMANDS
	// ============================================================================================

	private void send(final AbstractCTSMessage event)
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

	public void setGameFactory(IClientGameFactory<?> factory)
	{
		this.gameFactory = factory;
	}

	public void connect(final IClientTransport transport)
	{
		this.transport = transport;
		getTransport().addListener(this);
		send(new CTSMessage_ConnectionRequest(getName()));
	}

	public void disconnect(final DisconnectReason reason)
	{
		send(new CTSMessage_DisconnectNotice(reason));

		this.transport = null;
		this.participant = null;
		this.participants.clear();
		this.game = null;

		for (final IListener listener : this.listeners)
			listener.onAfterConnectionBroken(reason);
	}

	@Override
	public void sendChatMessage(final String message)
	{
		send(new CTSMessage_Chat(message));

		for (final IListener listener : this.listeners)
			listener.onChatMessage(getParticipant(), message);
	}

	@Override
	public void sendAction(IAction action)
	{
		send(new CTSMessage_Action(action.encode()));
	}

	// ============================================================================================
	// === TRANSPORT LISTENERS
	// ============================================================================================

	@Override
	public void onPacket(String contents)
	{
		STCMessageDecoder.decode(contents).dispatch(this);
	}

	@Override
	public void onError(TransportException exception)
	{
		throw new RuntimeException(exception);
	}

	// ============================================================================================
	// === MESSAGE HANDLERS
	// ============================================================================================

	@Override
	public void onConnectionAcceptedMessage(final STCMessage_ConnectionAccepted event)
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

	@Override
	public void onParticipantConnectedMessage(final STCMessage_ParticipantConnected event)
	{
		final Participant newParticipant = new Participant(event.getParticipantName());
		this.participants.add(newParticipant);
		fireParticipantConnected(newParticipant);
	}

	@Override
	public void onParticipantStateChangedMessage(final STCMessage_ParticipantStateChanged event)
	{
		final Participant participant = this.participants.get(event.getParticipantIndex());
		participant.setState(event.getNewState());
	}

	@Override
	public void onParticipantDisconnectedMessage(final STCMessage_ParticipantDisconnected event)
	{
		final Participant patricipant = this.participants.get(event.getParticipantIndex());

		if (patricipant == getParticipant())
		{
			this.transport = null;
			this.participant = null;
			this.participants.clear();
			this.game = null;

			fireConnectionBroken(event);
		}
		else
		{
			this.participants.remove(patricipant);

			fireParticipantDisconnected(patricipant);
		}
	}

	@Override
	public void onChatMessage(final STCMessage_Chat event)
	{
		final Participant participant = event.getParticipantIndex() == null ? null : this.participants.get(event.getParticipantIndex());
		fireChatMessage(event, participant);
	}

	@Override
	public void onGameStartedMessage(final STCMessage_GameStarted message)
	{
		final List<Player> players = new ArrayList<Player>();

		for (final Participant client : getParticipants())
			if (client.getState() == Participant.State.ACCEPTED)
				players.add(new Player());

		this.game = this.gameFactory.createClientGame();

		fireGameStarting();

		this.game.start(this.game.getCoder().decodeInitInfo(message.getEncodedInitInfo()));
	}

	public void onChangeMessage(STCMessage_Change message)
	{
		this.game.onChange(this.game.getCoder().decodeChange(message.getEncodedChange()));
	}

	private void fireParticipantConnected(final Participant newClient)
	{
		for (final IListener listener : getListeners())
			listener.onClientConnected(newClient);
	}

	private void fireConnectionBroken(final STCMessage_ParticipantDisconnected event)
	{
		for (final IListener listener : getListeners())
			listener.onAfterConnectionBroken(event.getReason());
	}
	
	// ============================================================================================
	// === EVENT FIRING METHODS
	// ============================================================================================

	private void fireParticipantDisconnected(final Participant client)
	{
		for (final IListener listener : getListeners())
			listener.onClientDisconnected(client);
	}

	private void fireChatMessage(final STCMessage_Chat event, final Participant client)
	{
		for (final IListener listener : getListeners())
			listener.onChatMessage(client, event.getMessage());
	}

	private void fireGameStarting()
	{
		for (final IListener listener : getListeners())
			listener.onGameStarting();
	}
	
	private void fireConnectionAccepted()
	{
		for (final IListener listener : this.listeners)
			listener.onConnectionAccepted();
	}

	// ============================================================================================
	// === EVENT HANDLERS
	// ============================================================================================

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
	public IClientGame<?, ?, ?, ?> getGame()
	{
		return this.game;
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
}
