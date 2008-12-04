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
	private final HostGameDialog window;
	private final JButton addSlotButton;
	private final List<ClientPanel> panels = new ArrayList<ClientPanel>();

	public ClientsPanel(final HostGameDialog window)
	{
		this.window = window;

		setLayout(new MigLayout("", "[]", "[]"));

		this.addSlotButton = new JButton("Add another slot");
		add(this.addSlotButton);

		addPanel();
		addPanel();
	}

	private void addPanel()
	{
		final ClientPanel panel = new ClientPanel(this);
		this.panels.add(panel);

		((MigLayout) getLayout()).setRowConstraints(Util.copy(this.panels.size(), "[]6") + "[]");
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

	public HostGameDialog getWindow()
	{
		return this.window;
	}

	@Override
	public void setEnabled(final boolean enabled)
	{
		for (final ClientPanel panel : this.panels)
		{
			panel.setEnabled(enabled);
		}

		this.addSlotButton.setEnabled(enabled);
	}
}
