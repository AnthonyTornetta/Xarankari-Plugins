package com.cornchipss.oregenerator.generators;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.oregenerator.ref.Helper;


public class Generator 
{
	public static final int GENERATOR_COAL_ID     = 0;
	public static final int GENERATOR_IRON_ID     = 1;
	public static final int GENERATOR_REDSTONE_ID = 2;
	public static final int GENERATOR_LAPIS_ID    = 3;
	public static final int GENERATOR_GOLD_ID     = 4;
	public static final int GENERATOR_DIAMOND_ID  = 5;
	public static final int GENERATOR_EMERALD_ID  = 6;
	
	public static int getGeneratorType(ItemMeta im)
	{
		List<String> lore = im.getLore();
		if (lore == null)
			return -1;
		
		String lastLine = lore.get(lore.size() - 1);
		if(Helper.isInt(lastLine.charAt(0) + ""))
		{
			return Integer.parseInt(lastLine.charAt(0) + "");
		}
		return -1;
	}
	
	public static ItemStack createGenerator(int type, Material oreGenMat)
	{
		if(type < 0 || type > GENERATOR_EMERALD_ID)
		{
			System.out.println("WARNING: Generator ID out of range");
			return null; // Not a valid type
		}
		System.out.println(oreGenMat);
		ItemStack is = new ItemStack(oreGenMat);
		System.out.println(is);
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null)
			lore = new ArrayList<>();
		lore.add(type + "");
		im.setLore(lore);
		
		switch(type)
		{
		case GENERATOR_COAL_ID:
			im.setDisplayName(ChatColor.DARK_GRAY + "Coal Transmutator");
			break;
		case GENERATOR_IRON_ID:
			im.setDisplayName(ChatColor.GRAY + "Iron Transmutator");
			break;
		case GENERATOR_REDSTONE_ID:
			im.setDisplayName(ChatColor.RED + "Redstone Transmutator");
			break;
		case GENERATOR_LAPIS_ID:
			im.setDisplayName(ChatColor.BLUE + "Lapis Transmutator");
			break;
		case GENERATOR_GOLD_ID:
			im.setDisplayName(ChatColor.GOLD + "Gold Transmutator");
			break;
		case GENERATOR_DIAMOND_ID:
			im.setDisplayName(ChatColor.AQUA + "Diamond Transmutator");
			break;
		case GENERATOR_EMERALD_ID:
			im.setDisplayName(ChatColor.GREEN + "Emerald Transmutator");
			break;
		}
		
		is.setItemMeta(im);
		return is;
	}
}
