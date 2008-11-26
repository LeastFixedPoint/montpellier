package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.Meeple.Type;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.util.convert.AbstractMessage;
import info.reflectionsofmind.connexion.util.convert.IConvertible;

public class Turn implements IConvertible<Turn>
{
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
	public IMessage<Turn> encode()
	{
		final String string = "[" + isAdvancePlayer() + //
				":" + ((Location) getLocation()).getX() + // 
				":" + ((Location) getLocation()).getY() + //
				":" + getDirection().getIndex() + //
				":" + getMeepleType().toString() + //
				":" + getSectionIndex() + "]";

		return new Message(string);
	}

	public final static class Message extends AbstractMessage<Turn>
	{
		public Message(final String string)
		{
			super(string);
		}

		@Override
		public Turn decode()
		{
			final String[] tokens = getTokens();

			final Geometry geometry = new Geometry();

			final Turn turn = new Turn(Boolean.parseBoolean(tokens[0]));

			turn.addTilePlacement(//
					new Location(geometry, Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])), //
					geometry.getDirections().get(Integer.parseInt(tokens[3])));

			turn.addMeeplePlacement(Type.valueOf(tokens[4]), Integer.valueOf(tokens[5]));

			return turn;
		}
	}
}
