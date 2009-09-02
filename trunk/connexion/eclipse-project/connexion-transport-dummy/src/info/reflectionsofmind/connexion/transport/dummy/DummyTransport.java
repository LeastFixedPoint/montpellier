package info.reflectionsofmind.connexion.transport.dummy;

import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.TransportNode;

public final class DummyTransport extends AbstractTransport
{
	private static int index = 0;
	private final int numberOfPlayers;
	private final int indexOfTransport;
	private boolean running = false;
	
	public DummyTransport(final Integer numberOfPlayers)
	{
		this.indexOfTransport = (++index);
		this.numberOfPlayers = numberOfPlayers;
		
		for (int i = 1; i < this.numberOfPlayers + 1; i++)
		{
			final int n = i;
			
			new Thread()
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
					
					if (DummyTransport.this.running)
					{
						firePacket(new TransportNode(DummyTransport.this, // 
								"p" + n + "@dt" + DummyTransport.this.indexOfTransport), "{" //
								+ " type: \"ParticipationRequest\"," //
								+ " name: \"" + "Player #" + DummyTransport.this.indexOfTransport + ":" + n + "\"" //
								+ "}");
					}
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
		this.running = true;
		fireStarted();
	}
	
	@Override
	public void stop()
	{
		this.running = false;
		fireStopped();
	}
	
	@Override
	public String getName()
	{
		return "Transport #" + this.indexOfTransport;
	}
	
}
