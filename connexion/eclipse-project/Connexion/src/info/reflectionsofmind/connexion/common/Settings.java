package info.reflectionsofmind.connexion.common;

import info.reflectionsofmind.connexion.transport.ITransport;
import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;
import info.reflectionsofmind.connexion.util.FormUtil;
import info.reflectionsofmind.connexion.util.Form.Parameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jivesoftware.smack.util.StringUtils;

import com.google.common.collect.ImmutableMap;

public class Settings
{
	private final static Pattern TRANSPORT_CLASS_KEY = Pattern.compile("^transport\\.(.+)\\.class$");

	private Configuration root;

	private final List<ITransport> transports = new ArrayList<ITransport>();

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
		
		this.root = new Configuration((Map)properties);
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
					final Parameter field = FormUtil.getFieldById(transport.getForm(), fieldName);
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
	
	// ============================================================================================
	// === CATEGORY CLASS
	// ============================================================================================

	public class Configuration
	{
		private final Map<String, Configuration> categories = new HashMap<String, Configuration>();
		private final Map<String, String> values = new HashMap<String, String>();
		
		public Configuration(Map<String, String> properties)
		{
			for (String key : properties.keySet())
			{
				final String[] tokens = key.split("\\.");
				
				Configuration category = this;
				
				for (int i = 0; i < tokens.length - 1; i++)
				{
					if (categories.get(tokens[i]) == null)
					{
						categories.put(tokens[i], new Configuration());
					}
					
					category = categories.get(tokens[i]);
				}
				
				category.values.put(tokens[tokens.length - 1], properties.get(key));
			}
		}
		
		public Configuration()
		{
			
		}

		public Map<String, Configuration> getCategories()
		{
			return ImmutableMap.copyOf(this.categories);
		}

		public Map<String, String> getValues()
		{
			return ImmutableMap.copyOf(this.values);
		}
	}

	public Configuration getRoot()
	{
		return this.root;
	}
}
