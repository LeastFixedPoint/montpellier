package info.reflectionsofmind;

public class Sandbox
{

	public static void main(String[] args)
	{
		double r1 = 10.0;
		
		System.out.println("Result: " + v(r1));
	}

	static double v(double r)
	{
		return 4.0 / 3.0 * 3.14 * r * r * r;
	}

	// [тип ретурна или void] имя([тип] аргумент, [тип] аргумент, [тип] аргумент, ...)
	// {
	//      тело;
	//      return что-то;  <-- если не void
	// }
	
}
