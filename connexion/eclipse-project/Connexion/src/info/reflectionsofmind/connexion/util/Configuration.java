package info.reflectionsofmind.connexion.util;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

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
	
	private Configuration()
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
