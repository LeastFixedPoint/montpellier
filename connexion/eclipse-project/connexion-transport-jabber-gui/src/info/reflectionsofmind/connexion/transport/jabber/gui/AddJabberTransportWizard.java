package info.reflectionsofmind.connexion.transport.jabber.gui;

import info.reflectionsofmind.connexion.platform.gui.IOkCancelListener;
import info.reflectionsofmind.connexion.platform.gui.OkCancelPanel;
import info.reflectionsofmind.connexion.platform.gui.host.HostGameFrame;
import info.reflectionsofmind.connexion.platform.gui.host.IAddTransportWizard;
import info.reflectionsofmind.connexion.transport.jabber.JabberConnectionParameters;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class AddJabberTransportWizard implements IAddTransportWizard
{
	public JabberConnectionParameters execute(final HostGameFrame hostGameFrame)
	{
		final AddJabberTransportDialog dialog = new AddJabberTransportDialog(hostGameFrame);
		dialog.setVisible(true);
		return dialog.getParameters();
	}
	
	private final class AddJabberTransportDialog extends JDialog implements IOkCancelListener
	{
		private JabberConnectionParameters parameters = null;
		
		private final JTextField passwordField;
		private final JTextField portField;
		private final JTextField hostField;
		private final JTextField usernameField;
		private final JTextField resourceField;
		
		public AddJabberTransportDialog(final Frame owner)
		{
			super(owner, "Add jabber transport", true);
			setLayout(new MigLayout("", "[][180::, grow][][36::]", "[][][][][]"));
			
			add(new JLabel("Username"));
			add(this.usernameField = new JTextField("connexion-server"), "span, grow");
			
			add(new JLabel("Host"));
			add(this.hostField = new JTextField("binaryfreedom.info"), "grow");
			add(new JLabel(":"));
			add(this.portField = new JTextField("5222"), "wrap, grow");
			
			add(new JLabel("Password"));
			add(this.passwordField = new JTextField("connexion"), "span, grow");
			
			add(new JLabel("Resource"));
			add(this.resourceField = new JTextField("connexion"), "span, grow");
			
			add(new OkCancelPanel(this, "Add", "Cancel"), "span, al right");
			
			setResizable(false);
			pack();
			setLocationRelativeTo(owner);
		}
		
		public void onCancel()
		{
			dispose();
		}
		
		public void onOk()
		{
			this.parameters = new JabberConnectionParameters( //
					this.usernameField.getText(), //
					this.passwordField.getText(), //
					this.hostField.getText(), //
					this.resourceField.getText(), //
					Integer.valueOf(this.portField.getText()));
			
			dispose();
		}
		
		public JabberConnectionParameters getParameters()
		{
			return this.parameters;
		}
	}
}
