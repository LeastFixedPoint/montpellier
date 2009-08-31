package info.reflectionsofmind.connexion.transport.dummy.gui;

import info.reflecitonsofmind.connexion.platform.gui.IOkCancelListener;
import info.reflecitonsofmind.connexion.platform.gui.OkCancelPanel;
import info.reflecitonsofmind.connexion.platform.gui.host.HostGameFrame;
import info.reflecitonsofmind.connexion.platform.gui.host.IAddTransportWizard;
import info.reflectionsofmind.connexion.transport.dummy.DummyConnectionParameters;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;

public class AddDummyTransportWizard implements IAddTransportWizard
{
	public DummyConnectionParameters execute(final HostGameFrame hostGameFrame)
	{
		final AddDummyTransportDialog dialog = new AddDummyTransportDialog(hostGameFrame);
		dialog.setVisible(true);
		
		if (dialog.getNumberOfPlayers() != null)
		{
			return new DummyConnectionParameters(dialog.getNumberOfPlayers());
		}
		else
		{
			return null;
		}
	}
	
	private final class AddDummyTransportDialog extends JDialog implements IOkCancelListener
	{
		private Integer numberOfPlayers = null;
		private final JSpinner spinner;
		
		public AddDummyTransportDialog(final Frame owner)
		{
			super(owner, "Add dummy transport", true);
			setLayout(new MigLayout("", "[][]", "[]"));
			
			add(new JLabel("Number of dummy players"));
			
			this.spinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
			add(this.spinner, "wrap");
			
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
			this.numberOfPlayers = (Integer) this.spinner.getModel().getValue();
			dispose();
		}
		
		public Integer getNumberOfPlayers()
		{
			return this.numberOfPlayers;
		}
	}
}
