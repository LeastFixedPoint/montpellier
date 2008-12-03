package info.reflectionsofmind.connexion.local.server.slot;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.transport.ITransport;

public interface ISlot<TransportType extends ITransport<?>>
{
	public enum State
	{
		CLOSED, OPEN, CONNECTED, ACCEPTED, ERROR
	}

	/** {@link State#CLOSED} to {@link State#OPEN} */
	void open();                               

	/** {@link State#OPEN} to {@link State#CLOSED} */
	void close();
	
	/** {@link State#CONNECTED} to {@link State#ACCEPTED} */
	void accept(Player player);
	
	/** {@link State#CONNECTED}, {@link State#ACCEPTED} to {@link State#CLOSED} */
	void disconnect(DisconnectReason reason);

	State getState();
	IRemoteClient getClient();
	Player getPlayer();
	Exception getError();
	TransportType getTransport();
	
	void addListener(IListener listener);

	public interface IListener
	{
		void onAfterSlotStateChange(State previousState);
	}
}