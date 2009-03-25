package info.reflectionsofmind.connexion.fortress.core.common.action;

import info.reflectionsofmind.connexion.fortress.core.common.Player;
import info.reflectionsofmind.connexion.fortress.core.common.board.Meeple;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Section;
import info.reflectionsofmind.connexion.fortress.core.server.ServerGame;

public final class MeeplePlacementAction extends AbstractAction
{
	private final Meeple meeple;
	private final Section section;

	private MeeplePlacementAction(Player player, Meeple meeple, Section section)
	{
		super(player);
		this.meeple = meeple;
		this.section = section;
	}

	@Override
	public void dispatch(ServerGame serverFortress)
	{
		serverFortress.onMeeplePlacement(this);
	}
	
	public Meeple getMeeple()
	{
		return this.meeple;
	}

	public Section getSection()
	{
		return this.section;
	}
}
