package info.reflecitonsofmind.connexion.platform.gui.host;

import info.reflecitonsofmind.connexion.platform.gui.host.HostGameFrame;
import info.reflectionsofmind.connexion.transport.IConnectionParameters;

public interface IAddTransportWizard
{
	IConnectionParameters execute(HostGameFrame hostGameFrame);
}
