package info.reflectionsofmind.connexion.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class Form
{
	private final List<Parameter> fields = new ArrayList<Parameter>();

	public void addField(Parameter field)
	{
		this.fields.add(field);
	}

	public List<Parameter> getFields()
	{
		return ImmutableList.copyOf(this.fields);
	}

	public static class Parameter
	{
		public enum Type
		{
			STRING, INT;
		}

		private final String id;
		private final String name;
		private final Type type;
		private Object value;

		public Parameter(String id, Type type, String name, Object value)
		{
			this.id = id;
			this.type = type;
			this.name = name;
			this.value = value;
		}

		public String getId()
		{
			return this.id;
		}

		public String getName()
		{
			return this.name;
		}

		public Type getType()
		{
			return this.type;
		}

		public String getString()
		{
			if (this.type != Type.STRING) throw new ClassCastException("Tried to get string from field of type [" + this.type + "].");
			return (String) value;
		}
		
		public void setString(String value)
		{
			if (this.type != Type.STRING) throw new ClassCastException("Tried to set string to field of type [" + this.type + "].");
			this.value = value;
		}

		public Integer getInteger()
		{
			if (this.type != Type.INT) throw new ClassCastException("Tried to get integer from field of type [" + this.type + "].");
			return (Integer) value;
		}
		
		public void setInt(Integer value)
		{
			if (this.type != Type.INT) throw new ClassCastException("Tried to set integer to field of type [" + this.type + "].");
			this.value = value;
		}
		
		public void setValue(String value)
		{
			switch (this.type)
			{
				case INT: setInt(Integer.valueOf(value));
				case STRING: setString(value);
			}
		}
	}
}
