package info.reflectionsofmind.connexion.platform.core.client.game;

import java.util.List;

import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.Board;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.platform.core.client.IClientCoder;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;

public interface IClientGame
{
	void start(IStartInfo startInfo);

	void onChange(IChange change);

	List<Player> getPlayers();

	Board getBoard();

	Tile getCurrentTile();

	void addListener(IListener listener);

	IClientCoder getCoder();

	public interface IListener
	{
		void onStarted();

		void onFinished();
	}

}
