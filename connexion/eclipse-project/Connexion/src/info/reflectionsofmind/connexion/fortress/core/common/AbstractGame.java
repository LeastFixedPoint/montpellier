package info.reflectionsofmind.connexion.fortress.core.common;

import info.reflectionsofmind.connexion.fortress.core.common.board.Board;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.RectangularGeometry;
import info.reflectionsofmind.connexion.util.AbstractListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGame<TListener> extends AbstractListener<TListener>
{
	private final List<Player> players = new ArrayList<Player>();
	private final Board board = new Board(new RectangularGeometry());

	public List<Player> getPlayers()
	{
		return this.players;
	}

	public Board getBoard()
	{
		return this.board;
	}
}
