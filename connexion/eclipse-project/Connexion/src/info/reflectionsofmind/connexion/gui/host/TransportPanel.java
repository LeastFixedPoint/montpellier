package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.gui.common.ChatPane;
import info.reflectionsofmind.connexion.platform.core.transport.IServerTransport;
import info.reflectionsofmind.connexion.platform.core.transport.IServerTransportFactory;
import info.reflectionsofmind.connexion.platform.core.transport.TransportException;
import info.reflectionsofmind.connexion.util.form.Form;
import info.reflectionsofmind.connexion.util.form.FormDialog;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

public class TransportPanel extends JPanel
{
	private final JButton openCloseButton;
	private final TransportsPanel transportsPanel;
	private final IServerTransportFactory transportFactory;
	private IServerTransport transport;

	public TransportPanel(final TransportsPanel transportsPanel, final IServerTransportFactory transportFactory)
	{
		this.transportsPanel = transportsPanel;
		this.transportFactory = transportFactory;

		setLayout(new MigLayout("ins 0", "[]6[90]", "[]"));

		this.openCloseButton = new JButton(new OpenAction());
		add(this.openCloseButton, "grow");
		add(new JLabel(transportFactory.getName()), "grow");
	}

	private void enableTransport()
	{
		final Form form = this.transportFactory.newConfigurationForm();

		new FormDialog(this.transportsPanel.getHostGameFrame(), form, "Enable player type", "Enable")
		{
			@Override
			protected void onSubmit()
			{
				doEnableTransport(form);
			}
		}.setVisible(true);
	}

	private void doEnableTransport(final Form form)
	{
		this.openCloseButton.getAction().setEnabled(false);
		this.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Starting " + ChatPane.format(this.transportFactory) + " transport...");

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				final TransportPanel panel = TransportPanel.this;

				try
				{
					panel.transport = panel.transportFactory.createTransport(form);
					panel.transport.addListener(panel.transportsPanel.getHostGameFrame().getServer());
					panel.transport.start();
					panel.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Transport " + ChatPane.format(panel.transport) + " started.");
					panel.openCloseButton.setAction(new CloseAction());
				}
				catch (final TransportException exception)
				{
					panel.transport = null;
					panel.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Cannot start transport.");
					exception.printStackTrace();
					panel.openCloseButton.setAction(new OpenAction());
				}
			}
		});
	}

	private void close()
	{
		this.openCloseButton.getAction().setEnabled(false);
		this.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Stopping [" + this.transportFactory.getName() + "] transport...");

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				TransportPanel.this.transport.stop();
				TransportPanel.this.transport = null;
				TransportPanel.this.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Transport [" + TransportPanel.this.transportFactory.getName() + "] stopped.");
				TransportPanel.this.openCloseButton.setAction(new OpenAction());
			}
		});
	}

	private final class OpenAction extends AbstractAction
	{
		public OpenAction()
		{
			super("Enable...");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			enableTransport();
		}
	}

	private final class CloseAction extends AbstractAction
	{
		public CloseAction()
		{
			super("Disable");
		}

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			close();
		}
	}
}
