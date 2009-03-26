package info.reflectionsofmind.connexion.platform.core.server.game;

import info.reflectionsofmind.connexion.platform.core.common.game.IAction;
import info.reflectionsofmind.connexion.platform.core.common.game.IChange;
import info.reflectionsofmind.connexion.util.form.Form;

public interface IServerGameFactory
{
	IServerGame<IChange, IAction, IServerGame.IListener> createServerGame(Form form);

	Form newConfigurationForm();
}
