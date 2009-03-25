package info.reflectionsofmind.connexion.fortress.core.board.exception;

import info.reflectionsofmind.connexion.fortress.core.board.TilePlacement;
import info.reflectionsofmind.connexion.fortress.core.board.BoardUtil.PlacementAnalysis;

public class InvalidTileLocationException extends TilePlacementException
{
	private static final long serialVersionUID = 1L;

	private final TilePlacement placement;
	private final PlacementAnalysis analysis;

	public InvalidTileLocationException(final TilePlacement placement, final PlacementAnalysis analysis)
	{
		this.placement = placement;
		this.analysis = analysis;
	}

	@Override
	public String getMessage()
	{
		return "Invalid placement " + this.placement + ": " + this.analysis;
	}
}
