package info.reflectionsofmind.connexion.transport;

import info.reflectionsofmind.connexion.util.INamed;
import info.reflectionsofmind.connexion.util.form.Form;

public interface ITransportFactory extends INamed
{
	ITransport createTransport(Form connectionForm);

	Form newConnectionForm();
}
