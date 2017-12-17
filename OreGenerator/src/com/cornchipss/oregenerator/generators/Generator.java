package com.cornchipss.oregenerator.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.oregenerator.OreGeneratorPlugin;
import com.cornchipss.oregenerator.ref.DoubleList;
import com.cornchipss.oregenerator.ref.InventoryHelper;
import com.cornchipss.oregenerator.ref.Vector3;
import com.cornchipss.oregenerator.upgrades.GeneratorUpgrade;

import net.md_5.bungee.api.ChatColor;

public abstract class Generator 
{
	private int maxUpgrades = 10; // TODO: Change from config or somethin, idk what to make this
	
	private OreGeneratorPlugin plugin;
	
	private List<GeneratorUpgrade> upgrades = new ArrayList<>();
	
	private Block generatorBlock;
	
	private Vector3 range;
	
	private int genId, time, timeRemaining;
	private int timeDecreaseAmount = 1;
	private int chance;
	
	public Generator(Vector3 defaultRange, int chance, int genId, Block generatorBlock, OreGeneratorPlugin plugin, List<GeneratorUpgrade> upgrades)
	{
		setRange(defaultRange);
		setChance(chance);
		setGeneratorId(genId);
		setGeneratorBlock(generatorBlock);
		setTimeBetweenRun(plugin.getGeneratorTimeBetween(getGeneratorId()));
		setTimeRemaining(plugin.getGeneratorTimeBetween(getGeneratorId()));
		
		this.plugin = plugin;
		this.upgrades = upgrades;
	}
	
	public Generator(Vector3 defaultRange, int chance, int genId, Block generatorBlock, OreGeneratorPlugin plugin)
	{
		setRange(defaultRange);
		setChance(chance);
		setGeneratorId(genId);
		setGeneratorBlock(generatorBlock);
		setTimeBetweenRun(plugin.getGeneratorTimeBetween(getGeneratorId()));
		setTimeRemaining(plugin.getGeneratorTimeBetween(getGeneratorId()));
		
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
	
	public void openInventory(Player p)
	{
		final int ROWS = 5;
		
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS);
		
		System.out.println(getTimeBetweenRuns() - (double)getTimeRemaining() + "; timebe: " + getTimeBetweenRuns() + "; remaining: " + getTimeRemaining());
		InventoryHelper.genBorders(ROWS, inv, ChatColor.GREEN + "" + (int)Math.round(((getTimeBetweenRuns() - (double)getTimeRemaining()) / getTimeBetweenRuns()) * 100) + "% Done");
		
		DoubleList<GeneratorUpgrade, Integer> upgrades = new DoubleList<>();
		for(int i = 0; i < getUpgradesAmount(); i++)
		{
			upgrades.put(getUpgrade(i), upgrades.getOrDefaultVal(getUpgrade(i), 0) + 1);
		}
		
		for(int i = 0; i < upgrades.size(); i++)
		{
			GeneratorUpgrade gu = upgrades.getKey(i);
			ItemStack symbol = gu.getSymbol().clone();
			ItemMeta im = symbol.getItemMeta();
			im.setDisplayName(im.getDisplayName() + " x " + upgrades.getObjectAtIndex(i));
			List<String> lore = im.getLore();
			lore.add(ChatColor.ITALIC + "Click an upgrade in your inventory to add it.");
			symbol.setItemMeta(im);
			inv.setItem(9 + i + 1 + (upgrades.size() / 2 + (upgrades.size() % 2 == 0 ? 0 : 1)), symbol);
		}
		
		p.openInventory(inv);
	}
	
	public abstract void run();
	
	public Vector3 getRange() { return range; }
	public void setRange(Vector3 r) { this.range = r; }

	public int getChance() { return chance; }
	public void setChance(int c) { this.chance = c; }
	
	public int getGeneratorId() { return genId; }
	private void setGeneratorId(int t) { this.genId = t; }
	
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
	
	public boolean addUpgrade(GeneratorUpgrade upgrade)
	{
		if(maxUpgrades <= getUpgradesAmount())
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
		return "Generator: Type: " + getGeneratorId() + "; Range: " + getRange() + "; chance per block: " + getChance() + "; Location: " + generatorBlock.getLocation();
	}
}
