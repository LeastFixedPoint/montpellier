package info.reflectionsofmind.connexion.common;

import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;
import info.reflectionsofmind.connexion.util.FormUtil;
import info.reflectionsofmind.connexion.util.Form.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jivesoftware.smack.util.StringUtils;

public class Settings
{
	private final static Pattern TRANSPORT_CLASS_KEY = Pattern.compile("^transport\\.(.+)\\.class$");

	private String clientName;
	private JabberAddress jabberAddress;

	private List<ITransport> transports = new ArrayList<ITransport>();

	public void load() throws InitializationException
	{
		final Properties properties = new Properties();

		try
		{
			properties.load(getClass().getClassLoader().getResourceAsStream("connexion.properties"));
		}
		catch (IOException exception)
		{
			throw new InitializationException(exception);
		}

		for (Object key : properties.keySet())
		{
			Matcher matcher = TRANSPORT_CLASS_KEY.matcher((String) key);

			if (matcher.matches())
			{
				final String transportName = matcher.group(1);
				transports.add(createTransport(transportName, properties));
			}
		}

		this.jabberAddress = new JabberAddress("connexion:connexion@binaryfreedom.info:5222");
		this.clientName = StringUtils.randomString(8);
	}

	private ITransport createTransport(String transportName, Properties properties) throws InitializationException
	{
		try
		{
			final String transportClassName = properties.getProperty(transportName);
			final Class<?> transportClass = Class.forName(transportClassName);
			final ITransport transport = (ITransport) transportClass.newInstance();
			
			for (Object object : properties.keySet())
			{
				final String key = (String) object;
				
				if (key.startsWith("transport." + transportName + "."))
				{
					final String fieldName = key.substring(key.indexOf('.', key.indexOf('.') + 1) + 1);
					final String fieldValue = properties.getProperty(key);
					final Field field = FormUtil.getFieldById(transport.getForm(), fieldName);
					field.setValue(fieldValue);
				}
			}
			
			return transport;
		}
		catch (Exception exception)
		{
			throw new InitializationException(exception);
		}
	}

	public JabberAddress getJabberAddress()
	{
		return this.jabberAddress;
	}

	public void setJabberAddress(final JabberAddress jabberAddress)
	{
		this.jabberAddress = jabberAddress;
	}

	public String getDefaultClientName()
	{
		return this.clientName;
	}

	public void setClientName(final String playerName)
	{
		this.clientName = playerName;
	}
}
