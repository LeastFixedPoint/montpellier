package info.reflecitonsofmind.connexion.platform.gui.join;

import info.reflecitonsofmind.connexion.platform.gui.IMainFrameReference;
import info.reflecitonsofmind.connexion.platform.gui.MainFrame;
import info.reflectionsofmind.connexion.transport.ITransport;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

public class JoinGameFrame extends JFrame implements IMainFrameReference
{
	private final MainFrame mainFrame;
	
	public JoinGameFrame(final MainFrame mainFrame, final IJoinGameGui joinGameGui, final ITransport transport)
	{
		super("Connexion :: Join game");
		
		this.mainFrame = mainFrame;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setLayout(new MigLayout("", "[240!, grow 0][max]", "[][][]"));
		
		// add(new AvailableTransportsPanel(this), "cell 0 0");
		//		
		// add(gameGui.getServerGamePanelFactory().createHostGamePanel(this),
		// "grow, cell 0 1");
		// add(this.activeTransportsPanel = new ActiveTransportsPanel(this),
		// "cell 0 2");
		// add(this.messagePanel = new MessagePanel(this),
		// "cell 1 0, spany, grow");
		
		pack();
		setSize(800, 600);
		// setExtendedState(MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		
	}
	
	public MainFrame getMainFrame()
	{
		return this.mainFrame;
	}
}
