package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.transport.ITransport;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class TransportPanel extends JPanel
{
	private final JButton openCloseButton;
	private final TransportsPanel transportsPanel;
	private final ITransport transport;
	
	public TransportPanel(final TransportsPanel transportsPanel, final ITransport transport)
	{
		this.transportsPanel = transportsPanel;
		this.transport = transport;
		
		setLayout(new MigLayout("ins 0", "[]6[90]", "[]"));
		
		this.openCloseButton = new JButton(new OpenAction());
		add(this.openCloseButton, "grow");
		add(new JLabel(transport.getName()), "grow");
	}
	
	private void open()
	{
		this.openCloseButton.setAction(new CloseAction());
	}
	
	private void close()
	{
		this.openCloseButton.setAction(new OpenAction());
	}
	
	private final class OpenAction extends AbstractAction
	{
		public OpenAction()
		{
			super("Open...");
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			open();
		}
	}
	
	private final class CloseAction extends AbstractAction
	{
		public CloseAction()
		{
			super("Close");
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			close();
		}
	}
}
