package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.common.Settings;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.transport.local.ClientLocalTransport.ServerToClientNode;
import info.reflectionsofmind.connexion.util.Form;
import info.reflectionsofmind.connexion.util.FormUtil;

public class ServerLocalTransport extends AbstractLocalTransport
{
	private static final String PARAMETER_NUMBER_OF_PLAYERS = "number-of-players";

	private final Form configuration = FormUtil.newBuilder() //
			.addInteger(PARAMETER_NUMBER_OF_PLAYERS, "Number of players") //
			.build();

	private final IApplication application;

	public ServerLocalTransport(final IApplication application)
	{
		this.application = application;
	}

	@Override
	public Form getForm()
	{
		return this.configuration;
	}

	@Override
	public boolean isServerSideOnly()
	{
		return true;
	}

	@Override
	public void send(final INode to, final String message) throws TransportException
	{
		new Thread()
		{
			@Override
			public void run()
			{
				final ClientLocalTransport clientTransport = ((ServerToClientNode) to).getClientTransport();
				clientTransport.receive(clientTransport.getServerNode(), message);
			}
		}.start();
	}

	@Override
	public void start() throws TransportException
	{
		final int number = FormUtil.getFieldById(this.configuration, PARAMETER_NUMBER_OF_PLAYERS).getInteger();

		for (int index = 0; index < number; index++)
		{
			new ClientLocalTransport(this.application, this, index + 1);
		}
	}
}
