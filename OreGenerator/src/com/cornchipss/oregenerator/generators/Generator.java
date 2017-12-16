package com.cornchipss.oregenerator.generators;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.block.Block;

import com.cornchipss.oregenerator.OreGeneratorPlugin;
import com.cornchipss.oregenerator.ref.Vector3;
import com.cornchipss.oregenerator.upgrades.GeneratorUpgrade;

public abstract class Generator 
{
	private int maxUpgrades = 10; // TODO: Change from config or somethin, idk what to make this
	
	private OreGeneratorPlugin plugin;
	
	private ArrayList<GeneratorUpgrade> upgrades = new ArrayList<>();
	
	private Block generatorBlock;
	
	private Vector3 range;
	private int type, time, timeRemaining;
	private int timeDecreaseAmount = 1;
	private int chance;
	
	public Generator(Vector3 defaultRange, int chance, int type, int time, Block generatorBlock, OreGeneratorPlugin plugin, ArrayList<GeneratorUpgrade> upgrades)
	{
		setRange(defaultRange);
		setChance(chance);
		setType(type);
		setGeneratorBlock(generatorBlock);
		setTimeBetweenRun(time);
		setTimeRemaining(time);
		
		this.plugin = plugin;
		this.upgrades = upgrades;
	}
	
	public Generator(Vector3 defaultRange, int chance, int type, int time, Block generatorBlock, OreGeneratorPlugin plugin)
	{
		setRange(defaultRange);
		setChance(chance);
		setType(type);
		setGeneratorBlock(generatorBlock);
		setTimeBetweenRun(time);
		setTimeRemaining(time);
		
		this.plugin = plugin;
	}
	
	public void tick()
	{
		setTimeRemaining(getTimeRemaining() - getTimeDecreaseAmount());
		
		if(getTimeRemaining() < 0)
		{
			run();
			setTimeRemaining(this.getTimeBetweenRuns());
		}
	}
	
	
	public abstract void run();
	
	public Vector3 getRange() { return range; }
	public void setRange(Vector3 r) { this.range = r; }

	public int getChance() { return chance; }
	public void setChance(int c) { this.chance = c; }
	
	public int getType() { return type; }
	private void setType(int t) { this.type = t; }
	
	public int getTimeBetweenRuns() { return time; }
	public void setTimeBetweenRun(int t) { this.time = t; }
	
	public int getTimeRemaining() { return this.timeRemaining; }
	public void setTimeRemaining(int t) { this.timeRemaining = t; }
	
	public int getTimeDecreaseAmount() { return timeDecreaseAmount; }
	public void setTimeDecreaseAmount(int amt) { this.timeDecreaseAmount = amt; } 
	
	public Block getGeneratorBlock() { return generatorBlock; }
	public void setGeneratorBlock(Block b) { this.generatorBlock = b; }
	
	public OreGeneratorPlugin getPlugin() { return this.plugin; }
	
	protected boolean shouldTransmute()
	{
		Random rdm = new Random();
		return rdm.nextInt(chance) == 0;
	}
	
	public int getUpgradesSize() { return upgrades.size(); }
	public boolean addUpgrade(GeneratorUpgrade upgrade)
	{
		if(maxUpgrades <= getUpgradesSize())
			return false;
		
		upgrade.applyUpgrade(this);
		upgrades.add(upgrade);
		return true;
	}
	protected void removeUpgrade(int i)
	{
		GeneratorUpgrade upgrade = upgrades.get(i);
		upgrade.removeUpgrade(this);
		upgrades.remove(upgrade);
	}
	public GeneratorUpgrade getUpgrade(int index) { return upgrades.get(index); }
	public int getUpgradesAmount() { return upgrades.size(); }
	
	@Override
	public String toString() 
	{
		return "Generator: Type: " + getType() + "; Range: " + getRange() + "; chance per block: " + getChance() + "; Location: " + generatorBlock.getLocation();
	}
}
