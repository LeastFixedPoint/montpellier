package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.core.board.Meeple;
import info.reflectionsofmind.connexion.core.board.geometry.IDirection;
import info.reflectionsofmind.connexion.core.board.geometry.ILocation;
import info.reflectionsofmind.connexion.core.tile.Section;
import info.reflectionsofmind.connexion.server.remote.IRemoteClient;

public class ClientTurnEvent
{
	private final IRemoteClient client;

	private final IDirection direction;
	private final ILocation location;
	private final Meeple meeple;
	private final Section section;

	public ClientTurnEvent( //
			final IRemoteClient client, //
			final IDirection direction, //
			final ILocation location, //
			final Meeple meeple, //
			final Section section)
	{
		this.client = client;
		this.direction = direction;
		this.location = location;
		this.meeple = meeple;
		this.section = section;
	}

	public IDirection getDirection()
	{
		return this.direction;
	}

	public IRemoteClient getClient()
	{
		return this.client;
	}

	public ILocation getLocation()
	{
		return this.location;
	}

	public Meeple getMeeple()
	{
		return this.meeple;
	}

	public Section getSection()
	{
		return this.section;
	}

}
