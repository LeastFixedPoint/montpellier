package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.IConnectionParameters;
import info.reflectionsofmind.connexion.transport.ITransportFactory;

public class JabberTransportFactory implements ITransportFactory
{
	@Override
	public String getName()
	{
		return "Jabber/XMPP";
	}
	
	@Override
	public JabberTransport createTransport(final IConnectionParameters parameters)
	{
		return new JabberTransport((JabberConnectionParameters) parameters);
	}
}
