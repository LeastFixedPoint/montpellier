package info.reflecitonsofmind.connexion.platform.gui.host;

import info.reflecitonsofmind.connexion.platform.gui.ConnexionGUI;
import info.reflectionsofmind.connexion.transport.IConnectionParameters;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.ITransportFactory;
import info.reflectionsofmind.connexion.util.Util;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class AvailableTransportsPanel extends JPanel
{
	private final HostGameFrame hostGameFrame;
	
	public AvailableTransportsPanel(final HostGameFrame hostGameFrame)
	{
		this.hostGameFrame = hostGameFrame;
		setBorder(BorderFactory.createTitledBorder("Available transports"));
		
		final List<? extends ITransportFactory> factories = hostGameFrame.getMainFrame().getGui()
				.getTransportFactories();
		
		setLayout(new MigLayout("ins 3 6 6 6", "[36][max]", Util.copy(factories.size(), "[]")));
		
		for (final ITransportFactory factory : factories)
		{
			add(new JButton(new AddTransportAction(factory)));
			add(new JLabel(factory.getName()), "wrap");
		}
	}
	
	private final class AddTransportAction extends AbstractAction
	{
		private final ITransportFactory factory;
		
		public AddTransportAction(final ITransportFactory factory)
		{
			super("Add");
			this.factory = factory;
		}
		
		@Override
		public void actionPerformed(final ActionEvent event)
		{
			final ConnexionGUI gui = AvailableTransportsPanel.this.hostGameFrame.getMainFrame().getGui();
			final IConnectionParameters parameters = gui.getAddTransportWizards().get(AddTransportAction.this.factory)
					.execute(AvailableTransportsPanel.this.hostGameFrame);
			
			if (parameters != null)
			{
				new Thread()
				{
					@Override
					public void run()
					{
						final ITransport transport = AddTransportAction.this.factory.createTransport(parameters);
						AvailableTransportsPanel.this.hostGameFrame.onTransportAdded(transport);
						transport.start();
					}
				}.start();
			}
		}
	}
}
