package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.transport.IServerTransport.IClientNode;
import info.reflectionsofmind.connexion.transport.IServerTransport.IClientPacket;

public final class DefaultClientPacket implements IClientPacket
{
	private final IClientNode from;
	private final String contents;

	public DefaultClientPacket(IClientNode from, String contents)
	{
		this.from = from;
		this.contents = contents;
	}

	public IClientNode getFrom()
	{
		return this.from;
	}

	public String getContents()
	{
		return this.contents;
	}
}
