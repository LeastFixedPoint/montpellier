package info.reflectionsofmind.connexion.fortress.core.common.change;

import info.reflectionsofmind.connexion.fortress.core.client.ClientGame;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;

public abstract class AbstractChange implements IChange
{
	public abstract void dispatch(ClientGame game);
}
