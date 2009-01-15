package info.reflectionsofmind.connexion.util.form;

import info.reflectionsofmind.connexion.util.form.Form.Field;
import info.reflectionsofmind.connexion.util.form.Form.IntField;
import info.reflectionsofmind.connexion.util.form.Form.StringField;

public final class FormUtil
{
	private FormUtil()
	{
		throw new UnsupportedOperationException("Utility class");
	}

	@SuppressWarnings("unchecked")
	public static <T> Field<T> getFieldById(Form form, String id)
	{
		for (Field<?> field : form.getFields())
		{
			if (id.equals(field.getId())) return (Field<T>) field;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static String getStringById(Form form, String id)
	{
		for (Form.Field<?> field : form.getFields())
		{
			if (field instanceof StringField && id.equals(field.getId())) return ((StringField) field).getValue();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static Integer getIntegerById(Form form, String id)
	{
		for (Form.Field<?> field : form.getFields())
		{
			if (field instanceof IntField && id.equals(field.getId())) return ((IntField) field).getValue();
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
					id, info.reflectionsofmind.connexion.util.form.STRING, label, null));

			return this;
		}

		public ConfigurationBuilder addInteger(String id, String label)
		{
			this.configuration.addField(new Form.Parameter( //
					id, info.reflectionsofmind.connexion.util.form.INT, label, null));

			return this;
		}

		public Form build()
		{
			return this.configuration;
		}
	}
}
