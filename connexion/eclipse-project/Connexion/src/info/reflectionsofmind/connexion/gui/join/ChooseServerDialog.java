package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.gui.common.TransportName;
import info.reflectionsofmind.connexion.transport.INode;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public final class ChooseServerDialog extends JDialog
{
	private final JComboBox serverCombo;
	private INode serverNode;

	public ChooseServerDialog(final JoinGameFrame joinGameFrame, final ITransport transport)
	{
		super(joinGameFrame, "Connexion :: Choose server", true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLayout(new MigLayout("", "[240]", "[]6[]6[]"));

		add(new JLabel("Choose [" + TransportName.getName(transport) + "] server:"), "wrap");

		this.serverCombo = new JComboBox(new String[] { "connexion-server@binaryfreedom.info" });
		this.serverCombo.setEditable(true);
		add(this.serverCombo, "grow, wrap");

		add(new JButton(new AbstractAction("Connect")
		{
			@Override
			public void actionPerformed(final ActionEvent event)
			{
				if (serverCombo.getSelectedItem() != null)
				{
					ChooseServerDialog.this.serverNode = transport.getNode((String) serverCombo.getSelectedItem());
				}
				
				dispose();
			}
		}), "center, split");

		add(new JButton(new AbstractAction("Cancel")
		{
			@Override
			public void actionPerformed(final ActionEvent event)
			{
				dispose();
			}
		}), "center");

		pack();
		setLocationRelativeTo(joinGameFrame);
	}
	
	public INode getServerNode()
	{
		return this.serverNode;
	}
}
