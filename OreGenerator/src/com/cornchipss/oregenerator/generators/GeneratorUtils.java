package com.cornchipss.oregenerator.generators;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.oregenerator.OreGeneratorPlugin;
import com.cornchipss.oregenerator.generators.types.CoalGenerator;
import com.cornchipss.oregenerator.generators.types.DiamondGenerator;
import com.cornchipss.oregenerator.generators.types.EmeraldGenerator;
import com.cornchipss.oregenerator.generators.types.GoldGenerator;
import com.cornchipss.oregenerator.generators.types.IronGenerator;
import com.cornchipss.oregenerator.generators.types.LapisGenerator;
import com.cornchipss.oregenerator.generators.types.RedstoneGenerator;
import com.cornchipss.oregenerator.ref.Helper;
import com.cornchipss.oregenerator.upgrades.GeneratorUpgrade;
import com.cornchipss.oregenerator.upgrades.UpgradeUtils;
import com.cornchipss.oregenerator.upgrades.types.SpeedUpgrade;


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
		ItemStack is = new ItemStack(oreGenMat);
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null)
			lore = new ArrayList<>();
		lore.add(type + "");
		im.setLore(lore);
		
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
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
		
		is.addUnsafeEnchantment(Enchantment.LURE, 1);
		
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
			if(i == upgrades.size() - 1)
				upgradesStr += upgrades.get(i);
			else
				upgradesStr += upgrades.get(i) + "|";
		}
		upgradesStr += "}";
		
		Location l = generator.getGeneratorBlock().getLocation();
		String locationStr = "location:{x:" + l.getX() + "|y:" + l.getY() + "|z:" + l.getZ() + "|w:" + l.getWorld().getName() + "}";
		String extraInfo = "extra:{time:" + generator.getTimeRemaining() + "}";
		
		String serialized = generator.getGeneratorId() + ":{" + locationStr + "," + extraInfo + "," + upgradesStr + "}";
		return serialized;
	}
	
	public static Generator deserialize(String serialized, OreGeneratorPlugin plugin)
	{
		serialized = serialized.replaceAll(" ", "").toLowerCase();
		// 3:{location:{x:-285.0|y:68.0|z:249.0|w:world},extra:{time:40},upgrades:{0:2|1:4}}
		String searchFor = ":";
		int cutOut = serialized.indexOf(searchFor);
		
		int genId = Integer.parseInt(serialized.substring(0, cutOut + searchFor.length() - 1));
		
		Location loc = null;
		
		int timeRemaining = 0;
		
		List<GeneratorUpgrade> upgrades = null;
		
		searchFor = ":{";
		cutOut = serialized.indexOf(":{");
		String dealWith = serialized.substring(cutOut + searchFor.length() - 1, serialized.length() - 1);
		 // location:{x:-285.0|y:68.0|z:249.0|w:world},extra:{time:40},upgrades:{0:2|1:4}
		
		String[] thingsToParse = dealWith.split(",");
		for(int i = 0; i < thingsToParse.length; i++)
		{
			if(thingsToParse[i].contains("location"))
			{
				searchFor = ":{";
				cutOut = thingsToParse[i].indexOf(":{");
				dealWith = thingsToParse[i].substring(cutOut + searchFor.length(), thingsToParse[i].length() - 1);
				
				// x:-285.0|y:68.0|z:249.0|w:world
				String[] sections = dealWith.split("\\|");
				double x, y, z;
				World world;
				
				{ String[] splitSection = sections[0].split(":");
				  x = Double.parseDouble(splitSection[1]); }
				
				{ String[] splitSection = sections[1].split(":");
				  y = Double.parseDouble(splitSection[1]); }
				
				{ String[] splitSection = sections[2].split(":");
				  z = Double.parseDouble(splitSection[1]); }
				
				{ String[] splitSection = sections[3].split(":");
				  world = Bukkit.getServer().getWorld(splitSection[1]); }
				
				loc = new Location(world, x, y, z);
			}
			else if(thingsToParse[i].contains("upgrades"))
			{
				searchFor = ":{";
				cutOut = thingsToParse[i].indexOf(":{");
				dealWith = thingsToParse[i].substring(cutOut + searchFor.length(), thingsToParse[i].length() - 1);
				
				if(dealWith.isEmpty())
					continue; // There are no upgrades
				
				// 0:2|1:4
				String[] sections = dealWith.split("\\|");
				for(int j = 0; j < sections.length; j++)
				{
					// Because if there are upgrades to add, make it not null; but if there are no upgrades, keep it null to make the generator creator handle it the way it should
					if(upgrades == null)
						upgrades = new ArrayList<>();
					
					// 0:2
					String[] split = sections[j].split(":");
					int upgradeType = Integer.parseInt(split[0]);
					
					GeneratorUpgrade upgrade = null;
					
					switch(upgradeType)
					{
						case UpgradeUtils.UPGRADE_SPEED_ID:
							upgrade = new SpeedUpgrade();
							break;
					}
					
					for(int k = 0; k < Integer.parseInt(split[1]); k++)
					{
						if(upgrade != null)
							upgrades.add(upgrade);
					}
				}
			}
			else if(thingsToParse[i].contains("extra"))
			{
				searchFor = ":{";
				cutOut = thingsToParse[i].indexOf(":{");
				dealWith = thingsToParse[i].substring(cutOut + searchFor.length(), thingsToParse[i].length() - 1);
				
				// time:40
				timeRemaining = Integer.parseInt(dealWith.split(":")[1]);
			}
		}
		
		Generator generator = createGenerator(genId, loc.getBlock(), plugin, upgrades);
		
		generator.setTimeBetweenRun(timeRemaining);
		
		return generator;
	}
	
	public static Generator createGenerator(int id, Block b, OreGeneratorPlugin plugin, List<GeneratorUpgrade> upgrades)
	{
		Generator gen = null;
		
		boolean addUpgrades = upgrades != null;
		
		switch(id)
		{
			case GENERATOR_COAL_ID:
				if(addUpgrades)
					gen = new CoalGenerator(b, plugin, upgrades);
				else
					gen = new CoalGenerator(b, plugin);
				break;
			case GENERATOR_IRON_ID:
				if(addUpgrades)
					gen = new IronGenerator(b, plugin, upgrades);
				else
					gen = new IronGenerator(b, plugin);
				break;
			case GENERATOR_REDSTONE_ID:
				if(addUpgrades)
					gen = new RedstoneGenerator(b, plugin, upgrades);
				else
					gen = new RedstoneGenerator(b, plugin);
				break;
			case GENERATOR_LAPIS_ID:
				if(addUpgrades)
					gen = new LapisGenerator(b, plugin, upgrades);
				else
					gen = new LapisGenerator(b, plugin);
				break;
			case GENERATOR_GOLD_ID:
				if(addUpgrades)
					gen = new GoldGenerator(b, plugin, upgrades);
				else
					gen = new GoldGenerator(b, plugin);
				break;
			case GENERATOR_DIAMOND_ID:
				if(addUpgrades)
					gen = new DiamondGenerator(b, plugin, upgrades);
				else
					gen = new DiamondGenerator(b, plugin);
				break;
			case GENERATOR_EMERALD_ID:
				if(addUpgrades)
					gen = new EmeraldGenerator(b, plugin, upgrades);
				else
					gen = new EmeraldGenerator(b, plugin);
				break;
		}
		
		return gen;
	}
}
