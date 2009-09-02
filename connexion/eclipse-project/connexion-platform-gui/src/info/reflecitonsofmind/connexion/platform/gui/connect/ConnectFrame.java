package info.reflecitonsofmind.connexion.platform.gui.connect;

import info.reflecitonsofmind.connexion.platform.gui.IMainFrameReference;
import info.reflecitonsofmind.connexion.platform.gui.MainFrame;
import info.reflecitonsofmind.connexion.platform.gui.MessagePanel;
import info.reflecitonsofmind.connexion.platform.gui.TransportEventLogger;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.IClientToServerMessage;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.ParticipationRequest;
import info.reflecitonsofmind.connexion.platform.gui.event.stc.IServerToClientMessage;
import info.reflecitonsofmind.connexion.platform.gui.event.stc.ParticipationAccepted;
import info.reflecitonsofmind.connexion.platform.gui.join.IJoinGameGui;
import info.reflecitonsofmind.connexion.platform.gui.join.JoinGameFrame;
import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportNode;
import info.reflectionsofmind.connexion.util.convert.DecodingException;
import info.reflectionsofmind.connexion.util.convert.EncodingException;
import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;

public class ConnectFrame extends JFrame implements IMainFrameReference
{
	private final MainFrame mainFrame;
	private final IJoinGameGui joinGameGui;
	
	private final JComboBox serverCombo;
	private MessagePanel messagePanel;
	
	private final ITransport.IListener transportListener = new ParticipationAcceptedListener();
	
	private ITransport currentTransport = null;
	
	public ConnectFrame(final MainFrame mainFrame, final IJoinGameGui joinGameGui)
	{
		super("Connexion :: Connect to server");
		
		this.mainFrame = mainFrame;
		this.joinGameGui = joinGameGui;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new MigLayout("", "[max][][]", "[][]"));
		
		this.serverCombo = new JComboBox(getMainFrame().getGui().getServerList().toArray());
		
		add(this.serverCombo, "grow");
		add(new JButton(new ConnectAction()), "grow");
		add(new JButton("Edit server list"), "grow, wrap");
		add(this.messagePanel = new MessagePanel(this), "span, wrap");
		add(new JButton(new CloseAction()), "span, al right, wrap");
		
		pack();
		setSize(640, 400);
		setLocationRelativeTo(mainFrame);
	}
	
	public ITransport.IListener getTransportListener()
	{
		return this.transportListener;
	}
	
	public MainFrame getMainFrame()
	{
		return this.mainFrame;
	}
	
	private final class ParticipationAcceptedListener extends AbstractTransport.Listener
	{
		@Override
		public void onPacket(final TransportNode sender, final String contents)
		{
			try
			{
				final IServerToClientMessage message = getMainFrame().getGui().getSTCMessageCoder().decode(contents);
				
				if (message instanceof ParticipationAccepted)
				{
					ConnectFrame.this.messagePanel.addRawLine("Participation request accepted.");
					dispose();
					new JoinGameFrame(ConnectFrame.this.mainFrame, ConnectFrame.this.joinGameGui,
							ConnectFrame.this.currentTransport).setVisible(true);
				}
			}
			catch (final DecodingException exception)
			{
				throw new RuntimeException(exception);
			}
		}
	}
	
	private final class CloseAction extends AbstractAction
	{
		public CloseAction()
		{
			super("Close");
		}
		
		public void actionPerformed(final ActionEvent arg0)
		{
			if (ConnectFrame.this.currentTransport != null) ConnectFrame.this.currentTransport.stop();
			dispose();
			ConnectFrame.this.mainFrame.setVisible(true);
		}
	}
	
	private final class ConnectAction extends AbstractAction
	{
		private ConnectAction()
		{
			super("Connect");
		}
		
		public void actionPerformed(final ActionEvent e)
		{
			final ServerInfo server = (ServerInfo) ConnectFrame.this.serverCombo.getSelectedItem();
			ConnectFrame.this.currentTransport = server.getFactory().createTransport(server.getParameters());
			final TransportNode serverNode = new TransportNode(ConnectFrame.this.currentTransport, server.getAddress());
			
			ConnectFrame.this.currentTransport.addListener(ConnectFrame.this.getTransportListener());
			ConnectFrame.this.currentTransport.addListener(new TransportEventLogger(ConnectFrame.this.messagePanel));
			
			new SwingWorker<Void, Void>()
			{
				@Override
				protected Void doInBackground() throws Exception
				{
					ConnectFrame.this.currentTransport.start();
					
					try
					{
						final ICoder<IClientToServerMessage> coder = getMainFrame().getGui().getCTSMessageCoder();
						final String message = coder.encode(new ParticipationRequest("Player"));
						ConnectFrame.this.messagePanel.addRawLine("Sending participation request.");
						ConnectFrame.this.currentTransport.send(serverNode, message);
						ConnectFrame.this.messagePanel.addRawLine("Participation request sent, waiting for response.");
					}
					catch (final EncodingException exception)
					{
						throw new RuntimeException(exception);
					}
					
					return null;
				}
			}.execute();
		}
	}
}
