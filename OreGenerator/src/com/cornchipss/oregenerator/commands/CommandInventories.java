package com.cornchipss.oregenerator.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.oregenerator.OreGeneratorPlugin;
import com.cornchipss.oregenerator.generators.GeneratorUtils;
import com.cornchipss.oregenerator.ref.InventoryHelper;
import com.cornchipss.oregenerator.ref.Reference;
import com.cornchipss.oregenerator.upgrades.UpgradeUtils;

public class CommandInventories 
{
	public static void openGetGeneratorGUI(final Player p, final OreGeneratorPlugin plugin)
	{
		final int ROWS = 3;
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.CMD_WINDOW_GET_GENERATOR_TITLE);
		
		InventoryHelper.genBorders(ROWS, inv);
		
		ItemStack coalGen = new ItemStack(Material.COAL_ORE);
		ItemMeta coalMeta = coalGen.getItemMeta();
		coalMeta.setDisplayName(ChatColor.DARK_GRAY + "Coal Ore Generator");
		List<String> coalLore = new ArrayList<>();
		coalLore.add(ChatColor.GREEN + "$" + plugin.getGeneratorPrice(GeneratorUtils.GENERATOR_COAL_ID));
		coalMeta.setLore(coalLore);
		coalGen.setItemMeta(coalMeta);
				
		ItemStack ironGen = new ItemStack(Material.IRON_ORE);
		ItemMeta ironMeta = ironGen.getItemMeta();
		ironMeta.setDisplayName(ChatColor.GRAY + "Iron Ore Generator");
		List<String> ironLore = new ArrayList<>();
		ironLore.add(ChatColor.GREEN + "$" + plugin.getGeneratorPrice(GeneratorUtils.GENERATOR_IRON_ID));
		ironMeta.setLore(ironLore);
		ironGen.setItemMeta(ironMeta);
		
		ItemStack redstoneGen = new ItemStack(Material.REDSTONE_ORE);
		ItemMeta redMeta = redstoneGen.getItemMeta();
		redMeta.setDisplayName(ChatColor.RED + "Redstone Ore Generator");
		List<String> redstoneLore = new ArrayList<>();
		redstoneLore.add(ChatColor.GREEN + "$" + plugin.getGeneratorPrice(GeneratorUtils.GENERATOR_REDSTONE_ID));
		redMeta.setLore(redstoneLore);
		redstoneGen.setItemMeta(redMeta);
		
		ItemStack lapisGen = new ItemStack(Material.LAPIS_ORE);
		ItemMeta lapisMeta = redstoneGen.getItemMeta();
		lapisMeta.setDisplayName(ChatColor.BLUE + "Lapis Ore Generator");
		List<String> lapisLore = new ArrayList<>();
		lapisLore.add(ChatColor.GREEN + "$" + plugin.getGeneratorPrice(GeneratorUtils.GENERATOR_LAPIS_ID));
		lapisMeta.setLore(lapisLore);
		lapisGen.setItemMeta(lapisMeta);
		
		ItemStack goldGen = new ItemStack(Material.GOLD_ORE);
		ItemMeta goldMeta = goldGen.getItemMeta();
		goldMeta.setDisplayName(ChatColor.GOLD + "Gold Ore Generator");
		List<String> goldLore = new ArrayList<>();
		goldLore.add(ChatColor.GREEN + "$" + plugin.getGeneratorPrice(GeneratorUtils.GENERATOR_GOLD_ID));
		goldMeta.setLore(goldLore);
		goldGen.setItemMeta(goldMeta);
		
		ItemStack diaGen = new ItemStack(Material.DIAMOND_ORE);
		ItemMeta diaMeta = diaGen.getItemMeta();
		diaMeta.setDisplayName(ChatColor.AQUA + "Diamond Ore Generator");
		List<String> diamondLore = new ArrayList<>();
		diamondLore.add(ChatColor.GREEN + "$" + plugin.getGeneratorPrice(GeneratorUtils.GENERATOR_DIAMOND_ID));
		diaMeta.setLore(diamondLore);
		diaGen.setItemMeta(diaMeta);
		
		ItemStack emeraldGen = new ItemStack(Material.EMERALD_ORE);
		ItemMeta emeraldMeta = emeraldGen.getItemMeta();
		emeraldMeta.setDisplayName(ChatColor.GREEN + "Emerald Ore Generator");
		List<String> emeraldLore = new ArrayList<>();
		emeraldLore.add(ChatColor.GREEN + "$" + plugin.getGeneratorPrice(GeneratorUtils.GENERATOR_EMERALD_ID));
		emeraldMeta.setLore(emeraldLore);
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

	public static void openGetUpgradesGUI(Player p, OreGeneratorPlugin plugin) 
	{
		final int ROWS = 3;
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.UPGRADE_INVENTORY_NAME);
		
		InventoryHelper.genBorders(ROWS, inv);
		
//		ItemStack speedUpgrade = new ItemStack(plugin.getUpgradeMaterial(UpgradeUtils.UPGRADE_SPEED_ID));
//		ItemMeta speedMeta = speedUpgrade.getItemMeta();
//		speedMeta.setDisplayName(ChatColor.DARK_GRAY + "Speed Upgrade");
//		List<String> speedLore = new ArrayList<>();
//		speedLore.add(ChatColor.GREEN + "$" + plugin.getUpgradePrice(UpgradeUtils.UPGRADE_SPEED_ID));
//		speedMeta.setLore(speedLore);
//		speedUpgrade.setItemMeta(speedMeta);
		
		for(int i = UpgradeUtils.MIN_UPGRADE_ID; i <= UpgradeUtils.MAX_UPGRADE_ID; i++)
		{
			ItemStack akchewalUpgrade = UpgradeUtils.createUpgradeItemStack(i, plugin.getUpgradeMaterial(i));
			ItemStack upgrade = new ItemStack(plugin.getUpgradeMaterial(i));
			ItemMeta upMeta = upgrade.getItemMeta();
			upMeta.setDisplayName(akchewalUpgrade.getItemMeta().getDisplayName());
			List<String> upLore = new ArrayList<>();
			upLore.add(ChatColor.GREEN + "$" + plugin.getUpgradePrice(i));
			upMeta.setLore(upLore);
			upgrade.setItemMeta(upMeta);
			inv.setItem(10 + i, upgrade);
		}
		
		p.openInventory(inv);
	}
}
