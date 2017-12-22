package com.cornchipss.oregenerator.upgrades.types;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.oregenerator.generators.Generator;
import com.cornchipss.oregenerator.upgrades.GeneratorUpgrade;
import com.cornchipss.oregenerator.upgrades.UpgradeUtils;

public class SpeedUpgrade extends GeneratorUpgrade
{
	private static final double DIF = 1.5;
	
	public SpeedUpgrade()
	{
		super(forgeSymbol(), UpgradeUtils.UPGRADE_SPEED_ID);
	}

	@Override
	public void applyUpgrade(Generator g) 
	{
		int shaved = (int)Math.round(g.getTimeBetweenRuns() - g.getTimeBetweenRuns() / DIF);
		g.setTimeBetweenRun((int)Math.round(g.getTimeBetweenRuns() / DIF));
		
		int secsShaved = g.getTimeRemaining() - shaved;
		g.setTimeRemaining(secsShaved > 0 ? secsShaved : 0);
		System.out.println(g.getTimeBetweenRuns());
	}
	
	@Override
	public void removeUpgrade(Generator g) 
	{
		g.setTimeBetweenRun((int)Math.round(g.getTimeBetweenRuns() * DIF));
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
