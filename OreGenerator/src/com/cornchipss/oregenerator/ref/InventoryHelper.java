package com.cornchipss.oregenerator.ref;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryHelper 
{
	public static void genBorders(int rows, Inventory inv)
	{
		genBorders(rows, inv, "");
	}

	public static void genBorders(int rows, Inventory inv, String display) 
	{
		if(display.isEmpty())
			display = " ";
		ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)13);
		ItemMeta greenGlassMeta = greenGlass.getItemMeta();
		greenGlassMeta.setDisplayName(display);
		greenGlass.setItemMeta(greenGlassMeta);
		
		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta closeMeta = close.getItemMeta();
		closeMeta.setDisplayName(ChatColor.RED + "Close");
		close.setItemMeta(closeMeta);
		
		for(int i = 0; i < 9; i++)
		{
			if(i == 8)
				inv.setItem(i, close);
			else
				inv.setItem(i, greenGlass);
			inv.setItem(i + ((rows - 1) * 9), greenGlass);
		}
	}
}
