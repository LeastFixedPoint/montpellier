package info.reflectionsofmind.connexion.platform.core.common.event.stc;

import info.reflectionsofmind.connexion.fortress.core.board.Meeple;
import info.reflectionsofmind.connexion.fortress.core.board.Meeple.Type;
import info.reflectionsofmind.connexion.fortress.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.fortress.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.fortress.core.board.geometry.rectangular.Direction;
import info.reflectionsofmind.connexion.fortress.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.fortress.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

/** A turn event coming from server. */
public class ServerToClient_TurnEvent extends ServerToClientEvent
{
	public final static String PREFIX = ServerToClientEvent.EVENT_PREFIX + ":turn";

	private final ILocation location;
	private final IDirection direction;
	private final Meeple.Type meepleType;
	private final int sectionIndex;

	private final String currentTileCode;

	public ServerToClient_TurnEvent(ILocation location, IDirection direction, Type meepleType, int sectionIndex, final String currentTileCode)
	{
		this.direction = direction;
		this.location = location;
		this.meepleType = meepleType;
		this.sectionIndex = sectionIndex;
		this.currentTileCode = currentTileCode;
	}

	@Override
	public void dispatch(IServerToClientEventListener listener)
	{
		listener.onTurn(this);
	}

	public String getCurrentTileCode()
	{
		return this.currentTileCode;
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

	@Override
	public String encode()
	{
		return CODER.encode(this);
	}

	public final static ICoder<ServerToClient_TurnEvent> CODER = new AbstractCoder<ServerToClient_TurnEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ServerToClient_TurnEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final Geometry geometry = new Geometry();

			final ILocation location = new Location(geometry, Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
			final IDirection direction = new Direction(geometry, Integer.parseInt(tokens[2]));
			final Meeple.Type meepleType = tokens[3].isEmpty() ? null : Meeple.Type.valueOf(tokens[3]);
			final int sectionIndex = Integer.parseInt(tokens[4]);

			final String currentTileCode = tokens[5];
			return new ServerToClient_TurnEvent(location, direction, meepleType, sectionIndex, currentTileCode);
		}

		@Override
		public String encode(final ServerToClient_TurnEvent event)
		{
			return join(PREFIX, // 			
					/* 0 */String.valueOf(((Location) event.getLocation()).getX()), // 
					/* 1 */String.valueOf(((Location) event.getLocation()).getY()), //
					/* 2 */String.valueOf(event.getDirection().getIndex()), //
					/* 3 */event.getMeepleType() == null ? "" : event.getMeepleType().toString(), //
					/* 4 */String.valueOf(event.getSectionIndex()), // 
					/* 5 */event.getCurrentTileCode());
		}
	};
}
