package info.reflectionsofmind.connexion.local.server.slot;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.server.ServerSideDisconnectReason;
import info.reflectionsofmind.connexion.local.server.IServer;
import info.reflectionsofmind.connexion.local.server.transport.ITransport;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;

public interface ISlot
{
	public enum State
	{
		INIT, CLOSED, OPEN, CONNECTED, ACCEPTED, SPECTATOR, ERROR
	}

	/** {@link State#INIT} to {@link State#CLOSED} */
	void init(final IServer server);           

	/** {@link State#CLOSED} to {@link State#OPEN} */
	void open();                               

	/** {@link State#OPEN} to {@link State#CLOSED} */
	void close();
	
	/** {@link State#CONNECTED} to {@link State#ACCEPTED} */
	void accept(Player player);
	
	/** {@link State#CONNECTED} to {@link State#SPECTATOR} */
	void spectate();
	
	/** {@link State#CONNECTED}, {@link State#ACCEPTED}, {@link State#SPECTATOR} to {@link State#CLOSED} */
	void disconnect(ServerSideDisconnectReason reason);

	State getState();
	IRemoteClient getClient();
	Player getPlayer();
	Exception getError();
	ITransport getTransport();
	
	void addListener(IListener listener);

	public interface IListener
	{
		void onConnected();
	}
}