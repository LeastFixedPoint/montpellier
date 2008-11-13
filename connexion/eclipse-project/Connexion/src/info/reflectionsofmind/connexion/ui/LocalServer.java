package info.reflectionsofmind.connexion.ui;

import info.reflectionsofmind.connexion.core.game.Game;
import info.reflectionsofmind.connexion.core.game.Player;
import info.reflectionsofmind.connexion.core.game.Turn;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class LocalServer implements IServer
{
	private final List<IClient> clients = new ArrayList<IClient>();
	private final BiMap<IClient, Player> players = new HashBiMap<IClient, Player>();
	private Game game;
	private boolean gameStarted = false;

	@Override
	public void register(final IClient client)
	{
		if (this.gameStarted) throw new RuntimeException("Game already started.");
		this.clients.add(client);
	}

	@Override
	public void startGame()
	{
		this.gameStarted = true;
		
		this.game = new Game();
		
		for (IClient client : clients)
		{
			final Player player = new Player(client.getName());	
			this.players.put(client, player);
			game.addPlayer(player);
		}

		for (IClient client : clients)
		{
			client.onStart(game, players.get(client));
		}
	}

	@Override
	public void sendTurn(final Turn turn)
	{
		if (turn.getPlayer() != game.getCurrentPlayer())
		{
			throw new RuntimeException("It's not your turn!");
		}
	}
}
