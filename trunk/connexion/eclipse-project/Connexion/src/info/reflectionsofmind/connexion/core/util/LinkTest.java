package info.reflectionsofmind.connexion.core.util;
import info.reflectionsofmind.connexion.core.tile.parser.TileCodeFormatException;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;


public class LinkTest
{
	@Test
	public void $_test_linkSingleToSingle_success() throws TileCodeFormatException
	{
		final String source = "source";
		final String target = "source";
		
		final Link<String, String> sourceLink = new Link<String, String>(source);
		final Link<String, String> targetLink = new Link<String, String>(target);
		
		sourceLink.link(targetLink);
		
		Assert.assertSame(target, targetLink.get());
	}

	@Test
	public void $_test_linkSingleToMulti_success() throws TileCodeFormatException
	{
		final String source1 = "source1";
		final String source2 = "source2";
		final String target = "source";
		
		final Link<String, String> source1Link = new Link<String, String>(source1);
		final Link<String, String> source2Link = new Link<String, String>(source2);
		final Multi<String, String> targetLink = new Multi<String, String>(target);
		
		source1Link.link(targetLink);
		source2Link.link(targetLink);
		
		Assert.assertEquals(Arrays.asList(source1, source2), targetLink);
		Assert.assertSame(target, source1Link.get());
		Assert.assertSame(target, source2Link.get());
	}

	@Test
	public void $_test_linkMultiToSingle_success() throws TileCodeFormatException
	{
		final String source1 = "source1";
		final String source2 = "source2";
		final String target = "source";
		
		final Link<String, String> source1Link = new Link<String, String>(source1);
		final Link<String, String> source2Link = new Link<String, String>(source2);
		final Multi<String, String> targetLink = new Multi<String, String>(target);
		
		targetLink.link(source1Link);
		targetLink.link(source2Link);
		
		Assert.assertEquals(Arrays.asList(source1, source2), targetLink);
		Assert.assertSame(target, source1Link.get());
		Assert.assertSame(target, source2Link.get());
	}

	@Test
	public void $_test_linkMultiToMulti_success() throws TileCodeFormatException
	{
		final String source1 = "source1";
		final String source2 = "source2";
		final String target1 = "target1";
		final String target2 = "target2";
		
		final Multi<String, String> source1Link = new Multi<String, String>(source1);
		final Multi<String, String> source2Link = new Multi<String, String>(source2);
		final Multi<String, String> target1Link = new Multi<String, String>(target1);
		final Multi<String, String> target2Link = new Multi<String, String>(target2);
		
		target1Link.link(source1Link);
		target1Link.link(source2Link);
		source1Link.link(target2Link);
		
		Assert.assertEquals(Arrays.asList(source1, source2), target1Link);
		Assert.assertEquals(Arrays.asList(source1), target2Link);
		Assert.assertEquals(Arrays.asList(target1, target2), source1Link);
		Assert.assertEquals(Arrays.asList(target1), source2Link);
	}
}
