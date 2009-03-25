package info.reflectionsofmind.connexion.fortress.core.common;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.action.AbstractAction;
import info.reflectionsofmind.connexion.fortress.core.common.action.EndTurnAction;
import info.reflectionsofmind.connexion.fortress.core.common.action.MeeplePlacementAction;
import info.reflectionsofmind.connexion.fortress.core.common.action.TilePlacementAction;
import info.reflectionsofmind.connexion.fortress.core.common.change.AbstractChange;
import info.reflectionsofmind.connexion.fortress.core.common.change.CurrentPlayerChange;
import info.reflectionsofmind.connexion.fortress.core.common.change.MeeplePlacementChange;
import info.reflectionsofmind.connexion.fortress.core.common.change.NextTileChange;
import info.reflectionsofmind.connexion.fortress.core.common.change.TilePlacementChange;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;
import info.reflectionsofmind.connexion.util.convert.ICoder;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractFortressCoder<T> extends AbstractCoder<T>
{
	@SuppressWarnings("unchecked")
	private final List<AbstractCoder<? extends AbstractAction>> actionCoders;
	private final List<AbstractCoder<? extends AbstractChange>> changeCoders;

	@SuppressWarnings("unchecked")
	public AbstractFortressCoder(ClientGame game)
	{
		this.actionCoders = Arrays.asList( //
				new EndTurnAction.Coder(game), //
				new TilePlacementAction.Coder(game), //
				new MeeplePlacementAction.Coder(game));

		this.changeCoders = Arrays.asList( //
				new TilePlacementChange.Coder(game), // 
				new MeeplePlacementChange.Coder(game), //
				new NextTileChange.Coder(game), //
				new CurrentPlayerChange.Coder(game));
	}

	public IChange decodeChange(String string)
	{
		for (ICoder<? extends AbstractChange> coder : this.changeCoders)
			if (coder.accepts(string)) return coder.decode(string);

		return null;
	}

	public IChange encodeChange(String string)
	{
		for (ICoder<? extends AbstractChange> coder : this.changeCoders)
			if (coder.accepts(string)) return coder.decode(string);

		return null;
	}
}
