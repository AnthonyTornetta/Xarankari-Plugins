package com.cornchipss.main;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemForge 
{
	public static ItemStack makeItem(Material mat, String name, String lore, int amount)
	{
		// Create the item
		ItemStack item = new ItemStack(mat, amount);
		
		// Change the meta data
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		ArrayList<String> loreList = new ArrayList<>();
		loreList.add(ChatColor.DARK_GRAY + lore);
		im.setLore(loreList);		
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		// Set the items meta data to the new one
		item.setItemMeta(im);
		
		// Return it
		return item;
	}
}