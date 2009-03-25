package info.reflectionsofmind.connexion.fortress.core.common.tile;

import info.reflectionsofmind.connexion.fortress.core.common.tile.parser.TileCodeFormatException;
import info.reflectionsofmind.connexion.fortress.core.common.tile.parser.TileCodeParser;
import info.reflectionsofmind.connexion.fortress.core.util.Loop;
import info.reflectionsofmind.connexion.fortress.core.util.Multi;

import java.util.List;
import java.util.Map;

public class Tile
{
	private final Multi<Tile, Section> sections = new Multi<Tile, Section>(this);
	private final Map<String, Section> sectionIds;
	private final Multi<Tile, Side> sides = new Multi<Tile, Side>(this);
	private final String code;

	/**
	 * <p>
	 * Code format: p = pasture, c = castle, r = road, m = monastery
	 * </p>
	 * 
	 * <p>
	 * Example: <code>
	 * c1c2f1f2f3f4r1r2|f1r1f2c2f3r2f4,f2c1f3,c1r1,c1r1,r2c1c2,c1r2,f1f2c1,f3f4c1|c1,f1r1f2,c2,f3r2f4
	 * </code>
	 * 
	 * </p>
	 */
	public Tile(final String code) throws TileCodeFormatException
	{
		this.code = code;
		final String[] subCodes = code.split("\\|");

		if (subCodes.length != 3) throw new TileCodeFormatException(code, "There was [" + subCodes.length + "] subcodes in the tile code. Must be [3].");

		final TileCodeParser parser = new TileCodeParser();

		this.sectionIds = parser.parseSectionsCode(subCodes[0], this);
		parser.parseAdjacentsCode(subCodes[1], this.sectionIds);
		final List<List<Section>> sidesSections = parser.parseSidesCode(subCodes[2], this.sectionIds);

		for (final List<Section> sideSections : sidesSections)
		{
			new Side(this, sideSections);
		}
	}

	public Multi<Tile, Section> getSections()
	{
		return this.sections;
	}

	public Loop<Side> getSides()
	{
		return new Loop<Side>(this.sides);
	}

	public Multi<Tile, Side> getSidesLink()
	{
		return this.sides;
	}
	
	public String getCode()
	{
		return this.code;
	}
	
	@Override
	public String toString()
	{
		return "Tile@" + hashCode() + ": " + code;
	}
}
