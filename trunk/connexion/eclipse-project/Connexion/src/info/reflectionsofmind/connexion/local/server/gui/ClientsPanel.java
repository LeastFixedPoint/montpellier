package info.reflectionsofmind.connexion.local.server.gui;

import info.reflectionsofmind.connexion.util.Util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public class ClientsPanel extends JPanel
{
	private final HostGameWindow window;

	private final List<ClientPanel> panels = new ArrayList<ClientPanel>();
	
	public ClientsPanel(HostGameWindow window)
	{
		this.window = window;
		
		setLayout(new MigLayout("", "[]", "[]"));
		
		add(new JButton("Add another client"));
		
		addPanel();
		addPanel();
	}
	
	private void addPanel()
	{
		ClientPanel panel = new ClientPanel(this);
		this.panels.add(panel);
		
		((MigLayout)this.getLayout()).setRowConstraints(Util.copy(this.panels.size(), "[]6") + "[]");
		add(panel, new CC().spanX().cell(0, 1 + this.panels.indexOf(panel)));
		
		getLayout().layoutContainer(this);
	}

	public void removePanel(final ClientPanel panel)
	{
		if (this.panels.size() > 1)
		{
			remove(panel);
			this.panels.remove(panel);
			getLayout().layoutContainer(this);
		}
	}
	
	public HostGameWindow getWindow()
	{
		return this.window;
	}
}
