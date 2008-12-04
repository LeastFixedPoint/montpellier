package info.reflectionsofmind.connexion.local.client.gui.join;

import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.AbstractDocument.AbstractElement;
import javax.swing.text.html.HTMLDocument;

public class ChatPane extends JEditorPane
{
	public ChatPane()
	{
		super("text/html", null);

		setEditable(false);
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
					final HTMLDocument document = (HTMLDocument) getDocument();
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
