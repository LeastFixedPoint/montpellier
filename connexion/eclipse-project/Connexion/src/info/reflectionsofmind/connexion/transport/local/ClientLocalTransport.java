package info.reflectionsofmind.connexion.transport.local;

import info.reflectionsofmind.connexion.common.Settings;
import info.reflectionsofmind.connexion.gui.join.JoinGameFrame;
import info.reflectionsofmind.connexion.transport.AbstractNode;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.util.Form;

import javax.swing.SwingUtilities;

public class ClientLocalTransport extends AbstractLocalTransport
{
	private final ServerToClientNode clientNode;
	private final ClientToServerNode serverNode;

	private final Settings settings;

	public ClientLocalTransport(final Settings settings, final ServerLocalTransport serverTransport, final int index)
	{
		this.settings = settings;

		this.clientNode = new ServerToClientNode(serverTransport, this);
		this.serverNode = new ClientToServerNode(this, serverTransport, index);

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				final JoinGameFrame joinGameFrame = new JoinGameFrame(ClientLocalTransport.this.settings);
				joinGameFrame.setVisible(true);
				joinGameFrame.connect(ClientLocalTransport.this.serverNode);
			}
		});
	}

	@Override
	public Form getForm()
	{
		return null;
	}

	@Override
	public void send(final INode to, final String message) throws TransportException
	{
		((ClientToServerNode) to).getServerTransport().receive(this.clientNode, message);
	}

	@Override
	public void start() throws TransportException
	{

	}

	public ClientToServerNode getServerNode()
	{
		return this.serverNode;
	}

	public ServerToClientNode getClientNode()
	{
		return this.clientNode;
	}

	public final class ClientToServerNode extends AbstractNode
	{
		private final ClientLocalTransport clientTransport;
		private final ServerLocalTransport serverTransport;
		private final int index;

		public ClientToServerNode(final ClientLocalTransport clientTransport, final ServerLocalTransport serverTransport, final int index)
		{
			this.index = index;
			this.clientTransport = clientTransport;
			this.serverTransport = serverTransport;
		}

		@Override
		public ITransport getTransport()
		{
			return this.clientTransport;
		}

		public ServerLocalTransport getServerTransport()
		{
			return this.serverTransport;
		}

		public String getId()
		{
			return "Local server";
		}

		public int getIndex()
		{
			return this.index;
		}
	}

	public final class ServerToClientNode extends AbstractNode
	{
		private final ClientLocalTransport clientTransport;
		private final ServerLocalTransport serverTransport;

		public ServerToClientNode(final ServerLocalTransport serverTransport, final ClientLocalTransport clientTransport)
		{
			this.clientTransport = clientTransport;
			this.serverTransport = serverTransport;
		}

		@Override
		public ITransport getTransport()
		{
			return this.serverTransport;
		}

		public ClientLocalTransport getClientTransport()
		{
			return this.clientTransport;
		}

		public String getId()
		{
			return "Local client";
		}
	}
}