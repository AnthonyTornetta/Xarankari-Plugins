package com.cornchipss.oregenerator.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.oregenerator.ref.Reference;

public class CommandInventories 
{
	public static void openGetGeneratorGUI(final Player p)
	{
		final int ROWS = 3;
		
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.CMD_WINDOW_GET_GENERATOR_TITLE);
		
		genBorders(ROWS, inv);
		
		ItemStack coalGen = new ItemStack(Material.COAL_ORE);
		ItemMeta coalMeta = coalGen.getItemMeta();
		coalMeta.setDisplayName(ChatColor.DARK_GRAY + "Coal Ore Generator");
		coalGen.setItemMeta(coalMeta);
		
		ItemStack ironGen = new ItemStack(Material.IRON_ORE);
		ItemMeta ironMeta = ironGen.getItemMeta();
		ironMeta.setDisplayName(ChatColor.GRAY + "Iron Ore Generator");
		ironGen.setItemMeta(ironMeta);
		
		ItemStack redstoneGen = new ItemStack(Material.REDSTONE_ORE);
		ItemMeta redMeta = redstoneGen.getItemMeta();
		redMeta.setDisplayName(ChatColor.RED + "Redstone Ore Generator");
		redstoneGen.setItemMeta(redMeta);
		
		ItemStack lapisGen = new ItemStack(Material.LAPIS_ORE);
		ItemMeta lapisMeta = redstoneGen.getItemMeta();
		lapisMeta.setDisplayName(ChatColor.BLUE + "Lapis Ore Generator");
		lapisGen.setItemMeta(lapisMeta);
		
		ItemStack goldGen = new ItemStack(Material.GOLD_ORE);
		ItemMeta goldMeta = goldGen.getItemMeta();
		goldMeta.setDisplayName(ChatColor.GOLD + "Gold Ore Generator");
		goldGen.setItemMeta(goldMeta);
		
		ItemStack diaGen = new ItemStack(Material.DIAMOND_ORE);
		ItemMeta diaMeta = diaGen.getItemMeta();
		diaMeta.setDisplayName(ChatColor.AQUA + "Diamond Ore Generator");
		diaGen.setItemMeta(diaMeta);
		
		ItemStack emeraldGen = new ItemStack(Material.EMERALD_ORE);
		ItemMeta emeraldMeta = emeraldGen.getItemMeta();
		emeraldMeta.setDisplayName(ChatColor.GREEN + "Emerald Ore Generator");
		emeraldGen.setItemMeta(emeraldMeta);
		
		inv.setItem(9 + 1, coalGen);
		inv.setItem(9 + 2, ironGen);
		inv.setItem(9 + 3, redstoneGen);
		inv.setItem(9 + 4, lapisGen);
		inv.setItem(9 + 5, goldGen);
		inv.setItem(9 + 6, diaGen);
		inv.setItem(9 + 7, emeraldGen);
		
		p.openInventory(inv);
	}

	private static void genBorders(final int ROWS, final Inventory inv)
	{
		ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)13);
		ItemMeta greenGlassMeta = greenGlass.getItemMeta();
		greenGlassMeta.setDisplayName(" ");
		greenGlass.setItemMeta(greenGlassMeta);
		
		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta closeMeta = close.getItemMeta();
		closeMeta.setDisplayName(ChatColor.RED + "Close");
		close.setItemMeta(closeMeta);
		
		// Give it a border
		for(int i = 0; i < 9; i++)
		{
			if(i == 8)
				inv.setItem(i, close);
			else
				inv.setItem(i, greenGlass);
			inv.setItem(i + ((ROWS - 1) * 9), greenGlass);
		}
	}
}
