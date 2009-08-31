package info.reflectionsofmind.connexion.transport.dummy;

import info.reflectionsofmind.connexion.transport.IConnectionParameters;

public class DummyConnectionParameters implements IConnectionParameters
{
	private final int numberOfPlayers;
	
	public DummyConnectionParameters(final int numberOfPlayers)
	{
		this.numberOfPlayers = numberOfPlayers;
	}
	
	public int getNumberOfPlayers()
	{
		return this.numberOfPlayers;
	}
}
