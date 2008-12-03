package info.reflectionsofmind.connexion.remote.client;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.core.tile.Tile;
import info.reflectionsofmind.connexion.local.server.DisconnectReason;
import info.reflectionsofmind.connexion.local.server.exception.ClientConnectionException;
import info.reflectionsofmind.connexion.transport.IAddressee;
import info.reflectionsofmind.connexion.transport.ITransport;

import java.util.List;

public class RemoteJabberClient extends AbstractRemoteClient
{
	
	public RemoteJabberClient(ITransport<IAddressee> transport, IAddressee clientAddressee)
	{
		super(transport, clientAddressee);
	}

	@Override
	public String getName()
	{
		return null;
	}
}
