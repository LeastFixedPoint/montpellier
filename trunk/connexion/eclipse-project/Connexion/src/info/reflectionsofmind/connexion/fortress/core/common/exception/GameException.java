package info.reflectionsofmind.connexion.fortress.core.common.exception;

import info.reflectionsofmind.connexion.fortress.core.common.board.exception.BoardException;

public class GameException extends Exception
{
	private static final long serialVersionUID = 1L;

	public GameException()
	{
		super();
	}

	public GameException(BoardException cause)
	{
		super(cause);
	}
}
