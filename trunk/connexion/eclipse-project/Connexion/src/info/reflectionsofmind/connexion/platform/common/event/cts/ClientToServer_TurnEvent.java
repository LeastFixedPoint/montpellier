package info.reflectionsofmind.connexion.platform.common.event.cts;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.Meeple.Type;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Direction;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Geometry;
import info.reflectionsofmind.connexion.core.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.platform.transport.IClientNode;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ClientToServer_TurnEvent extends ClientToServerEvent
{
	public final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":turn";

	private final IDirection direction;
	private final ILocation location;
	private final Meeple.Type meepleType;
	private final int sectionIndex;
	
	public ClientToServer_TurnEvent(IDirection direction, ILocation location, Type meepleType, int sectionIndex)
	{
		this.direction = direction;
		this.location = location;
		this.meepleType = meepleType;
		this.sectionIndex = sectionIndex;
	}

	@Override
	public void dispatch(IClientNode from, IClientToServerEventListener target)
	{
		target.onClientTurn(from, this);
	}
	
	public IDirection getDirection()
	{
		return this.direction;
	}
	
	public ILocation getLocation()
	{
		return this.location;
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

	public final static ICoder<ClientToServer_TurnEvent> CODER = new AbstractCoder<ClientToServer_TurnEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ClientToServer_TurnEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final Geometry geometry = new Geometry();
			
			final ILocation location = new Location(geometry, Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
			final IDirection direction = new Direction(geometry, Integer.parseInt(tokens[2]));
			final Meeple.Type meepleType = tokens[3].isEmpty() ? null : Meeple.Type.valueOf(tokens[3]);
			final int sectionIndex = Integer.parseInt(tokens[4]);
			
			return new ClientToServer_TurnEvent(direction, location, meepleType, sectionIndex);
		}

		@Override
		public String encode(final ClientToServer_TurnEvent event)
		{
			return join(PREFIX,
					/* 0 */String.valueOf(((Location) event.getLocation()).getX()), // 
					/* 1 */String.valueOf(((Location) event.getLocation()).getY()), //
					/* 2 */String.valueOf(event.getDirection().getIndex()), //
					/* 3 */event.getMeepleType() == null ? "" : event.getMeepleType().toString(), //
					/* 4 */String.valueOf(event.getSectionIndex()));
		}
	};
}
