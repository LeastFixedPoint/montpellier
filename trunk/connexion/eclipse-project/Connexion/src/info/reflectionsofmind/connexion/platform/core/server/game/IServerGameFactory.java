package info.reflectionsofmind.connexion.platform.core.server.game;

import info.reflectionsofmind.connexion.util.form.Form;

public interface IServerGameFactory
{
	IServerGame createServerGame(Form form);

	Form newConfigurationForm();
}
