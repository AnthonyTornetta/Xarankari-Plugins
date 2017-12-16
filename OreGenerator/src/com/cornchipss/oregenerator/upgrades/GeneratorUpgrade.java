package com.cornchipss.oregenerator.upgrades;

import org.bukkit.inventory.ItemStack;

import com.cornchipss.oregenerator.generators.Generator;

public abstract class GeneratorUpgrade 
{
	private ItemStack symbol;
	
	public GeneratorUpgrade(ItemStack symbol)
	{
		setSymbol(symbol);
	}
	
	public abstract void applyUpgrade(Generator g);
	public abstract void removeUpgrade(Generator g);
	
	public ItemStack getSymbol() { return this.symbol; }
	protected void setSymbol(ItemStack s) { this.symbol = s; }
}
