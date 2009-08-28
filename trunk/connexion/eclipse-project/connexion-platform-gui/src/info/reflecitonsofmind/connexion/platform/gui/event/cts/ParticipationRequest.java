package info.reflecitonsofmind.connexion.platform.gui.event.cts;

import info.reflectionsofmind.connexion.transport.TransportNode;

public class ParticipationRequest implements IClientToServerMessage
{
	private final String name;
	
	public ParticipationRequest(final String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	@Override
	public void dispatch(final IClientToServerMessageDispatchTarget target, final TransportNode sender)
	{
		target.onParticipationRequest(this, sender);
	}
}
