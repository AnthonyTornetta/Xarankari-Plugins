package com.cornchipss.main.helpers;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.main.items.CustomItemForge;
import com.cornchipss.main.items.Items;

import net.md_5.bungee.api.ChatColor;

public class LoreParser 
{
	public static final byte BAD_VALUE = -9;
	public static byte getId(ItemStack item)
	{
		if(item != null)
			if(item.getItemMeta() != null)
				if(item.getItemMeta().getLore() != null)
					if(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1) != null)
						return Byte.parseByte(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).substring(2));
		return BAD_VALUE;
	}
	
	public static int getMana(ItemStack item)
	{
		if(item.getItemMeta().getLore() == null)
			return BAD_VALUE;
		String manaLore = item.getItemMeta().getLore().get(0);
		
		try
		{
			// 100000 to 999999
			if(isInteger(manaLore.substring(2, 8)))
			{
				return Integer.parseInt(manaLore.substring(2, 8));
			}
		} catch(StringIndexOutOfBoundsException ex) {}
		
		try
		{
			// 10000 to 99999
			if(isInteger(manaLore.substring(2, 7)))
			{
				return Integer.parseInt(manaLore.substring(2, 7));
			}
		} catch(StringIndexOutOfBoundsException ex) {}
		
		try
		{
			// 1000 to 9999
			if(isInteger(manaLore.substring(2, 6)))
			{
				return Integer.parseInt(manaLore.substring(2, 6));
			}
		} catch(StringIndexOutOfBoundsException ex) {}
		
		try
		{
			// 100 to 999
			if(isInteger(manaLore.substring(2, 5)))
			{
				return Integer.parseInt(manaLore.substring(2, 5));
			}
		} catch(StringIndexOutOfBoundsException ex) {}
		
		try
		{
			// 10 to 99
			if(isInteger(manaLore.substring(2, 4)))
			{
				return Integer.parseInt(manaLore.substring(2, 4));
			}
		} catch(StringIndexOutOfBoundsException ex) {}
		
		try
		{
			// 0 to 9
			if(isInteger(manaLore.substring(2, 3)))
			{
				return Integer.parseInt(manaLore.substring(2, 3));
			}
		} catch(StringIndexOutOfBoundsException ex) {}
		
		// It must be a custom mana string (like 100 mana per tick or something)
		return 0;
	}
	
	public static boolean isInteger(String str) 
	{
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}

	public static boolean takeMana(ItemStack item, Player caster) 
	{
		int manaToTake = getMana(item);
		if(manaToTake == BAD_VALUE)
			return false;
		
		ItemStack magicHolder = caster.getInventory().getItemInOffHand();
		if(LoreParser.getId(magicHolder) == Items.MAGIC_HOLDER_ID)
		{
			ItemMeta im = magicHolder.getItemMeta();
			int mana = getMana(magicHolder);
			if(mana - manaToTake < 0)
			{
				caster.sendMessage(ChatColor.RED + "Not enough mana to cast the spell!");
				return false;
			}
			mana -= manaToTake;
			ArrayList<String> lore = (ArrayList<String>) im.getLore();
			lore.set(0, ChatColor.LIGHT_PURPLE + "" + mana + "/" + CustomItemForge.CORE_MAX_USES);
			im.setLore(lore);
			magicHolder.setItemMeta(im);
			//caster.getInventory().setItemInOffHand(magicHolder);			
			return true;
		}
		else
		{
			caster.sendMessage(ChatColor.RED + "The magician's core must be in your off hand!");
			return false;
		}
	}
	
	public static boolean takeMana(int manaToTake, Player caster) 
	{
		ItemStack magicHolder = caster.getInventory().getItemInOffHand();
		if(LoreParser.getId(magicHolder) == Items.MAGIC_HOLDER_ID)
		{
			ItemMeta im = magicHolder.getItemMeta();
			int mana = getMana(magicHolder);
			if(mana - manaToTake < 0)
			{
				caster.sendMessage(ChatColor.RED + "Not enough mana to cast the spell!");
				return false;
			}
			mana -= manaToTake;
			ArrayList<String> lore = (ArrayList<String>) im.getLore();
			lore.set(0, ChatColor.LIGHT_PURPLE + "" + mana + "/" + CustomItemForge.CORE_MAX_USES);
			im.setLore(lore);
			magicHolder.setItemMeta(im);
			//caster.getInventory().setItemInOffHand(magicHolder);			
			return true;
		}
		else
		{
			caster.sendMessage(ChatColor.RED + "The magician's core must be in your off hand!");
			return false;
		}
	}

	public static void addMana(Player caster, int amount) 
	{
		ItemStack item = caster.getInventory().getItemInOffHand();
		ItemMeta im = item.getItemMeta();
		int mana = getMana(item);
		mana += amount;
		mana = Helper.clamp(mana, 0, CustomItemForge.CORE_MAX_USES);
		ArrayList<String> lore = (ArrayList<String>)im.getLore();
		lore.set(0, ChatColor.LIGHT_PURPLE + "" + mana + "/" + CustomItemForge.CORE_MAX_USES);
		im.setLore(lore);
		item.setItemMeta(im);
	}
}