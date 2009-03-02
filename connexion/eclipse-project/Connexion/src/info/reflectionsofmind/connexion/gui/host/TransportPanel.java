package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.transport.IServerTransport;
import info.reflectionsofmind.connexion.transport.IServerTransportFactory;
import info.reflectionsofmind.connexion.transport.TransportException;
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
		TransportPanel.this.openCloseButton.getAction().setEnabled(true);

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					TransportPanel.this.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Starting [" + TransportPanel.this.transportFactory.getName() + "] transport...");
					TransportPanel.this.transport = TransportPanel.this.transportFactory.createTransport(form);
					TransportPanel.this.transport.start();
					TransportPanel.this.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Transport started. Now accepting [" + TransportPanel.this.transportFactory.getName() + "] connections.");
					TransportPanel.this.openCloseButton.setAction(new CloseAction());
				}
				catch (final TransportException exception)
				{
					TransportPanel.this.transport = null;
					TransportPanel.this.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Cannot start transport.");
					exception.printStackTrace();
					TransportPanel.this.openCloseButton.setAction(new OpenAction());
				}
			}
		});
	}

	private void close()
	{
		this.transport.stop();
		this.transport = null;
		this.openCloseButton.setAction(new OpenAction());
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
