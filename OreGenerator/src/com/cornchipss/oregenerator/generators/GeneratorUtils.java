package com.cornchipss.oregenerator.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.oregenerator.ref.Helper;
import com.cornchipss.oregenerator.upgrades.GeneratorUpgrade;


public class GeneratorUtils 
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
	
	public static ItemStack createGeneratorItemStack(int type, Material oreGenMat)
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

	public static ItemStack createGeneratorItemStack(Generator gen) 
	{
		return createGeneratorItemStack(gen.getGeneratorId(), gen.getPlugin().getGeneratorMaterial(gen.getGeneratorId()));
	}

	public static String serialize(Generator generator) 
	{
		List<String> upgrades = new ArrayList<>();
		
		addUpgradeLoop:
		for(int i = 0; i < generator.getUpgradesAmount(); i++)
		{
			GeneratorUpgrade gu = generator.getUpgrade(i);
			for(int j = 0; j < upgrades.size(); j++)
			{
				int indexOf = upgrades.get(j).indexOf(":");
				String upgradeId = upgrades.get(j).substring(0, indexOf);
				if(upgradeId.equals(gu.getId() + ""))
				{
					int x = Integer.parseInt(upgrades.get(j).substring(indexOf + 1, upgrades.get(j).length())) + 1;
					upgrades.set(j, upgradeId + ":" + x);
					continue addUpgradeLoop;
				}
			}
			
			upgrades.add(gu.getId() + ":1");
		}
		
		String upgradesStr = "upgrades:{";
		for(int i = 0; i < upgrades.size(); i++)
		{
			upgradesStr += upgrades.get(i);
		}
		upgradesStr += "}";
		
		Location l = generator.getGeneratorBlock().getLocation();
		String locationStr = "location:{x:" + l.getX() + "|y:" + l.getY() + "|z:" + l.getZ() + "|w:" + l.getWorld().getName() + "}";
		String extraInfo = "extra:{time:" + generator.getTimeRemaining() + "}";
		
		String serialized = generator.getGeneratorId() + ":{" + locationStr + "," + extraInfo + "," + upgradesStr + "}";
		System.out.println(serialized);
		return serialized;
	}
	
//	public static Generator deserialize(String serialized)
//	{
//		//3:{location:{x:-285.0|y:68.0|z:249.0|w:world},extra:{time:40},upgrades:{}}
//	}
	
	public Map<String, Object> serializeLocation(Location l) 
	{
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("world", l.getWorld().getName());
		m.put("x", l.getX());
		m.put("y", l.getY());
		m.put("z", l.getZ());
		return m;
	}
		 
		public static Location deserializeLocation(Map<String, Object> m) 
		{
		    World w = Bukkit.getServer().getWorld((String) m.get("world"));
		    if (w == null) 
		    	throw new IllegalArgumentException("non-existent world");
		    return new Location(w, (Double) m.get("x"), (Double) m.get("y"), (Double) m.get("z"));
		}
}
