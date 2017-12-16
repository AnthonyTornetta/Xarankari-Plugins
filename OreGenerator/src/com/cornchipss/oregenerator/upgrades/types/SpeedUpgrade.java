package com.cornchipss.oregenerator.upgrades.types;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.oregenerator.generators.Generator;
import com.cornchipss.oregenerator.upgrades.GeneratorUpgrade;


public class SpeedUpgrade extends GeneratorUpgrade
{
	public SpeedUpgrade() 
	{
		super(forgeSymbol());
	}

	@Override
	public void applyUpgrade(Generator g) 
	{
		g.setTimeBetweenRun((int)Math.round(g.getTimeBetweenRuns() / 1.1));
	}
	
	@Override
	public void removeUpgrade(Generator g) 
	{
		g.setTimeBetweenRun((int)Math.round(g.getTimeBetweenRuns() * 1.1));
	}
	
	private static ItemStack forgeSymbol()
	{
		ItemStack s = new ItemStack(Material.SUGAR);
		ItemMeta sm = s.getItemMeta();
		sm.setDisplayName(ChatColor.AQUA + "Speed Upgrade");
		s.setItemMeta(sm);
		return s;
	}
}
