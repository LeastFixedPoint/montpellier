package info.reflectionsofmind.connexion.core.tile;

public enum Type
{
	PASTURE("f"), CASTLE("c"), ROAD("r"), CLOISTER("m");

	private final String code;

	private Type(final String code)
	{
		this.code = code;
	}

	public static Type getByCode(String code)
	{
		for (Type type : Type.values())
		{
			if (code.equals(type.getCode()))
			{
				return type;
			}
		}
		
		return null;
	}
	
	public String getCode()
	{
		return this.code;
	}
}
