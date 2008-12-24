package info.reflectionsofmind.connexion.gui.common;

import info.reflectionsofmind.connexion.common.Client;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
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
	private final List<IListener> listeners = new ArrayList<IListener>();

	private final JEditorPane chatPane;
	private final JButton sendButton;
	private final JTextField sendField;

	public ChatPane()
	{
		this.chatPane = new JEditorPane("text/html", null);
		this.chatPane.setEditable(false);
		this.chatPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

		this.sendButton = new JButton(new SendAction());
		this.sendField = new JTextField();
		this.sendField.setAction(new SendAction());

		setLayout(new MigLayout("ins 0", "[grow][]", "[grow][]"));

		add(this.chatPane, "grow, span");
		add(this.sendField, "grow");
		add(this.sendButton, "grow");

		putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		this.sendButton.setEnabled(enabled);
		this.sendField.setEnabled(enabled);
	}

	public void addListener(IListener listener)
	{
		this.listeners.add(listener);
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

					final String formattedText = text //
							.replaceAll("<red>", "<font color=red><b>") //
							.replaceAll("</red>", "</b></font>");

					document.insertBeforeEnd(element, formattedText);
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

	public void writeMessage(final Client sender, final String message)
	{
		final String name = sender == null ? "Server" : sender.getName();
		writeRaw("[<red>" + name + "</red>]: " + message + "<br>");
	}

	public void writeSystem(final String text)
	{
		writeRaw("<font color=green>" + text + "</font><br>");
	}

	public interface IListener
	{
		void onChatPaneMessageSent(String message);
	}

	private final class SendAction extends AbstractAction
	{
		public SendAction()
		{
			super("Send");
		}

		@Override
		public void actionPerformed(ActionEvent event)
		{
			for (IListener listener : listeners)
			{
				listener.onChatPaneMessageSent(sendField.getText());
			}

			sendField.setText("");
			sendField.requestFocus();
		}
	}
}
