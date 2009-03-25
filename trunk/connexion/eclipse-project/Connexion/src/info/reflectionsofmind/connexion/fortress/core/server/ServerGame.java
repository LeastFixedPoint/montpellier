package info.reflectionsofmind.connexion.fortress.core.server;

import info.reflectionsofmind.connexion.fortress.core.common.AbstractGame;
import info.reflectionsofmind.connexion.fortress.core.common.GameUtil;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.action.AbstractAction;
import info.reflectionsofmind.connexion.fortress.core.common.action.EndTurnAction;
import info.reflectionsofmind.connexion.fortress.core.common.action.MeeplePlacementAction;
import info.reflectionsofmind.connexion.fortress.core.common.action.TilePlacementAction;
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
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.fortress.core.util.Looper;
import info.reflectionsofmind.connexion.platform.core.server.game.IServerGame;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerGame extends AbstractGame<ServerGame.IListener> implements IServerGame<ServerInitInfo, AbstractChange, AbstractAction, ServerGame.IListener>
{
	public final int MEEPLE_COUNT = 6;

	private ITileSequence tileSequence;
	private Tile currentTile;

	private final Looper<Player> playerLoop = new Looper<Player>(getPlayers());
	private Player currentPlayer;

	@Override
	public void start(final ServerInitInfo initInfo)
	{
		getPlayers().addAll(initInfo.getPlayers());
		this.tileSequence = initInfo.getTileSequence();

		try
		{
			getBoard().placeTile(this.tileSequence.getNextTile(), // 
					getBoard().getGeometry().getInitialLocation(), // 
					getBoard().getGeometry().getDirections().get(0));
		}
		catch (final TilePlacementException exception)
		{
			throw new RuntimeException(exception);
		}

		this.currentTile = this.tileSequence.getNextTile();

		this.playerLoop.reset();
		this.currentPlayer = this.playerLoop.next();
	}

	// ============================================================================================
	// === ACTION PROCESSING
	// ============================================================================================

	@Override
	public void onAction(final AbstractAction action)
	{
		if (action.getPlayer() != this.currentPlayer)
			throw new RuntimeException("Player " + action.getPlayer() + " tried to do " + action + " on turn of " + this.currentPlayer);
		action.dispatch(this);
	}

	public void onTilePlacement(final TilePlacementAction tileAction)
	{
		final TilePlacement placement = new TilePlacement(getBoard(), this.currentTile, tileAction.getLocation(), tileAction.getDirection());
		final PlacementAnalysis analysis = BoardUtil.getPlacementAnalysis(placement);
		if (analysis != PlacementAnalysis.CORRECT_PLACEMENT)
			throw new RuntimeException("Invalid tile placement (" + analysis + ") from action " + tileAction);

		try
		{
			getBoard().placeTile(this.currentTile, tileAction.getLocation(), tileAction.getDirection());
		}
		catch (final TilePlacementException exception)
		{
			throw new RuntimeException(exception);
		}

		this.currentTile = null;

		for (final IListener listener : getListeners())
			listener.onTilePlaced(this.currentPlayer, this.currentTile, tileAction.getLocation(), tileAction.getDirection());
	}

	public void onMeeplePlacement(final MeeplePlacementAction action)
	{
		try
		{
			getBoard().placeMeeple(action.getMeeple(), action.getSection());
		}
		catch (final MeeplePlacementException exception)
		{
			throw new RuntimeException(exception);
		}

		for (final IListener listener : getListeners())
			listener.onMeeplePlaced(this.currentPlayer, action.getMeeple());
	}

	public void onEndTurnAction(final EndTurnAction action)
	{
		this.currentPlayer = this.playerLoop.next();
		this.currentTile = this.tileSequence.getNextTile();
		scoreFeatures();

		for (final IListener listener : getListeners())
			listener.onTurnEnded();

		if (this.currentTile == null)
			for (final IListener listener : getListeners())
				listener.onFinished();
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

	public interface IListener extends IServerGame.IListener
	{
		void onTilePlaced(Player player, Tile tile, ILocation location, IDirection direction);

		void onMeeplePlaced(Player player, Meeple meeple);

		void onTurnEnded();
	}
}
