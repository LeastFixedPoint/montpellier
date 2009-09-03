package info.reflecitonsofmind.connexion.platform.gui.host;

import info.reflecitonsofmind.connexion.platform.gui.MainFrame;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.DisconnectNotice;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.IClientToServerMessage;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.IClientToServerMessageDispatchTarget;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.ParticipationRequest;
import info.reflecitonsofmind.connexion.platform.gui.event.stc.KickNotice;
import info.reflectionsofmind.connexion.transport.AbstractTransport;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportNode;
import info.reflectionsofmind.connexion.util.convert.DecodingException;
import info.reflectionsofmind.connexion.util.convert.EncodingException;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

public class HostGameFrame extends JFrame
{
	private final IClientToServerMessageDispatchTarget dispatchTarget = new ClientToServerMessageDispatchTarget();
	private final TransportMessageDecoder transportMessageDecoder = new TransportMessageDecoder();
	
	private final MainFrame mainFrame;
	private final List<Participant> participants = new ArrayList<Participant>();
	private final ActiveTransportsPanel activeTransportsPanel;
	
	public HostGameFrame(final MainFrame mainFrame)
	{
		super("Connexion :: Host game");
		
		this.mainFrame = mainFrame;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setLayout(new MigLayout("", "[240, fill][]", "[][fill]"));
		
		add(new AvailableTransportsPanel(this), "wrap");
		
		this.activeTransportsPanel = new ActiveTransportsPanel(this);
		add(this.activeTransportsPanel);
		
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
	}
	
	public void disconnect(final Participant participant)
	{
		try
		{
			final String message = getMainFrame().getGui().getSTCMessageCoder().encode(new KickNotice());
			participant.getNode().send(message);
			this.participants.remove(participant);
			this.activeTransportsPanel.removeParticipant(participant);
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
		}
		
		@Override
		public void onParticipationRequest(final ParticipationRequest participationRequest, final TransportNode sender)
		{
			final Participant participant = new Participant(sender, participationRequest.getName());
			HostGameFrame.this.participants.add(participant);
			HostGameFrame.this.activeTransportsPanel.addParticipant(participant);
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
}
