package com.cornchipss.main.items;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemForge 
{
	public static final int CORE_MAX_USES = 100000;
	
	/**
	 * Creates a custom item from the custom item forge!
	 * @param name The item's display name
	 * @param description The description in the lore
	 * @param manaPerUse The amount of mana it takes per use
	 * @param id The id of the item
	 * @return The newly created item
	 */
	public static ItemStack forgeItem(String name, String description, int manaPerUse, byte id)
	{
		ItemStack item = new ItemStack(Material.DIAMOND_HOE, 1);
		item.setDurability((short) (id + 20));
		
		ItemMeta im = item.getItemMeta();
		
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.LIGHT_PURPLE + "" + manaPerUse + " mana per use");
		
		// For using new paragraphs without all the hassle
		if(description.contains("\n"))
		{
			String[] desc = description.split("\n");
			for(int i = 0; i < desc.length; i++)
			{
				System.out.println(i);
				lore.add(ChatColor.DARK_GRAY + desc[i]);
			}
		}
		else
		{
			lore.add(ChatColor.DARK_GRAY + description);
		}
		lore.add(ChatColor.DARK_GRAY + "" + id + "");
		im.setLore(lore);
		
		im.setDisplayName(name);
		//im.setUnbreakable(true);
		item.setItemMeta(im);
		return item;
	}
	
	/**
	 * Creates a custom item from the custom item forge with even MORE customizable features (used for armor and stuffs)!
	 * @param name The name of the item
	 * @param description The description of the item in the lore
	 * @param manaPerUse The amount of mana it takes per use
	 * @param id The item id
	 * @param mat The material
	 * @param setDurability If it's durability is fixed (reccomended for re-textures)
	 * @return The created item
	 */
	public static ItemStack forgeItem(String name, String description, int manaPerUse, byte id, Material mat, boolean setDurability)
	{
		ItemStack item = new ItemStack(mat, 1);
		if(setDurability)
			item.setDurability((short) (id + 20));
		
		ItemMeta im = item.getItemMeta();
		
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		ArrayList<String> lore = new ArrayList<>();
		
		lore.add(ChatColor.LIGHT_PURPLE + "" + manaPerUse + " mana per use");
		if(description.contains("\n"))
		{
			String[] desc = description.split("\n");
			for(int i = 0; i < desc.length; i++)
			{
				System.out.println(i);
				lore.add(ChatColor.DARK_GRAY + desc[i]);
			}
		}
		else
		{
			lore.add(ChatColor.DARK_GRAY + description);
		}
		lore.add(ChatColor.DARK_GRAY + "" + id + "");
		im.setLore(lore);
		
		im.setDisplayName(name);
		item.setItemMeta(im);
		return item;
	}
	
	/**
	 * Creates the default kenetic magic holder
	 * @return
	 */
	public static ItemStack magicHolder()
	{
		ItemStack item = new ItemStack(Material.DIAMOND_HOE, 1);
		item.setDurability((short)(Items.MAGIC_HOLDER_ID + 20));
		ItemMeta im = item.getItemMeta();
		
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.LIGHT_PURPLE + "" + CORE_MAX_USES + "/" + CORE_MAX_USES);
		lore.add(ChatColor.LIGHT_PURPLE + "Recharges based off movement");
		lore.add(ChatColor.DARK_GRAY + "Must be in your off hand to use!");
		lore.add(ChatColor.DARK_GRAY + "Used to power all magic things.");
		lore.add(ChatColor.DARK_GRAY + "" + Items.MAGIC_HOLDER_ID);
		im.setLore(lore);
		im.setDisplayName(ChatColor.DARK_PURPLE + "Kinetic Magician's Core");
		item.setItemMeta(im);
		
		return item;
	}

	public static ItemStack forgeItem(String name, String description, String manaPerUse, byte id) 
	{
		ItemStack item = new ItemStack(Material.DIAMOND_HOE, 1);
		item.setDurability((short) (id + 20));
		
		ItemMeta im = item.getItemMeta();
		
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.LIGHT_PURPLE + "" + manaPerUse);
		if(description.contains("\n"))
		{
			String[] desc = description.split("\n");
			for(int i = 0; i < desc.length; i++)
			{
				System.out.println(i);
				lore.add(ChatColor.DARK_GRAY + desc[i]);
			}
		}
		else
		{
			lore.add(ChatColor.DARK_GRAY + description);
		}
		lore.add(ChatColor.DARK_GRAY + "" + id + "");
		im.setLore(lore);
		
		im.setDisplayName(name);
		//im.setUnbreakable(true);
		item.setItemMeta(im);
		return item;
	}
}