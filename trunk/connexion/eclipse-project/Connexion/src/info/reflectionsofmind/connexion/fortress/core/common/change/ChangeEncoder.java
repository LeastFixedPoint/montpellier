package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.util.convert.CompositeEncoder;

public final class ChangeEncoder extends CompositeEncoder<AbstractChange>
{
	public ChangeEncoder(ServerGame game)
	{
		add(TilePlacementChange.class, new TilePlacementChange.Encoder(game));
		add(MeeplePlacementChange.class, new MeeplePlacementChange.Encoder(game));
		add(NextTileChange.class, new NextTileChange.Encoder(game));
		add(CurrentPlayerChange.class, new CurrentPlayerChange.Encoder(game));
	}
}
