package info.reflectionsofmind.connexion.platform.core.transport;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.util.INamed;
import info.reflectionsofmind.connexion.util.form.Form;

public interface IClientTransportFactory extends INamed
{
	IClientToServerTransport createTransport(Form form) throws TransportException;
	IApplication getApplication();
	Form newConfigurationForm();
}
