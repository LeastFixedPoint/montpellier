package info.reflectionsofmind.connexion.core.board;

import java.util.ArrayList;
import java.util.List;

import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.tile.Side;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.core.util.Loop;

public class OrientedTile
{
	private final Tile tile;
	private final IDirection direction;

	public OrientedTile(Tile tile, IDirection direction)
	{
		this.tile = tile;
		this.direction = direction;
	}

	public Tile getTile()
	{
		return this.tile;
	}

	public IDirection getDirection()
	{
		return this.direction;
	}

	public Loop<Side> getSides()
	{
		final List<Side> sides = new ArrayList<Side>();

		final int d = getDirection().getIndex();
		final int n = getDirection().getGeometry().getNumberOfDirections();

		for (int i = 0; i < n; i++)
		{
			sides.add(getTile().getSides().get(i + d));
		}

		return new Loop<Side>(sides);
	}
}
