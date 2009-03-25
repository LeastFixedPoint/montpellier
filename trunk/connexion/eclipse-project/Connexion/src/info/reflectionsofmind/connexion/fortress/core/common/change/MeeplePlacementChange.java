package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.Meeple;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Section;

public final class MeeplePlacementChange extends AbstractChange
{
	private final Player player;
	private final Meeple meeple;
	private final Section section;

	private MeeplePlacementChange(Player player, Meeple meeple, Section section)
	{
		this.player = player;
		this.meeple = meeple;
		this.section = section;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Meeple getMeeple()
	{
		return this.meeple;
	}

	public Section getSection()
	{
		return this.section;
	}
	
	@Override
	public void dispatch(ClientGame game)
	{
		game.onMeeplePlaced(this);
	}
}
