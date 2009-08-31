package info.reflecitonsofmind.connexion.platform.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class OkCancelPanel extends JPanel
{
	public OkCancelPanel(final IOkCancelListener listener, final String okTitle, final String cancelTitle)
	{
		setLayout(new MigLayout("ins 0", "[][]", "[]"));
		
		add(new JButton(new AbstractAction(okTitle)
		{
			public void actionPerformed(final ActionEvent arg0)
			{
				listener.onOk();
			}
		}));
		
		add(new JButton(new AbstractAction(cancelTitle)
		{
			public void actionPerformed(final ActionEvent arg0)
			{
				listener.onCancel();
			}
		}));
	}
}
