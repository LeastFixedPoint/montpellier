package info.reflectionsofmind.connexion.core.game;

import info.reflectionsofmind.connexion.core.tile.Section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Feature
{
	private final List<Section> sections = new ArrayList<Section>(); 

	public List<Section> getSections()
	{
		return Collections.unmodifiableList(this.sections);
	}

	public void addSection(Section currentSection)
	{
		this.sections.add(currentSection);
	}
}
