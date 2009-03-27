package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.util.convert.CompositeDecoder;

public final class ChangeDecoder extends CompositeDecoder<AbstractChange>
{
	public ChangeDecoder(ClientGame game)
	{
		add(new TilePlacementChange.Decoder(game));
		add(new MeeplePlacementChange.Decoder(game));
		add(new NextTileChange.Decoder(game));
		add(new CurrentPlayerChange.Decoder(game));
	}
}
