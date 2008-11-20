package info.reflectionsofmind.connexion.client.gui;

import info.reflectionsofmind.connexion.client.gui.ClientUI.State;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public class GameInfoPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final ClientUI clientUI;

	private final JLabel tileCountLabel;

	private final JButton endTurnButton;

	private JLabel tileCountDescriptionLabel;

	public GameInfoPanel(final ClientUI clientUI)
	{
		this.clientUI = clientUI;

		setLayout(new MigLayout("", "[180]", "[12]6[max]6[30]"));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.tileCountLabel = new JLabel("??? / ???", SwingConstants.CENTER);
		this.tileCountLabel.setFont(this.tileCountLabel.getFont().deriveFont(48.0f));
		
		this.endTurnButton = new JButton(new EndTurnAction());
		this.endTurnButton.setEnabled(false);

		this.tileCountDescriptionLabel = new JLabel("Tiles count", SwingConstants.CENTER);
		
		add(tileCountDescriptionLabel, "span, grow");
		add(this.tileCountLabel, "span, grow");
		add(this.endTurnButton, "span, grow");
	}

	public ClientUI getClientUI()
	{
		return this.clientUI;
	}
	
	public void updateInterface()
	{
		if (getClientUI().getTurnMode() == State.PLACE_MEEPLE)
		{
			this.endTurnButton.setEnabled(true);
		}
		else
		{
			this.endTurnButton.setEnabled(false);
		}
		
		final Integer current = getClientUI().getClient().getGame().getBoard().getPlacements().size();
		final Integer total = getClientUI().getClient().getGame().getSequence().getTotalNumberOfTiles();
		
		if (total != null)
		{
			this.tileCountDescriptionLabel.setText("Tiles left");
			this.tileCountLabel.setText((total - current) + " / " + total);
		}
		else
		{
			this.tileCountDescriptionLabel.setText("Tiles placed");
			this.tileCountLabel.setText("" + current);
		}
	}
	
	private class EndTurnAction extends AbstractAction
	{
		public EndTurnAction()
		{
			super("End turn");
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			getClientUI().endTurn();
		}
	}
}
