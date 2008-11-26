package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.local.client.gui.join.JoinGameFrame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class MainMenuFrame extends JFrame
{
	public MainMenuFrame()
	{
		super("Connexion");

		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new MigLayout("", "[240, center]", "[24]6[24]6[24]6[24]6[24]24[24]"));

		add(new JLabel("Connexion"), "span");

		add(new JButton("Host game"), "span, grow");

		add(new JButton(new AbstractAction("Join game")
		{
			@Override
			public void actionPerformed(final ActionEvent event)
			{
				new JoinGameFrame().setVisible(true);
			}
		}), "span, grow");

		add(new JButton("Play online"), "span, grow");

		add(new JButton("Configure"), "span, grow");

		add(new JButton("Exit"), "span");

		pack();
		setVisible(true);
	}
}
