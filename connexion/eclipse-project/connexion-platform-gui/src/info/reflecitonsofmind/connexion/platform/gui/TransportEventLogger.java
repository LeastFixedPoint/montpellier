package info.reflecitonsofmind.connexion.platform.gui;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.ITransport;

public class TransportEventLogger extends AbstractTransport.Listener
{
	private final MessagePanel messagePanel;
	
	public TransportEventLogger(final MessagePanel messagePanel)
	{
		this.messagePanel = messagePanel;
	}
	
	@Override
	public void onStarted(final ITransport transport)
	{
		this.messagePanel.addRawLine(MessageFactory.createTransportStarted(transport));
	}
	
	@Override
	public void onStopped(final ITransport transport)
	{
		this.messagePanel.addRawLine(MessageFactory.createTransportStopped(transport));
	}
	
	@Override
	public void onTrace(final ITransport transport, final String trace)
	{
		this.messagePanel.addRawLine(MessageFactory.createTransportTrace(transport, trace));
	}
}
