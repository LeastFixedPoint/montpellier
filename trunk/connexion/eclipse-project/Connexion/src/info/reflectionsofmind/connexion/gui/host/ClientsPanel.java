package info.reflectionsofmind.connexion.gui.host;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public class ClientsPanel extends JPanel
{
	private final HostGameFrame window;
	private final List<SlotPanel> panels = new ArrayList<SlotPanel>();

	public ClientsPanel(final HostGameFrame window)
	{
		this.window = window;

		setLayout(new MigLayout("ins 0", "[]6[]", "[]6[]6[]6[]"));

		add(new JLabel(""), "span");

		for (int i = 0; i < 6; i++)
		{
			addPanel();
		}

		getLayout().layoutContainer(this);
	}

	private void addPanel()
	{
		final SlotPanel panel = new SlotPanel(this, this.panels.size());
		this.panels.add(panel);

		final int i = this.panels.indexOf(panel);

		add(panel, new CC().grow().cell(i / 3, 1 + i % 3));
	}

	public void removePanel(final SlotPanel panel)
	{
		if (this.panels.size() > 1)
		{
			remove(panel);
			this.panels.remove(panel);
			getLayout().layoutContainer(this);
		}
	}

	public HostGameFrame getHostGameDialog()
	{
		return this.window;
	}

	@Override
	public void setEnabled(final boolean enabled)
	{
		for (final SlotPanel panel : this.panels)
		{
			panel.setEnabled(enabled);
		}
	}
}
