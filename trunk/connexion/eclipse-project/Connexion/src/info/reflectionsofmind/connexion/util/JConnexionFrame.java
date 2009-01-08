package info.reflectionsofmind.connexion.util;

import javax.swing.JFrame;

public abstract class JConnexionFrame extends JFrame implements IComponent
{
	private final IComponent component = new Component()
	{
	};
	
	public JConnexionFrame()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public final void configure(Configuration configuration)
	{
		this.component.configure(configuration);
	}

	public final <T extends IComponent> T createChild(String key, Class<T> componentClass)
	{
		return this.component.createChild(key, componentClass);
	}

	public final Configuration getConfiguration()
	{
		return this.component.getConfiguration();
	}
}
