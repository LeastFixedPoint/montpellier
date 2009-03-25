package info.reflectionsofmind.connexion.fortress.core.client;

import info.reflectionsofmind.connexion.fortress.core.common.AbstractGame;
import info.reflectionsofmind.connexion.fortress.core.common.GameUtil;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.action.AbstractAction;
import info.reflectionsofmind.connexion.fortress.core.common.board.BoardUtil;
import info.reflectionsofmind.connexion.fortress.core.common.board.Feature;
import info.reflectionsofmind.connexion.fortress.core.common.board.Meeple;
import info.reflectionsofmind.connexion.fortress.core.common.board.TilePlacement;
import info.reflectionsofmind.connexion.fortress.core.common.board.BoardUtil.PlacementAnalysis;
import info.reflectionsofmind.connexion.fortress.core.common.board.exception.MeeplePlacementException;
import info.reflectionsofmind.connexion.fortress.core.common.board.exception.TilePlacementException;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.IDirection;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.ILocation;
import info.reflectionsofmind.connexion.fortress.core.common.change.AbstractChange;
import info.reflectionsofmind.connexion.fortress.core.common.change.CurrentPlayerChange;
import info.reflectionsofmind.connexion.fortress.core.common.change.MeeplePlacementChange;
import info.reflectionsofmind.connexion.fortress.core.common.change.NextTileChange;
import info.reflectionsofmind.connexion.fortress.core.common.change.TilePlacementChange;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.fortress.core.util.Looper;
import info.reflectionsofmind.connexion.platform.core.client.game.IClientGame;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientGame extends AbstractGame<ClientGame.IListener> implements IClientGame<ClientInitInfo, AbstractAction, AbstractChange, ClientGame.IListener>
{
	public final int MEEPLE_COUNT = 6;

	private Tile currentTile;

	private final Looper<Player> playerLoop = new Looper<Player>(getPlayers());
	private Player currentPlayer;

	@Override
	public void start(final ClientInitInfo initInfo)
	{
		getPlayers().addAll(initInfo.getPlayers());
		this.currentTile = initInfo.getFirstTile();
		this.playerLoop.reset();
		this.currentPlayer = this.playerLoop.next();
	}

	// ============================================================================================
	// === CHANGE PROCESSING
	// ============================================================================================

	@Override
	public void onChange(final AbstractChange change)
	{
		change.dispatch(this);
	}

	public void onTilePlaced(final TilePlacementChange change)
	{
		final TilePlacement placement = new TilePlacement(getBoard(), change.getTile(), change.getLocation(), change.getDirection());
		final PlacementAnalysis analysis = BoardUtil.getPlacementAnalysis(placement);
		if (analysis != PlacementAnalysis.CORRECT_PLACEMENT)
			throw new RuntimeException("Invalid tile placement (" + analysis + ") from change " + change);

		try
		{
			getBoard().placeTile(change.getTile(), change.getLocation(), change.getDirection());
		}
		catch (final TilePlacementException exception)
		{
			throw new RuntimeException(exception);
		}

		this.currentTile = null;

		for (final IListener listener : getListeners())
			listener.onTilePlaced(change.getPlayer(), change.getTile(), change.getLocation(), change.getDirection());
	}

	public void onMeeplePlaced(final MeeplePlacementChange change)
	{
		try
		{
			getBoard().placeMeeple(change.getMeeple(), change.getSection());
		}
		catch (final MeeplePlacementException exception)
		{
			throw new RuntimeException(exception);
		}

		for (final IListener listener : getListeners())
			listener.onMeeplePlaced(change.getPlayer(), change.getMeeple());
	}

	public void onNextTileChanged(final NextTileChange change)
	{
		this.currentTile = change.getNextTile();
	}

	public void onCurrentPlayerChanged(final CurrentPlayerChange change)
	{
		this.currentPlayer = change.getNextPlayer();
		scoreFeatures();
	}

	// ============================================================================================
	// === SCORING
	// ============================================================================================

	private void scoreFeatures()
	{
		final Set<Feature> scoredFeatures = new HashSet<Feature>();

		for (final Meeple meeple : getBoard().getMeeples())
		{
			final Feature feature = BoardUtil.getFeatureOf(getBoard(), getBoard().getMeepleSection(meeple));

			if (feature.isCompleted())
			{
				scoredFeatures.add(feature);
			}
		}

		for (final Feature feature : scoredFeatures)
		{
			scoreFeature(feature);
		}
	}

	private void scoreFeature(final Feature feature)
	{
		final Map<Player, List<Meeple>> meeplesByPlayer = GameUtil.getMeeplesByPlayer(this, feature);

		int max = 0;

		for (final Player player : meeplesByPlayer.keySet())
		{
			max = Math.max(max, meeplesByPlayer.get(player).size());
		}

		for (final Player player : meeplesByPlayer.keySet())
		{
			if (meeplesByPlayer.get(player).size() == max)
			{
				player.addScore(feature.getCompletedScore());
			}
		}

		for (final Meeple meeple : BoardUtil.getMeeplesOnFeature(getBoard(), feature))
		{
			getBoard().removeMeeple(meeple);
		}
	}

	// ============================================================================================
	// === GETTERS
	// ============================================================================================

	public Tile getCurrentTile()
	{
		return this.currentTile;
	}
	
	public Player getCurrentPlayer()
	{
		return this.currentPlayer;
	}
	
	public interface IListener extends IClientGame.IListener
	{
		void onNextTileChanged(Tile newNextTile);

		void onCurrentPlayerChanged(Player newCurrentPlayer);

		void onTilePlaced(Player player, Tile tile, ILocation location, IDirection direction);

		void onMeeplePlaced(Player player, Meeple meeple);

		void onTurnEnded();
	}
}
