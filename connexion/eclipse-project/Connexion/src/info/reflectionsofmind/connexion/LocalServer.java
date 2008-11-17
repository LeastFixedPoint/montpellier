package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.core.board.InvalidLocationException;
import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.NotYourTurnException;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.SimpleTileGenerator;
import info.reflectionsofmind.connexion.core.game.Turn;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class LocalServer implements IServer
{
	private final List<IClient> clients = new ArrayList<IClient>();
	private final BiMap<IClient, Player> players = new HashBiMap<IClient, Player>();
	private boolean gameStarted = false;
	private Game game;

	@Override
	public void register(final IClient client)
	{
		if (this.gameStarted) throw new RuntimeException("Game already started.");
		this.clients.add(client);
	}

	@Override
	public void startGame() throws ServerException
	{
		this.gameStarted = true;

		final List<Player> players = new ArrayList<Player>();
		
		for (final IClient client : this.clients)
		{
			final Player player = new Player(client.getName());
			this.players.put(client, player);
			players.add(player);
		}

		try
		{
			final SimpleTileGenerator generator = new SimpleTileGenerator(
					getClass().getClassLoader().getResource("info/reflectionsofmind/connexion/tilelist/DefaultTileList.properties"));
			this.game = new Game(generator, players);
		}
		catch (Exception exception)
		{
			throw new ServerException(exception);
		}

		for (final IClient client : this.clients)
		{
			client.onStart(this.game, this.players.get(client));
		}
	}
	
	public List<IClient> getClients()
	{
		return this.clients;
	}

	@Override
	public void sendTurn(final Turn turn) throws InvalidLocationException, NotYourTurnException
	{
		this.game.doTurn(turn);
		
		for (IClient client : getClients())
		{
			client.onTurn(turn);
		}
	}
}
