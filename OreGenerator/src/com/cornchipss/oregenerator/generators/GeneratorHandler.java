package com.cornchipss.oregenerator.generators;

import java.util.ArrayList;
import java.util.List;

public class GeneratorHandler 
{
	private List<Generator> generators = new ArrayList<>();

	public void addGenerator(Generator g)
	{		
        generators.add(g);
	}
	
	public void removeGenerator(int i)
	{
		generators.remove(i);
	}
	
	public void tickGenerators()
	{
		for(Generator g : generators)
		{
			g.tick();
		}
	}

	public Generator getGenerator(int i) { return generators.get(i); }
	public int generatorAmount() { return generators.size(); }
}
