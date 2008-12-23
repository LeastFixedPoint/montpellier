package info.reflectionsofmind.connexion.gui.join;

import info.reflectionsofmind.connexion.client.ILocalClient;
import info.reflectionsofmind.connexion.common.Client;
import info.reflectionsofmind.connexion.common.DisconnectReason;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.util.Util;

import javax.swing.JList;

public class PlayerList extends JList implements ILocalClient.IListener
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
	public void onConnectionBroken(DisconnectReason reason)
	{
		updateList();
	}
	
	@Override
	public void onClientConnected(Client client)
	{
		updateList();
	}
	
	@Override
	public void onClientDisconnected(Client client)
	{
		updateList();
	}
	
	private void updateList()
	{
		setListData(Util.mapGetName(this.joinGameFrame.getClient().getClients()).toArray());
	}
	
	// ============================================================================================
	// === UNUSED
	// ============================================================================================

	@Override
	public void onChatMessage(Client sender, String message)
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
