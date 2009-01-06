package info.reflectionsofmind.connexion;

import info.reflectionsofmind.connexion.transport.jabber.JabberAddress;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JabberAddressTest
{
	private static final String LONG_ADDRESS = "lo.gin:p@ss.wrd:32@binaryfreedom.info:5222/resource";
	private static final String SHORT_ADDRESS = "lo.gin@binaryfreedom.info";

	@Test
	public void testLogin()
	{
		Assert.assertEquals(new JabberAddress(LONG_ADDRESS).getLogin(), "lo.gin");
	}
	
	@Test
	public void testHost()
	{
		Assert.assertEquals(new JabberAddress(LONG_ADDRESS).getHost(), "binaryfreedom.info");
	}

	@Test
	public void testPassword()
	{
		Assert.assertEquals(new JabberAddress(LONG_ADDRESS).getPassword(), "p@ss.wrd:32");
	}

	@Test
	public void testPasswordAbsent()
	{
		Assert.assertEquals(new JabberAddress(SHORT_ADDRESS).getPassword(), null);
	}
	
	@Test
	public void testPort()
	{
		Assert.assertEquals(new JabberAddress(LONG_ADDRESS).getPort(), new Integer(5222));
	}
	
	@Test
	public void testPortAbsent()
	{
		Assert.assertEquals(new JabberAddress(SHORT_ADDRESS).getPort(), null);
	}
	
	@Test
	public void testResource()
	{
		Assert.assertEquals(new JabberAddress(LONG_ADDRESS).getResource(), "resource");
	}
	
	@Test
	public void testResourceAbsent()
	{
		Assert.assertEquals(new JabberAddress(SHORT_ADDRESS).getResource(), null);
	}
}
