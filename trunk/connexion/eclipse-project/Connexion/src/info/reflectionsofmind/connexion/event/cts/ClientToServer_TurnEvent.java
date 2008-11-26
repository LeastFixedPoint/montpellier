package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.util.convert.AbstractMessage;

public class ClientToServer_TurnEvent extends ClientToServerEvent<ClientToServer_TurnEvent>
{
	private final Turn turn;

	public ClientToServer_TurnEvent(final Turn turn)
	{
		this.turn = turn;
	}

	public Turn getTurn()
	{
		return this.turn;
	}
	
	@Override
	public IMessage<ClientToServer_TurnEvent> encode()
	{
		return new AbstractMessage<ClientToServer_TurnEvent>(PREFIX + ":turn", turn.encode().getString())
		{
			@Override
			public ClientToServer_TurnEvent decode()
			{
				return new ClientToServer_TurnEvent(new Turn.Message(getTokens()[0]).decode());
			}
		};
	}
}
