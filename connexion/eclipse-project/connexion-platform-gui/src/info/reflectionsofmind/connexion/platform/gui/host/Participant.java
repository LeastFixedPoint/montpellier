package info.reflecitonsofmind.connexion.platform.gui.host;

import info.reflectionsofmind.connexion.transport.TransportNode;

public final class Participant
{
	private final String name;
	private final TransportNode node;
	
	public Participant(final TransportNode node, final String name)
	{
		this.node = node;
		this.name = name;
	}
	
	public TransportNode getNode()
	{
		return this.node;
	}
	
	public String getName()
	{
		return this.name;
	}
}
