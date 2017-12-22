package com.cornchipss.oregenerator.upgrades;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.oregenerator.ref.Helper;
import com.cornchipss.oregenerator.upgrades.types.SpeedUpgrade;

public class UpgradeUtils 
{
	public static final int UPGRADE_SPEED_ID = 0;
	public static final int MAX_UPGRADE_ID = UPGRADE_SPEED_ID;
	public static final int MIN_UPGRADE_ID = UPGRADE_SPEED_ID;
	
	public static GeneratorUpgrade createUpgradeFromId(int id)
	{
		GeneratorUpgrade g = null;
		
		switch(id)
		{
			case UPGRADE_SPEED_ID:
			{
				g = new SpeedUpgrade();
			}
		}
		return g;
	}
	
	public static int getItemStackUpgradeId(ItemStack is)
	{
		int id = -1;
		
		if(is == null)
			return id;
		
		ItemMeta im = is.getItemMeta();
		if(im == null)
			return id;
		
		List<String> lore = im.getLore();
		if(lore == null)
			return id;
		
		for(int i = 0; i < lore.size(); i++)
		{
			String loreLine = ChatColor.stripColor(lore.get(i)).toLowerCase();
			if(loreLine.contains("upgrade"))
			{
				int amt = 1;
				while(!Helper.isInt(loreLine.substring(loreLine.length() - amt)))
				{
					amt--;
				}
				
				id = Integer.parseInt(loreLine.substring(loreLine.length() - amt));
			}
		}
		
		return id;
	}
	
	public static ItemStack createUpgradeItemStack(int id, Material material)
	{
		if(id < 0 || id > MAX_UPGRADE_ID)
		{
			System.out.println("WARNING: Generator ID out of range");
			return null; // Not a valid id
		}
		
		ItemStack is = new ItemStack(material);
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null)
			lore = new ArrayList<>();
		
		String name = null;
		
		switch(id)
		{
		case UPGRADE_SPEED_ID:
			name = ChatColor.AQUA + "Speed";
			break;
		}
		
		if(name == null)
			return null;
		
		lore.add(ChatColor.stripColor(name) + " Upgrade: " + id);
		im.setLore(lore);
		
		is.addUnsafeEnchantment(Enchantment.LURE, 1);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);		
		
		is.setItemMeta(im);

		return is;
	}
	
	public static ItemStack createUpgradeItemStack(GeneratorUpgrade u)
	{
		return createUpgradeItemStack(u.getId(), u.getSymbol().getType());
	}
}
