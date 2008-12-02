package info.reflectionsofmind.connexion.event.cts;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

public class ClientToServer_TurnEvent extends ClientToServerEvent
{
	public final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":turn";
	public final static Coder CODER = new Coder();

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
	public String encode()
	{
		return CODER.encode(this);
	}
	
	public static class Coder extends AbstractCoder<ClientToServer_TurnEvent>
	{
		@Override
		public boolean accepts(String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ClientToServer_TurnEvent decode(String string)
		{
			final String[] tokens = split(PREFIX, string);
			final Turn turn = Turn.CODER.decode(Util.decode(tokens[0]));
			return new ClientToServer_TurnEvent(turn);
		}

		@Override
		public String encode(ClientToServer_TurnEvent event)
		{
			return join(PREFIX, Util.encode(Turn.CODER.encode(event.getTurn())));
		}
	}
}
