package info.reflecitonsofmind.connexion.platform.gui.host;

import info.reflecitonsofmind.connexion.platform.gui.IMainFrameReference;
import info.reflecitonsofmind.connexion.platform.gui.MainFrame;
import info.reflecitonsofmind.connexion.platform.gui.MessageFactory;
import info.reflecitonsofmind.connexion.platform.gui.MessagePanel;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.DisconnectNotice;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.IClientToServerMessage;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.IClientToServerMessageDispatchTarget;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.ParticipationRequest;
import info.reflecitonsofmind.connexion.platform.gui.event.stc.KickNotice;
import info.reflecitonsofmind.connexion.platform.gui.event.stc.ParticipationAccepted;
import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportNode;
import info.reflectionsofmind.connexion.transport.ITransport.IListener;
import info.reflectionsofmind.connexion.util.convert.DecodingException;
import info.reflectionsofmind.connexion.util.convert.EncodingException;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

public class HostGameFrame extends JFrame implements IMainFrameReference
{
	private final IClientToServerMessageDispatchTarget dispatchTarget = new ClientToServerMessageDispatchTarget();
	private final TransportMessageDecoder transportMessageDecoder = new TransportMessageDecoder();
	private final IListener transportListener = new HostGameFrame.TransportEventLogger();
	
	private final MainFrame mainFrame;
	
	private final List<Participant> participants = new ArrayList<Participant>();
	
	private final ActiveTransportsPanel activeTransportsPanel;
	private final MessagePanel messagePanel;
	
	public HostGameFrame(final MainFrame mainFrame, final IHostGameGui gameGui)
	{
		super("Connexion :: Host game");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.mainFrame = mainFrame;
		
		setLayout(new MigLayout("", "[240!, grow 0][max]", "[][][]"));
		
		add(new AvailableTransportsPanel(this), "cell 0 0");
		
		add(gameGui.getServerGamePanelFactory().createGamePanel(this), "grow, cell 0 1");
		add(this.activeTransportsPanel = new ActiveTransportsPanel(this), "cell 0 2");
		add(this.messagePanel = new MessagePanel(this), "cell 1 0, spany, grow");
		
		pack();
		setSize(800, 600);
		setExtendedState(MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
	}
	
	public MainFrame getMainFrame()
	{
		return this.mainFrame;
	}
	
	public void onTransportAdded(final ITransport transport)
	{
		transport.addListener(this.activeTransportsPanel.getTransportListener());
		transport.addListener(this.transportMessageDecoder);
		transport.addListener(this.transportListener);
		this.messagePanel.addRawLine(MessageFactory.createTransportStarting(transport));
	}
	
	public void kick(final Participant participant)
	{
		try
		{
			final String message = getMainFrame().getGui().getSTCMessageCoder().encode(new KickNotice());
			participant.getNode().send(message);
			this.participants.remove(participant);
			this.activeTransportsPanel.removeParticipant(participant);
			HostGameFrame.this.messagePanel.addRawLine(MessageFactory.createParticipantKicked(participant));
		}
		catch (final EncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	private Participant getParticipantByTransport(final ITransport transport)
	{
		for (final Participant participant : this.participants)
			if (participant.getNode().getTransport() == transport) return participant;
		
		return null;
	}
	
	public final class ClientToServerMessageDispatchTarget implements IClientToServerMessageDispatchTarget
	{
		@Override
		public void onDisconnectNotice(final DisconnectNotice disconnectNotice, final TransportNode sender)
		{
			final Participant participant = getParticipantByTransport(sender.getTransport());
			HostGameFrame.this.participants.remove(participant);
			HostGameFrame.this.activeTransportsPanel.removeParticipant(participant);
			HostGameFrame.this.messagePanel.addRawLine(MessageFactory.createParticipantDisconnected(participant));
		}
		
		@Override
		public void onParticipationRequest(final ParticipationRequest participationRequest, final TransportNode sender)
		{
			final Participant participant = new Participant(sender, participationRequest.getName());
			HostGameFrame.this.participants.add(participant);
			HostGameFrame.this.activeTransportsPanel.addParticipant(participant);
			HostGameFrame.this.messagePanel.addRawLine(MessageFactory.createParticipantAdded(participant));
			
			try
			{
				sender.send(getMainFrame().getGui().getSTCMessageCoder().encode(new ParticipationAccepted()));
			}
			catch (final EncodingException exception)
			{
				throw new RuntimeException(exception);
			}
		}
	}
	
	private final class TransportMessageDecoder extends AbstractTransport.Listener
	{
		@Override
		public void onPacket(final TransportNode sender, final String contents)
		{
			try
			{
				final IClientToServerMessage message = getMainFrame().getGui().getCTSMessageCoder().decode(contents);
				message.dispatch(HostGameFrame.this.dispatchTarget, sender);
			}
			catch (final DecodingException e)
			{
				e.printStackTrace();
				return;
			}
		}
	}
	
	private final class TransportEventLogger extends AbstractTransport.Listener
	{
		@Override
		public void onStarted(final ITransport transport)
		{
			HostGameFrame.this.messagePanel.addRawLine(MessageFactory.createTransportStarted(transport));
		}
		
		@Override
		public void onStopped(final ITransport transport)
		{
			HostGameFrame.this.messagePanel.addRawLine(MessageFactory.createTransportStopped(transport));
		}
		
		@Override
		public void onTrace(final ITransport transport, final String trace)
		{
			HostGameFrame.this.messagePanel.addRawLine(MessageFactory.createTransportTrace(transport, trace));
		}
	}
}
