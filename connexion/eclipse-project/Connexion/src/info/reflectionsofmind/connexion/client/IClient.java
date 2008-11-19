package info.reflectionsofmind.connexion.client;

import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.server.DisconnectReason;

public interface IClient
{
	void onTurn(Turn turn);
	void onStart(Player player);
	void onDisconnect(IClient client, DisconnectReason reason);
	String getName();
}
