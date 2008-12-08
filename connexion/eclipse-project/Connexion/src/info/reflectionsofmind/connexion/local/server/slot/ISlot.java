package info.reflectionsofmind.connexion.local.server.slot;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.transport.ITransport;

public interface ISlot
{
	public enum State
	{
		CLOSED, OPEN, CONNECTED, ACCEPTED, ERROR
	}
	
	void setTransport(ITransport transport);
	
	/** {@link State#CLOSED} to {@link State#OPEN} */
	void open();                               

	/** {@link State#OPEN} to {@link State#CLOSED} */
	void close();
	
	/** {@link State#CONNECTED} to {@link State#ACCEPTED} */
	void acceptAs(Player player);
	
	/** {@link State#CONNECTED}, {@link State#ACCEPTED} to {@link State#CLOSED} */
	void disconnect(DisconnectReason reason);

	State getState();
	IRemoteClient getClient();
	Player getPlayer();
	Exception getError();
	ITransport getTransport();
	
	void addListener(IListener listener);

	public interface IListener
	{
		void onAfterSlotStateChange(ISlot slot, State previousState);
	}
}