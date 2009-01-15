package info.reflectionsofmind.connexion.util.form;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public final class Form
{
	private final List<Field<?>> fields = new ArrayList<Field<?>>();

	public void addField(Field<?> field)
	{
		this.fields.add(field);
	}

	public List<Field<?>> getFields()
	{
		return ImmutableList.copyOf(this.fields);
	}

	public abstract class Field<T>
	{
		private final String id;
		private final String title;
		private T value;

		public Field(String id, String title, T defaultValue)
		{
			this.id = id;
			this.title = title;
			this.value = defaultValue;
		}

		public String getId()
		{
			return this.id;
		}

		public void setValue(T value)
		{
			this.value = value;
		}

		public T getValue()
		{
			return this.value;
		}

		public String getTitle()
		{
			return this.title;
		}
	}

	public final class IntField extends Field<Integer>
	{

		public IntField(String id, String title, Integer defaultValue)
		{
			super(id, title, defaultValue);
		}

	}

	public final class StringField extends Field<String>
	{
		public StringField(String id, String title, String defaultValue)
		{
			super(id, title, defaultValue);
		}
	}
}
