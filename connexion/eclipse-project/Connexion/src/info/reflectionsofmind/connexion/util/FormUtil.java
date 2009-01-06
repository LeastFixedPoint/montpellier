package info.reflectionsofmind.connexion.util;

public final class FormUtil
{
	private FormUtil()
	{
		throw new UnsupportedOperationException("Utility class");
	}
	
	public static Form.Field getFieldById(Form form, String id)
	{
		for (Form.Field field : form.getFields())
		{
			if (id.equals(field.getId()))
			{
				return field;
			}
		}
		
		return null;
	}
}
