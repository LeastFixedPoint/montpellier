package info.reflectionsofmind.connexion.platform.gui;

import info.reflectionsofmind.connexion.IApplication;

import javax.swing.JFrame;

public abstract class JConnexionFrame extends JFrame
{
	private final IApplication application;

	public JConnexionFrame(IApplication application)
	{
		this.application = application;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public IApplication getApplication()
	{
		return this.application;
	}
}
