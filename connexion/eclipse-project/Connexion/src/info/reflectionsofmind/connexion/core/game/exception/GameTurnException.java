package info.reflectionsofmind.connexion.core.game.exception;

import info.reflectionsofmind.connexion.core.board.exception.BoardException;

public class GameTurnException extends GameException
{
	private static final long serialVersionUID = 1L;

	public GameTurnException()
	{
		super();
	}

	public GameTurnException(BoardException cause)
	{
		super(cause);
	}
}
