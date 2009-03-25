package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.Meeple.Type;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class Turn
{
	public final static String PREFIX = "turn";

	private final boolean advancePlayer;

	private IDirection direction;
	private ILocation location;
	private Meeple.Type meepleType;
	private int sectionIndex;

	public Turn()
	{
		this(true);
	}

	public Turn(final boolean advancePlayer)
	{
		this.advancePlayer = advancePlayer;
	}

	public void addTilePlacement(final ILocation location, final IDirection direction)
	{
		this.location = location;
		this.direction = direction;
	}

	public void addMeeplePlacement(final Meeple.Type meepleType, final int sectionIndex)
	{
		this.meepleType = meepleType;
		this.sectionIndex = sectionIndex;
	}

	public ILocation getLocation()
	{
		return this.location;
	}

	public IDirection getDirection()
	{
		return this.direction;
	}

	public Meeple.Type getMeepleType()
	{
		return this.meepleType;
	}

	public int getSectionIndex()
	{
		return this.sectionIndex;
	}

	public boolean isAdvancePlayer()
	{
		return this.advancePlayer;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof Turn)) return false;
		Turn turn = (Turn) obj;
		return turn.direction.equals(this.direction) //
				&& turn.location.equals(this.location) //
				&& turn.meepleType == this.meepleType //
				&& turn.sectionIndex == this.sectionIndex //
				&& turn.advancePlayer == this.advancePlayer;
	}

	public static final ICoder<Turn> CODER = new AbstractCoder<Turn>()
	{
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX + ":");
		}

		public String encode(Turn turn)
		{
			return join(PREFIX, //
					/* 0 */String.valueOf(turn.isAdvancePlayer()), //
					/* 1 */String.valueOf(((Location) turn.getLocation()).getX()), // 
					/* 2 */String.valueOf(((Location) turn.getLocation()).getY()), //
					/* 3 */String.valueOf(turn.getDirection().getIndex()), //
					/* 4 */turn.getMeepleType() == null ? "" : turn.getMeepleType().toString(), //
					/* 5 */String.valueOf(turn.getSectionIndex()));
		}

		public Turn decode(String string)
		{
			final String[] tokens = split(PREFIX, string);

			final Geometry geometry = new Geometry();

			final Turn turn = new Turn(Boolean.parseBoolean(tokens[0]));

			turn.addTilePlacement(//
					new Location(geometry, Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])), //
					geometry.getDirections().get(Integer.parseInt(tokens[3])));

			final Type meepleType = tokens[4].isEmpty() ? null : Type.valueOf(tokens[4]);
			turn.addMeeplePlacement(meepleType, Integer.valueOf(tokens[5]));
			return turn;
		}
	};
}
