package info.reflectionsofmind.connexion.fortress.core.common.action;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.BoardUtil;
import info.reflectionsofmind.connexion.fortress.core.common.board.Meeple;
import info.reflectionsofmind.connexion.fortress.core.common.board.TilePlacement;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.ILocation;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.Location;
import info.reflectionsofmind.connexion.fortress.core.common.board.geometry.rectangular.RectangularGeometry;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Section;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;
import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.convert.AbstractCoder;

public final class MeeplePlacementAction extends AbstractAction
{
	private final Meeple meeple;
	private final Section section;

	private MeeplePlacementAction(final Player player, final Meeple meeple, final Section section)
	{
		super(player);
		this.meeple = meeple;
		this.section = section;
	}

	@Override
	public void dispatch(final ServerGame serverFortress)
	{
		serverFortress.onMeeplePlacement(this);
	}

	@Override
	public Player getPlayer()
	{
		return (Player) super.getPlayer();
	}

	public Meeple getMeeple()
	{
		return this.meeple;
	}

	public Section getSection()
	{
		return this.section;
	}

	public static final class Coder extends AbstractCoder<MeeplePlacementAction>
	{
		private final ClientGame game;

		public Coder(final ClientGame game)
		{
			this.game = game;
		}

		@Override
		public boolean accepts(final String string)
		{
			return string.startsWith("place-meeple#");
		}

		@Override
		public MeeplePlacementAction decode(final String string)
		{
			final String[] strings = string.split("#");

			final Integer playerIndex = Util.strToInt(strings[1]);
			final Player player = playerIndex == null ? null : this.game.getPlayers().get(playerIndex);
			final Meeple meeple = player.getMeeples().get(Util.strToInt(strings[2]));
			final RectangularGeometry geometry = (RectangularGeometry) this.game.getBoard().getGeometry();
			final ILocation location = geometry.newLocation(Util.strToInt(strings[3]), Util.strToInt(strings[4]));
			final TilePlacement placement = BoardUtil.getPlacementAt(this.game.getBoard(), location);
			final Section section = placement.getTile().getSections().get(Util.strToInt(strings[5]));

			return new MeeplePlacementAction(player, meeple, section);
		}

		@Override
		public String encode(final MeeplePlacementAction action)
		{
			final StringBuilder builder = new StringBuilder();

			builder.append("place-meeple");
			builder.append("#").append(this.game.getPlayers().indexOf(action.getPlayer()));
			builder.append("#").append(action.getPlayer().getMeeples().indexOf(action.getMeeple()));

			final TilePlacement placement = BoardUtil.getPlacementOf(this.game.getBoard(), action.getSection().getTile());
			builder.append("#").append(((Location) placement.getLocation()).getX());
			builder.append("#").append(((Location) placement.getLocation()).getY());
			builder.append("#").append(placement.getTile().getSections().indexOf(action.getSection()));

			return builder.toString();
		}
	}

}
