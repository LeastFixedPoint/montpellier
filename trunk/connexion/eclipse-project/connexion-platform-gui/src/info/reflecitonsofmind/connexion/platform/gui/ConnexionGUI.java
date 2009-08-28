package info.reflecitonsofmind.connexion.platform.gui;

import info.reflecitonsofmind.connexion.platform.gui.event.cts.ClientToServerMessageCoder;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.IClientToServerMessage;
import info.reflecitonsofmind.connexion.platform.gui.event.stc.IServerToClientMessage;
import info.reflecitonsofmind.connexion.platform.gui.event.stc.ServerToClientMessageCoder;
import info.reflecitonsofmind.connexion.platform.gui.host.HostGameFrame;
import info.reflectionsofmind.connexion.transport.ITransportFactory;
import info.reflectionsofmind.connexion.transport.dummy.DummyTransportFactory;
import info.reflectionsofmind.connexion.transport.jabber.JabberTransportFactory;
import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

public class ConnexionGUI
{
	private static final ICoder<IClientToServerMessage> CTS_MESSAGE_CODER = new ClientToServerMessageCoder();
	private static final ICoder<IServerToClientMessage> STC_MESSAGE_CODER = new ServerToClientMessageCoder();
	
	private final List<? extends ITransportFactory> transportFactories = Arrays.asList(//
			new JabberTransportFactory(), new DummyTransportFactory());
	
	public ConnexionGUI(final File configRoot)
	{
		
	}
	
	public void start()
	{
		try
		{
			JFrame.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
			
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					new HostGameFrame(new MainFrame(ConnexionGUI.this)).setVisible(true);
					// new MainFrame(ConnexionGUI.this).setVisible(true);
				}
			});
		}
		catch (final Exception exception)
		{
			JOptionPane.showMessageDialog(null, "Internal error", "Connexion", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}
	}
	
	public static void main(final String[] args)
	{
		new ConnexionGUI(null).start();
	}
	
	public List<? extends ITransportFactory> getTransportFactories()
	{
		return this.transportFactories;
	}
	
	public ICoder<IClientToServerMessage> getCTSMessageCoder()
	{
		return CTS_MESSAGE_CODER;
	}
	
	public ICoder<IServerToClientMessage> getSTCMessageCoder()
	{
		return STC_MESSAGE_CODER;
	}
}
