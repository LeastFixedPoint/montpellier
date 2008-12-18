package info.reflectionsofmind.connexion.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class Form
{
	public enum FieldType
	{
		STRING, INT;
	}

	private final List<Field> fields = new ArrayList<Field>();

	public void addField(Field field)
	{
		this.fields.add(field);
	}

	public List<Field> getFields()
	{
		return ImmutableList.copyOf(this.fields);
	}

	public static class Field
	{
		private final String name;
		private final FieldType type;
		private Object value;

		public Field(FieldType type, String name, Object value)
		{
			this.type = type;
			this.name = name;
			this.value = value;
		}

		public String getName()
		{
			return this.name;
		}

		public FieldType getType()
		{
			return this.type;
		}

		public String getString()
		{
			if (this.type != FieldType.STRING) throw new ClassCastException("Tried to get string from field of type [" + this.type + "].");
			return (String) value;
		}
		
		public void setString(String value)
		{
			if (this.type != FieldType.STRING) throw new ClassCastException("Tried to set string to field of type [" + this.type + "].");
			this.value = value;
		}

		public Integer getInt()
		{
			if (this.type != FieldType.INT) throw new ClassCastException("Tried to get integer from field of type [" + this.type + "].");
			return (Integer) value;
		}
		
		public void setInt(Integer value)
		{
			if (this.type != FieldType.INT) throw new ClassCastException("Tried to set integer to field of type [" + this.type + "].");
			this.value = value;
		}
	}
}
