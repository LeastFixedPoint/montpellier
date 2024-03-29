package info.reflecitonsofmind.connexion.platform.gui;

import info.reflecitonsofmind.connexion.platform.gui.connect.ServerInfo;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.ClientToServerMessageCoder;
import info.reflecitonsofmind.connexion.platform.gui.event.cts.IClientToServerMessage;
import info.reflecitonsofmind.connexion.platform.gui.event.stc.IServerToClientMessage;
import info.reflecitonsofmind.connexion.platform.gui.event.stc.ServerToClientMessageCoder;
import info.reflecitonsofmind.connexion.platform.gui.host.IAddTransportWizard;
import info.reflecitonsofmind.connexion.platform.gui.host.IHostGameGui;
import info.reflecitonsofmind.connexion.platform.gui.join.IJoinGameGui;
import info.reflectionsofmind.connexion.transport.ITransportFactory;
import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

public class ConnexionGUI
{
	private static final ICoder<IClientToServerMessage> CTS_MESSAGE_CODER = new ClientToServerMessageCoder();
	private static final ICoder<IServerToClientMessage> STC_MESSAGE_CODER = new ServerToClientMessageCoder();
	
	private final Map<ITransportFactory, IAddTransportWizard> addTransportWizards = new LinkedHashMap<ITransportFactory, IAddTransportWizard>();
	private final List<IHostGameGui> hostGameGuis = new ArrayList<IHostGameGui>();
	private final List<IJoinGameGui> joinGameGuis = new ArrayList<IJoinGameGui>();
	private final List<ServerInfo> serverList = new ArrayList<ServerInfo>();
	
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
					new MainFrame(ConnexionGUI.this).setVisible(true);
				}
			});
		}
		catch (final Exception exception)
		{
			JOptionPane.showMessageDialog(null, "Internal error", "Connexion", JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}
	}
	
	public List<? extends ITransportFactory> getTransportFactories()
	{
		return new ArrayList<ITransportFactory>(this.addTransportWizards.keySet());
	}
	
	public ICoder<IClientToServerMessage> getCTSMessageCoder()
	{
		return CTS_MESSAGE_CODER;
	}
	
	public ICoder<IServerToClientMessage> getSTCMessageCoder()
	{
		return STC_MESSAGE_CODER;
	}
	
	public Map<ITransportFactory, IAddTransportWizard> getAddTransportWizards()
	{
		return this.addTransportWizards;
	}
	
	public List<IHostGameGui> getHostGameGuis()
	{
		return this.hostGameGuis;
	}
	
	public List<IJoinGameGui> getJoinGameGuis()
	{
		return this.joinGameGuis;
	}
	
	public List<ServerInfo> getServerList()
	{
		return this.serverList;
	}
}
