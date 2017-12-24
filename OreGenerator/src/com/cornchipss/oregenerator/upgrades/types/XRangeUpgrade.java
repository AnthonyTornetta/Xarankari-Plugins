package com.cornchipss.oregenerator.upgrades.types;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.oregenerator.OreGeneratorPlugin;
import com.cornchipss.oregenerator.generators.Generator;
import com.cornchipss.oregenerator.upgrades.GeneratorUpgrade;
import com.cornchipss.oregenerator.upgrades.UpgradeUtils;

public class XRangeUpgrade extends GeneratorUpgrade 
{
	public XRangeUpgrade(OreGeneratorPlugin plugin) 
	{
		super(forgeSymbol(plugin), UpgradeUtils.UPGRADE_X_RANGE_ID);
	}
	
	@Override
	public void applyUpgrade(Generator g) 
	{
		g.getRange().setX(g.getRange().getX() + 2);
	}
	
	@Override
	public void removeUpgrade(Generator g) 
	{
		g.getRange().setX(g.getRange().getX() - 2);
	}
	
	private static ItemStack forgeSymbol(OreGeneratorPlugin plugin)
	{
		ItemStack s = new ItemStack(plugin.getUpgradeMaterial(UpgradeUtils.UPGRADE_X_RANGE_ID));
		ItemMeta sm = s.getItemMeta();
		sm.setDisplayName(ChatColor.AQUA + "X-Axis Range Upgrade");
		s.setItemMeta(sm);
		return s;
	}
}
