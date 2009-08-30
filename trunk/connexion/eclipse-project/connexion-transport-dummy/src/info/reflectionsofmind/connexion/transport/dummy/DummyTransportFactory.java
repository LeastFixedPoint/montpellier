package info.reflectionsofmind.connexion.transport.dummy;

import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.ITransportFactory;
import info.reflectionsofmind.connexion.util.form.Form;
import info.reflectionsofmind.connexion.util.form.FormUtil;

public final class DummyTransportFactory implements ITransportFactory
{
	public ITransport createTransport(Form form)
	{
		return new DummyTransport(FormUtil.getIntegerById(form, "numberOfPlayers"));
	}
	
	public Form newConnectionForm()
	{
		Form form = new Form();
		form.addField(form.new IntField("numberOfPlayers", "Number of Players", 3));
		return form;
	}
	
	@Override
	public String getName()
	{
		return "Dummy";
	}
}
