package info.reflectionsofmind.connexion.fortress.core.common.exception;

import info.reflectionsofmind.connexion.fortress.core.common.board.exception.BoardException;

/** Is thrown when a requested turn cannot be performed in the game. */ 
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
