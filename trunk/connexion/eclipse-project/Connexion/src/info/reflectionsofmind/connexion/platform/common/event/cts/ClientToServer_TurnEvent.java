package info.reflectionsofmind.connexion.platform.common.event.cts;

import info.reflectionsofmind.connexion.core.game.Turn;
import info.reflectionsofmind.connexion.platform.transport.IClientNode;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

public class ClientToServer_TurnEvent extends ClientToServerEvent
{
	public final static String PREFIX = ClientToServerEvent.EVENT_PREFIX + ":turn";

	private final Turn turn;

	public ClientToServer_TurnEvent(final Turn turn)
	{
		this.turn = turn;
	}
	
	@Override
	public void dispatch(IClientNode from, IClientToServerEventListener target)
	{
		target.onClientTurn(from, this);
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

	public final static ICoder<ClientToServer_TurnEvent> CODER = new AbstractCoder<ClientToServer_TurnEvent>()
	{
		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith(PREFIX);
		}

		@Override
		public ClientToServer_TurnEvent decode(final String string)
		{
			final String[] tokens = split(PREFIX, string);
			final Turn turn = Turn.CODER.decode(Util.decode(tokens[0]));
			return new ClientToServer_TurnEvent(turn);
		}

		@Override
		public String encode(final ClientToServer_TurnEvent event)
		{
			return join(PREFIX, Util.encode(Turn.CODER.encode(event.getTurn())));
		}
	};
}
