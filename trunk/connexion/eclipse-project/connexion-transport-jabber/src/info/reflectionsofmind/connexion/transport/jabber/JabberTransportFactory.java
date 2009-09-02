package info.reflectionsofmind.connexion.transport.jabber;

import info.reflectionsofmind.connexion.transport.IConnectionParameters;
import info.reflectionsofmind.connexion.transport.ITransportFactory;

public class JabberTransportFactory implements ITransportFactory
{
	public String getName()
	{
		return "Jabber/XMPP";
	}
	
	public JabberTransport createTransport(final IConnectionParameters parameters)
	{
		return new JabberTransport((JabberConnectionParameters) parameters);
	}
	
	public IConnectionParameters fromString(final String parameters)
	{
		final String[] tokens = parameters.split(" ");
		return new JabberConnectionParameters(tokens[0], tokens[1], tokens[2], tokens[3], Integer.valueOf(tokens[4]));
	}
}
