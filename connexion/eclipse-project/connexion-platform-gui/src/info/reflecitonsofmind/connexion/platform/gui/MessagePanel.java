package info.reflecitonsofmind.connexion.platform.gui;

import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

import net.miginfocom.swing.MigLayout;

public class MessagePanel extends JPanel
{
	private final JTextPane textPane;
	
	public MessagePanel(final IMainFrameReference owner)
	{
		setLayout(new MigLayout("ins 0", "[max, fill]", "[max, fill]"));
		
		this.textPane = new JTextPane();
		this.textPane.setBorder(null);
		this.textPane.setContentType("text/html");
		this.textPane.setEditable(false);
		
		final HTMLDocument document = (HTMLDocument) this.textPane.getDocument();
		
		document.getStyleSheet().addRule("p { margin: 0px; padding: 0px; padding-left: 3px; }");
		document.getStyleSheet().addRule("body { color: green; " //
				+ "font-family: " + getFont().getName() + "; " // 
				+ "font-size: " + getFont().getSize() + "pt; }");
		
		addRawLine("Welcome to Connexion!");
		
		final JScrollPane scrollPane = new JScrollPane(this.textPane);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		add(scrollPane);
	}
	
	public void addRawLine(final String line)
	{
		try
		{
			final HTMLDocument document = (HTMLDocument) MessagePanel.this.textPane.getDocument();
			final Element body = document.getRootElements()[0].getElement(0);
			document.insertBeforeEnd(body, "<p>" + line + "</p>");
		}
		catch (final BadLocationException exception)
		{
			throw new RuntimeException(exception);
		}
		catch (final IOException exception)
		{
			throw new RuntimeException(exception);
		}
	}
}
