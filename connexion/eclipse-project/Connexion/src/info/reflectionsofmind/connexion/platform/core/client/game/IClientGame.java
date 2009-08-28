package info.reflectionsofmind.connexion.platform.core.client.game;

import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.Board;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.platform.core.client.IClientCoder;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;

import java.util.List;

public interface IClientGame
{
	void start(IStartInfo startInfo);
	
	void onChange(IChange change);
	
	List<Player> getPlayers();
	
	Board getBoard();
	
	Tile getCurrentTile();
	
	Player getCurrentPlayer();
	
	void addListener(IListener listener);
	
	IClientCoder getCoder();
	
	public interface IListener
	{
		void onStarted();
		
		void onFinished();
	}
}
