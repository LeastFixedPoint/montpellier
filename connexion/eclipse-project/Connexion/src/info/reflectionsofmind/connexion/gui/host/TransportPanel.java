package info.reflectionsofmind.connexion.gui.host;

import info.reflectionsofmind.connexion.gui.common.TransportName;
import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.TransportException;
import info.reflectionsofmind.connexion.util.form.Form;
import info.reflectionsofmind.connexion.util.form.FormDialog;

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
		final Form form = this.transport.getForm();

		new FormDialog(this.transportsPanel.getHostGameFrame(), form, "Enable player type", "Enable")
		{
			@Override
			protected void onSubmit()
			{
				doOpen();
			}
		}.setVisible(true);
	}

	private void doOpen()
	{
		TransportPanel.this.openCloseButton.setAction(new CloseAction());

		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					TransportPanel.this.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Starting [" + TransportName.getName(TransportPanel.this.transport) + "] transport...");
					TransportPanel.this.transport.start();
					TransportPanel.this.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Transport started. Now accepting [" + TransportName.getName(TransportPanel.this.transport) + "] connections.");
				}
				catch (final TransportException exception)
				{
					TransportPanel.this.transportsPanel.getHostGameFrame().getChatPane().writeSystem("Cannot start transport.");
					exception.printStackTrace();
				}
			}
		}.start();
	}

	private void close()
	{
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
			open();
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
