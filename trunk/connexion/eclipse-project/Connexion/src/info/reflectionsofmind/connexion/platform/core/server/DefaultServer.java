package info.reflectionsofmind.connexion.platform.core.server;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.platform.core.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.core.common.Participant;
import info.reflectionsofmind.connexion.platform.core.common.Participant.State;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessageDecoder;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_Action;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_Chat;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_ConnectionRequest;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.CTSMessage_DisconnectNotice;
import info.reflectionsofmind.connexion.platform.core.common.message.cts.ICTSMessageTarget;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerGame;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerGameFactory;
import info.reflectionsofmind.connexion.platform.core.transport.IClientNode;
import info.reflectionsofmind.connexion.platform.core.transport.IClientPacket;
import info.reflectionsofmind.connexion.platform.core.transport.IServerTransport;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;
import info.reflectionsofmind.connexion.util.AbstractListener;
import info.reflectionsofmind.connexion.util.form.Form;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class DefaultServer extends AbstractListener<IServer.IListener> implements IServer, ICTSMessageTarget, Participant.IStateListener
{
	private final List<IRemoteClient> clients = new ArrayList<IRemoteClient>();
	private final IApplication application;

	private IServerGameFactory gameFactory;
	private IServerGame game;

	public DefaultServer(IApplication application)
	{
		this.application = application;
	}

	// ============================================================================================
	// === TRANSPORT MESSAGE HANDLER
	// ============================================================================================

	@Override
	public synchronized void onPacket(IClientPacket packet)
	{
		CTSMessageDecoder.decode(packet.getContents()).dispatch(packet.getFrom(), this);
	}

	@Override
	public synchronized void onError(TransportException exception)
	{
		exception.printStackTrace();
	}

	@Override
	public synchronized void onBeforeStopped(IServerTransport transport)
	{
		for (IRemoteClient client : getClients())
		{
			if (client.getNode().getTransport() == transport)
			{
				disconnect(client, DisconnectReason.CONNECTION_FAILURE);
			}
		}
	}

	// ============================================================================================
	// === STC MESSAGE DISPATCH HANDLERS
	// ============================================================================================

	@Override
	public synchronized void onConnectionRequest(IClientNode from, CTSMessage_ConnectionRequest event)
	{
		final IRemoteClient newRemoteClient = new RemoteClient(this, new Participant(event.getPlayerName()), from);

		this.clients.add(newRemoteClient);

		newRemoteClient.sendConnectionAccepted();

		for (IRemoteClient client : getClients())
		{
			if (client != newRemoteClient)
			{
				client.sendConnected(newRemoteClient);
			}
		}

		for (IServer.IListener listener : getListeners())
		{
			listener.onClientConnected(newRemoteClient);
		}

		newRemoteClient.getParticipant().addStateListener(this);
	}

	@Override
	public synchronized void onDisconnectNotice(IClientNode from, CTSMessage_DisconnectNotice event)
	{
		final IRemoteClient disconnectedClient = ServerUtil.getClientByNode(this, from);
		disconnect(disconnectedClient, event.getReason());
	}

	@Override
	public synchronized void onChatMessage(IClientNode from, CTSMessage_Chat event)
	{
		final IRemoteClient client = ServerUtil.getClientByNode(this, from);

		for (IRemoteClient otherClient : getClients())
		{
			if (otherClient != client)
			{
				otherClient.sendChatMessage(client, event.getMessage());
			}
		}

		for (IServer.IListener listener : getListeners())
		{
			listener.onClientMessage(client, event.getMessage());
		}
	}

	@Override
	public void onAction(IClientNode from, CTSMessage_Action event)
	{
		this.game.onAction(this.game.getCoder().decodeAction(event.getEncodedAction()));
	}

	// ====================================================================================================
	// === CLIENT STATE LISTENER
	// ====================================================================================================

	@Override
	public void onAfterClientStateChange(Participant client, State previousState)
	{
		for (IRemoteClient remoteClient : getClients())
		{
			remoteClient.sendStateChanged(ServerUtil.getClient(this, client), previousState);
		}
	}

	// ============================================================================================
	// === ACTIONS
	// ============================================================================================

	@Override
	public void disconnect(IRemoteClient disconnectedClient, DisconnectReason reason)
	{
		for (IRemoteClient client : getClients())
		{
			if (client != disconnectedClient)
				client.sendDisconnected(disconnectedClient, reason);
		}

		this.clients.remove(disconnectedClient);

		for (IServer.IListener listener : getListeners())
		{
			listener.onClientDisconnected(disconnectedClient);
		}
	}

	@Override
	public synchronized void startGame()
	{
		final Form form = this.gameFactory.newConfigurationForm();

		if (!this.application.getUI().displayForm(form))
			return;

		this.game = this.gameFactory.createServerGame(form);

		int numPlayers = 0;
		for (IRemoteClient client : getClients())
			if (client.getParticipant().getState() == State.ACCEPTED)
				numPlayers++;

		this.game.start(numPlayers);

		for (IRemoteClient client : getClients())
		{
			client.sendGameStarted();
		}
	}

	@Override
	public void sendChat(String message)
	{
		for (IRemoteClient client : getClients())
		{
			client.sendChatMessage(null, message);
		}
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	@Override
	public IApplication getApplication()
	{
		return this.application;
	}

	@Override
	public List<IRemoteClient> getClients()
	{
		return ImmutableList.copyOf(this.clients);
	}

	@Override
	public void setGameFactory(IServerGameFactory gameFactory)
	{
		this.gameFactory = gameFactory;
	}

	@Override
	public IServerGame getGame()
	{
		return null;
	}
}
