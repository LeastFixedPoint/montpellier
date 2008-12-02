package info.reflectionsofmind.connexion.local.server;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.local.server.slot.ISlot;
import info.reflectionsofmind.connexion.local.server.transport.ITransport;
import info.reflectionsofmind.connexion.remote.client.IRemoteClient;
import info.reflectionsofmind.connexion.tilelist.ITileSource;

import java.util.List;

public interface IServer extends IRemoteClient.IListener
{
	void addSlot(ISlot slot);
	List<ISlot> getSlots();

	void startGame(String name);
	Game getGame();
	ITileSource getTileSource();
	
	List<ITransport> getTransports();
	
	public interface IPlayerListener
	{
		void onMessage(Player player, String message);
		void onAfterPlayerConnected(ISlot slot);
		void onBeforePlayerDisconnected(ISlot slot, ServerSideDisconnectReason reason);
	}
}
