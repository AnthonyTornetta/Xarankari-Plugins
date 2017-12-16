package com.cornchipss.oregenerator.upgrades;

import org.bukkit.inventory.ItemStack;

import com.cornchipss.oregenerator.generators.Generator;

public abstract class GeneratorUpgrade 
{
	private ItemStack symbol;
	private int id;
	
	public GeneratorUpgrade(ItemStack symbol, int id)
	{
		setSymbol(symbol);
		setId(id);
	}
	
	public abstract void applyUpgrade(Generator g);
	public abstract void removeUpgrade(Generator g);
	
	public ItemStack getSymbol() { return this.symbol; }
	protected void setSymbol(ItemStack s) { this.symbol = s; }
	
	public int getId() { return this.id; }
	private void setId(int i) { this.id = i; }
}
