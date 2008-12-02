package info.reflectionsofmind.connexion.local.server.slot;

import info.reflectionsofmind.connexion.event.cts.ClientToServerDecoder;
import info.reflectionsofmind.connexion.event.cts.ClientToServer_ClientConnectionRequestEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_ConnectionAcceptedEvent;
import info.reflectionsofmind.connexion.event.stc.ServerToClient_PlayerDisconnectedEvent;
import info.reflectionsofmind.connexion.local.server.ServerSideDisconnectReason;
import info.reflectionsofmind.connexion.local.server.ServerUtil;
import info.reflectionsofmind.connexion.local.server.transport.ISender;
import info.reflectionsofmind.connexion.local.server.transport.ITransport;
import info.reflectionsofmind.connexion.local.server.transport.JabberTransport;
import info.reflectionsofmind.connexion.local.server.transport.TransportException;

public class JabberSlot extends AbstractSlot implements ITransport.IListener
{
	private final static ClientToServerDecoder DECODER = new ClientToServerDecoder();

	private JabberTransport transport;
	private JabberTransport.Sender sender;

	@Override
	protected void doOpen()
	{
		for (ITransport transport : getServer().getTransports())
		{
			if (transport instanceof JabberTransport)
			{
				this.transport = (JabberTransport) transport;
				this.transport.addListener(this);
				
				try
				{
					this.transport.start();
					setState(State.OPEN);
				}
				catch (TransportException exception)
				{
					setError(exception);
				}
			}
		}
		
		setError(new Exception("Jabber was not found among this server's transports."));
	}
	
	@Override
	protected void doAccept()
	{
		try
		{
			final ServerToClient_ConnectionAcceptedEvent event = // 
				new ServerToClient_ConnectionAcceptedEvent(ServerUtil.getPlayerNames(getServer()));
			
			this.transport.send(sender, event.encode());
			setState(State.ACCEPTED);
		}
		catch (TransportException exception)
		{
			setError(exception);
		}
	}
	
	@Override
	protected void doSpectate()
	{
		throw new RuntimeException("Not supported yet.");
	}
	
	@Override
	protected void doClose()
	{
		try
		{
			this.transport.stop();
			setState(State.CLOSED);
		}
		catch (TransportException exception)
		{
			setError(exception);
		}
	}

	@Override
	protected void doDisconnect(ServerSideDisconnectReason reason)
	{
		try
		{
			final int index = ServerUtil.getPlayers(getServer()).indexOf(getPlayer());
			
			final ServerToClient_PlayerDisconnectedEvent event = // 
				new ServerToClient_PlayerDisconnectedEvent(index, reason);
			
			this.transport.send(sender, event.encode().getString());
			this.transport.stop();
			setState(State.CLOSED);
		}
		catch (TransportException exception)
		{
			setError(exception);
		}
	}
	
	@Override
	public ITransport getTransport()
	{
		return this.transport;
	}
	
	@Override
	public void onMessage(ISender sender, String message)
	{
		
	}
}
