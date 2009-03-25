package info.reflectionsofmind.connexion.fortress.core.common.tile.parser;

import info.reflectionsofmind.connexion.fortress.core.common.tile.Section;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Tile;
import info.reflectionsofmind.connexion.fortress.core.common.tile.Type;
import info.reflectionsofmind.connexion.fortress.core.common.util.Multi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TileCodeParser
{
	public Map<String, Section> parseSectionsCode(final String code, final Tile tile) throws TileCodeFormatException
	{
		final Map<String, Section> sections = new LinkedHashMap<String, Section>();

		if (code.length() % 2 != 0) throw new TileCodeFormatException(code, "Sections code has odd length [" + code.length() + "].");

		for (int i = 0; i < code.length(); i += 2)
		{
			final String id = code.substring(i, i + 2);

			if (sections.containsKey(id)) throw new TileCodeFormatException(code, "Section with id [" + id + "] is already present on the tile.");

			final Type type = Type.getByCode(code.substring(i, i + 1));

			if (type == null) throw new TileCodeFormatException(code, "Unknown section type [" + code.substring(i, i + 1) + "].");

			sections.put(id, new Section(type, tile));
		}

		return Collections.unmodifiableMap(sections);
	}

	public void parseAdjacentsCode(final String code, final Map<String, Section> sections) throws TileCodeFormatException
	{
		final Iterator<Entry<String, Section>> sectionIterator = sections.entrySet().iterator();

		for (String subCode : code.split(","))
		{
			if (!sectionIterator.hasNext()) throw new TileCodeFormatException(subCode, "There are more adjacents subcodes than sections.");

			if (subCode.length() % 2 != 0) throw new TileCodeFormatException(subCode, "A subcode in adjacents code has odd length [" + subCode.length() + "].");

			final Entry<String, Section> entry = sectionIterator.next();

			for (int i = 0; i < subCode.length(); i += 2)
			{
				final String id = subCode.substring(i, i + 2);
				final Section section = sections.get(id);

				if (section == null) throw new TileCodeFormatException(subCode, "Section with id [" + id + "] referenced as adjacent, but not found.");

				entry.getValue().addAdjacent(section);
			}
		}

		if (sectionIterator.hasNext()) throw new TileCodeFormatException(code, "There are more sections than adjacents subcodes.");
	}

	public List<List<Section>> parseSidesCode(final String code, final Map<String, Section> sections) throws TileCodeFormatException
	{
		final List<List<Section>> sides = new ArrayList<List<Section>>();

		for (String subCode : code.split(","))
		{
			final List<Section> side = new ArrayList<Section>();

			for (int i = 0; i < subCode.length(); i += 2)
			{
				final String id = subCode.substring(i, i + 2);
				final Section section = sections.get(id);

				if (section == null) throw new TileCodeFormatException(subCode, "Section with id [" + id + "] referenced as adjacent, but not found.");

				side.add(section);
			}

			sides.add(Collections.unmodifiableList(side));
		}

		return Collections.unmodifiableList(sides);
	}

	// ============================================================================================
	// === TESTING
	// ============================================================================================

	@Test
	public void $_test_parseSectionsCode_correct() throws TileCodeFormatException
	{
		final String code = "c1c2f1f2f3f4r1r2mm";

		final Tile tile = Mockito.mock(Tile.class);
		final Multi<Tile, Section> sectionsLink = new Multi<Tile, Section>(tile);
		Mockito.when(tile.getSections()).thenReturn(sectionsLink);
		final Map<String, Section> sections = new TileCodeParser().parseSectionsCode(code, tile);

		final List<String> ids = Arrays.asList("c1", "c2", "f1", "f2", "f3", "f4", "r1", "r2", "mm");

		Assert.assertEquals(ids.size(), sections.size());
		Assert.assertEquals(new HashSet<String>(ids), sections.keySet());
	}

	@Test
	public void $_test_parseAdjacentsCode_correct() throws TileCodeFormatException
	{
		final String sectionsCode = "c1c2f1f2f3f4r1r2mm";

		final Tile tile = Mockito.mock(Tile.class);
		final Multi<Tile, Section> sectionsLink = new Multi<Tile, Section>(tile);
		Mockito.when(tile.getSections()).thenReturn(sectionsLink);
		final Map<String, Section> sections = new TileCodeParser().parseSectionsCode(sectionsCode, tile);

		final String code = "f1r1f2c2f3r2f4,f2c1f3,c1r1,c1r1,r2c1c2mm,c1r2,f1f2c1,f3f4c1,f3";
		new TileCodeParser().parseAdjacentsCode(code, sections);

		final Map<String, List<String>> adjacentIds = new HashMap<String, List<String>>();
		adjacentIds.put("c1", Arrays.asList("f1", "r1", "f2", "c2", "f3", "r2", "f4"));
		adjacentIds.put("c2", Arrays.asList("f2", "c1", "f3"));
		adjacentIds.put("f1", Arrays.asList("c1", "r1"));
		adjacentIds.put("f2", Arrays.asList("c1", "r1"));
		adjacentIds.put("f3", Arrays.asList("r2", "c1", "c2", "mm"));
		adjacentIds.put("f4", Arrays.asList("c1", "r2"));
		adjacentIds.put("r1", Arrays.asList("f1", "f2", "c1"));
		adjacentIds.put("r2", Arrays.asList("f3", "f4", "c1"));
		adjacentIds.put("mm", Arrays.asList("f3"));

		for (String srcId : adjacentIds.keySet())
		{
			for (String adjId : adjacentIds.get(srcId))
			{
				Assert.assertTrue(sections.get(srcId).isAdjacentTo(sections.get(adjId)), //
						"[" + srcId + "] is not adjacent to [" + adjId + "].");
			}
		}
	}

	@Test
	public void $_test_parseSidesCode_correct() throws TileCodeFormatException
	{
		final String sectionsCode = "c1c2f1f2f3f4r1r2mm";

		final Tile tile = Mockito.mock(Tile.class);
		final Multi<Tile, Section> sectionsLink = new Multi<Tile, Section>(tile);
		Mockito.when(tile.getSections()).thenReturn(sectionsLink);
		final Map<String, Section> sections = new TileCodeParser().parseSectionsCode(sectionsCode, tile);

		final String code = "c1,f1r1f2,c2,f3r2f4";
		final List<List<Section>> sides = new TileCodeParser().parseSidesCode(code, sections);

		final List<List<String>> sidesIds = new ArrayList<List<String>>();
		sidesIds.add(Arrays.asList("c1"));
		sidesIds.add(Arrays.asList("f1", "r1", "f2"));
		sidesIds.add(Arrays.asList("c2"));
		sidesIds.add(Arrays.asList("f3", "r2", "f4"));

		Assert.assertEquals(sidesIds.size(), sides.size());

		final Iterator<List<Section>> sidesIterator = sides.iterator();
		for (List<String> sideIds : sidesIds)
		{
			final List<Section> side = sidesIterator.next();

			Assert.assertEquals(sideIds.size(), side.size());

			final Iterator<Section> sideIterator = side.iterator();
			for (String sideId : sideIds)
			{
				Assert.assertSame(sections.get(sideId), sideIterator.next());
			}
		}
	}
}
