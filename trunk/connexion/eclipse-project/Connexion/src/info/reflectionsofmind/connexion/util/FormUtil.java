package info.reflectionsofmind.connexion.util;

public final class FormUtil
{
	private FormUtil()
	{
		throw new UnsupportedOperationException("Utility class");
	}
	
	public static Form.Parameter getFieldById(Form form, String id)
	{
		for (Form.Parameter field : form.getFields())
		{
			if (id.equals(field.getId()))
			{
				return field;
			}
		}
		
		return null;
	}
	
	public static ConfigurationBuilder newBuilder()
	{
		return new ConfigurationBuilder(new Form());
	}
	
	public static final class ConfigurationBuilder
	{
		private final Form configuration;
		
		private ConfigurationBuilder(Form configuration)
		{
			this.configuration = configuration;
		}
		
		public ConfigurationBuilder addString(String id, String label)
		{
			this.configuration.addField(new Form.Parameter( //
					id, Form.Parameter.Type.STRING, label, null));
			
			return this;
		}
		
		public ConfigurationBuilder addInteger(String id, String label)
		{
			this.configuration.addField(new Form.Parameter( //
					id, Form.Parameter.Type.INT, label, null));
			
			return this;
		}
		
		public Form build()
		{
			return this.configuration;
		}
	}
}
