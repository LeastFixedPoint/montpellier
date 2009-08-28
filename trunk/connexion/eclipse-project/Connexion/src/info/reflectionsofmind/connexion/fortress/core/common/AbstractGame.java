package info.reflectionsofmind.connexion.fortress.core.common;

import info.reflectionsofmind.connexion.fortress.core.common.board.Board;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.RectangularGeometry;
import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IGame;
import info.reflectionsofmind.connexion.platform.core.common.game.IStartInfo;
import info.reflectionsofmind.connexion.util.AbstractListener;
import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGame<TListener> extends AbstractListener<TListener> implements IGame
{
	private final List<Player> players = new ArrayList<Player>();
	private final Board board = new Board(new RectangularGeometry());
	
	public ICoder<IAction> getActionCoder()
	{
		throw new UnsupportedOperationException();
	}
	
	public ICoder<IStartInfo> getStartInfoCoder()
	{
		throw new UnsupportedOperationException();
	}
	
	public ICoder<IChange> getChangeCoder()
	{
		throw new UnsupportedOperationException();
	}
	
	public List<Player> getPlayers()
	{
		return this.players;
	}
	
	public Board getBoard()
	{
		return this.board;
	}
}
