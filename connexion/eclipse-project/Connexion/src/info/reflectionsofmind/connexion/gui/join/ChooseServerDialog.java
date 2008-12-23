package info.reflectionsofmind.connexion.gui.join;

import java.awt.Dimension;

import info.reflectionsofmind.connexion.transport.ITransport;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class ChooseServerDialog extends JDialog
{
	private JComboBox serverCombo;

	public ChooseServerDialog(JoinGameFrame joinGameFrame, ITransport transport)
	{
		super(joinGameFrame, "Connexion :: Choose server", true);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new MigLayout("", "[240, grow]", "[]6[]6[]"));

		add(new JLabel("Choose server:"), "wrap");

		
		serverCombo = new JComboBox();
		add(serverCombo, "grow, wrap");
		
		add(new JButton("Ok"), "center");
		
		pack();
		setMinimumSize(new Dimension(getWidth() , getHeight()));
		setLocationRelativeTo(joinGameFrame);
	}
}
