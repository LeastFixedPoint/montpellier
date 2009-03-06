package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.platform.client.IClient;
import info.reflectionsofmind.connexion.platform.common.DisconnectReason;
import info.reflectionsofmind.connexion.platform.common.Participant;
import info.reflectionsofmind.connexion.util.Util;

import javax.swing.JList;

public class PlayerList extends JList implements IClient.IListener
{
	private final JoinGameFrame joinGameFrame;
	
	public PlayerList(JoinGameFrame joinGameFrame)
	{
		this.joinGameFrame = joinGameFrame;
	}
	
	@Override
	public void onConnectionAccepted()
	{
		updateList();
	}
	
	@Override
	public void onAfterConnectionBroken(DisconnectReason reason)
	{
		updateList();
	}
	
	@Override
	public void onClientConnected(Participant client)
	{
		updateList();
	}
	
	@Override
	public void onClientDisconnected(Participant client)
	{
		updateList();
	}
	
	private void updateList()
	{
		setListData(Util.mapGetName(this.joinGameFrame.getClient().getParticipants()).toArray());
	}
	
	// ============================================================================================
	// === UNUSED
	// ============================================================================================

	@Override
	public void onChatMessage(Participant sender, String message)
	{
	}
	
	@Override
	public void onStart()
	{
	}
	
	@Override
	public void onTurn(Turn turn, String nextTileCode)
	{
	}
}
