package info.reflectionsofmind.connexion.platform.server.game;

import info.reflectionsofmind.connexion.platform.client.game.IChange;
import info.reflectionsofmind.connexion.platform.common.game.IPlayer;

import java.util.Map;

public interface IChangeListener
{
	void onGameChange(Map<IPlayer, IChange<?>> changes);
}
