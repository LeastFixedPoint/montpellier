package info.reflectionsofmind.connexion.gui.join;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.AbstractDocument.AbstractElement;
import javax.swing.text.html.HTMLDocument;

import net.miginfocom.swing.MigLayout;

public class ChatPane extends JPanel
{
	private final JEditorPane chatPane;
	private final JButton sendButton;
	private final JTextField sendField;

	public ChatPane()
	{
		this.chatPane = new JEditorPane("text/html", null);
		this.chatPane.setEditable(false);
		this.chatPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		
		this.sendButton = new JButton("Send");
		this.sendField = new JTextField();
		
		setLayout(new MigLayout("ins 0", "[grow][]", "[grow][]"));
		
		add(this.chatPane, "grow, span");
		add(this.sendField, "grow");
		add(this.sendButton, "grow");

		putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
	}

	public void writeRaw(final String text)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					final HTMLDocument document = (HTMLDocument) ChatPane.this.chatPane.getDocument();
					final AbstractElement element = (AbstractElement) document.getRootElements()[0].getElement(0).getElement(0);

					document.insertBeforeEnd(element, text);
				}
				catch (final IOException exception)
				{
					throw new RuntimeException(exception);
				}
				catch (final BadLocationException exception)
				{
					throw new RuntimeException(exception);
				}
			}
		});
	}

	public void writeMessage(final String name, final String message)
	{
		writeRaw("<font color=red>" + name + ":</font> " + message + "<br>");
	}

	public void writeSystem(final String text)
	{
		writeRaw("<font color=green>" + text + "</font><br>");
	}
}
