package info.reflectionsofmind.connexion.local.server.slot;

import info.reflectionsofmind.connexion.hotseat.HotSeatTransport;
import info.reflectionsofmind.connexion.local.client.DefaultGuiClient;
import info.reflectionsofmind.connexion.local.server.ServerSideDisconnectReason;

import org.jivesoftware.smack.util.StringUtils;

public class HotSeatSlot extends AbstractSlot
{
	private HotSeatTransport transport;

	@Override
	protected void doOpen()
	{
		setState(State.OPEN);

		this.transport = new HotSeatTransport();
		this.transport.setServer(getServer());

		final DefaultGuiClient client = new DefaultGuiClient(this.transport.getRemoteServer(), StringUtils.randomString(8));
		this.transport.setClient(client);
		
		setClient(transport.getRemoteClient());
		
		setState(State.CONNECTED);
		fireConnected();
	}

	@Override
	protected void doClose()
	{
		throw new IllegalStateException();
	}
	
	@Override
	protected void doDisconnect(ServerSideDisconnectReason reason)
	{
		this.transport = null;
		setClient(null);
		accept(null);
	}
}
