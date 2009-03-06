package info.reflectionsofmind.connexion.platform.transport;

import info.reflectionsofmind.connexion.IApplication;
import info.reflectionsofmind.connexion.util.INamed;
import info.reflectionsofmind.connexion.util.form.Form;

public interface IClientTransportFactory extends INamed
{
	IClientTransport createTransport(Form form) throws TransportException;
	IApplication getApplication();
	Form newConfigurationForm();
}
