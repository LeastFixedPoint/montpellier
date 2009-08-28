package info.reflectionsofmind.connexion.transport.dummy;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.TransportNode;

public final class DummyTransport extends AbstractTransport
{
	private static int index = 0;
	private final int numberOfPlayers;
	private final int indexOfTransport;
	
	public DummyTransport(final String string)
	{
		this.indexOfTransport = (++index);
		this.numberOfPlayers = Integer.parseInt(string);
		
		for (int i = 1; i < this.numberOfPlayers + 1; i++)
		{
			final int n = i;
			
			new Thread("Player #" + this.indexOfTransport + ":" + n)
			{
				@Override
				public void run()
				{
					try
					{
						sleep(n * 1000);
					}
					catch (final InterruptedException e)
					{
						e.printStackTrace();
					}
					
					firePacket(new TransportNode(DummyTransport.this, getName()), "{" //
							+ " type: \"ParticipationRequest\"," //
							+ " name: \"" + getName() + "\"" //
							+ "}");
				}
			}.start();
		}
	}
	
	@Override
	public void send(final TransportNode recipient, final String contents)
	{
		
	}
	
	@Override
	public void start()
	{
		fireStarted();
	}
	
	@Override
	public void stop()
	{
		fireStopped();
	}
	
	@Override
	public String getName()
	{
		return "Transport #" + this.indexOfTransport;
	}
	
}
