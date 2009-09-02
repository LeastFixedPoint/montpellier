package info.reflecitonsofmind.connexion.platform.gui;

import info.reflecitonsofmind.connexion.platform.gui.host.Participant;
import info.reflectionsofmind.connexion.transport.ITransport;

public final class MessageFactory
{
	private MessageFactory()
	{
		throw new UnsupportedOperationException("Utility class");
	}
	
	public static String createParticipantAdded(final Participant participant)
	{
		return "Participant [<font color=red>" + participant.getName() + "</font>] connected " //
				+ "as [<font color=red>" + participant.getNode().getName() + "</font>].";
	}
	
	public static String createParticipantDisconnected(final Participant participant)
	{
		return "Participant [<font color=red>" + participant.getName() + "</font>] disconnected.";
	}
	
	public static String createParticipantKicked(final Participant participant)
	{
		return "Participant [<font color=red>" + participant.getName() + "</font>] aka " //
				+ "[<font color=red>" + participant.getNode().getName() + "</font>] was kicked by the server.";
	}
	
	public static String createTransportStopped(final ITransport transport)
	{
		return "Transport [<font color=blue>" + transport.getName() + "</font>] stopped.";
	}
	
	public static String createTransportStarted(final ITransport transport)
	{
		return "Transport [<font color=blue>" + transport.getName() + "</font>] started.";
	}
	
	public static String createTransportTrace(final ITransport transport, final String trace)
	{
		return "<font color=black>Transport [" + transport.getName() + "] traced: [" + trace + "].</font>";
	}
	
	public static String createTransportStarting(final ITransport transport)
	{
		return "Transport [<font color=blue>" + transport.getName() + "</font>] starting.";
	}
}
