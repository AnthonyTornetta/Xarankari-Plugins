package com.cornchipss.main.items;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items 
{
	public static final byte MAGIC_HOLDER_ID = 0;
	public static final byte LIGHTNING_ID = 1;
	public static final byte EXPLOSION_ID = 2;
	public static final byte SHEEP_ID = 3;
	public static final byte SPEED_ID = 4;
	public static final byte REGEN_ID = 5;
	public static final byte INVIS_ID = 6;
	public static final byte HASTE_ID = 7;
	public static final byte HEALTH_ID = 8;
	public static final byte JUMP_ID = 9;
	public static final byte WATER_BREATH_ID = 10;
	public static final byte LEVITATE_ID = 11;
	public static final byte FLY_ID = 12;
	public static final byte NO_FALL_ID = 13;
	public static final byte FIRE_RESIST_ID = 14;
	public static final byte LIGHTNING_RESIST_ID = 15;
	public static final byte MINING_ID = 16;
	public static final byte GUN_ID = 17;
	public static final byte GROWTH_ID = 18;
	public static final byte LESS_GRAV_ID = 19;
	
	private ArrayList<ItemStack> customItems = new ArrayList<>();
	
	public Items()
	{
		// ADD THESE IN ORDER OF ID \\
		customItems.add(CustomItemForge.magicHolder());
		
		customItems.add(CustomItemForge.forgeItem(ChatColor.GOLD + "Wand of Lightning", "Summons lightning where you are looking.", 100, LIGHTNING_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.RED + "Wand of Explosions", "Summons an explostion where you are looking", 100, EXPLOSION_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.WHITE + "Wand of Sheep Summoning", "Summons a.... Sheep", 1000, SHEEP_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.AQUA + "Wand of Speed", "Gives the user a speed II boost", 50, SPEED_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.LIGHT_PURPLE + "Wand of Regeneration", "Gives the user a regen II boost", 50, REGEN_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.WHITE + "Wand of Invisibility", "Gives the user an invisible cloak for 30 seconds", 50, INVIS_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.YELLOW + "Wand of Haste", "Gives the user a haste II boost", 100, HASTE_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.LIGHT_PURPLE + "Wand of Instant Health", "Gives the user two hearts back", 50, HEALTH_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.GREEN + "Wand of Jump Boost", "Gives the user a jump II boost", 100, JUMP_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.BLUE + "Wand of Water Breathing", "Gives the user water breathing\nfor 30 seconds", 50, WATER_BREATH_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.WHITE + "Wand of Levitation", "Gives the user levatation for 5 seconds", 100, LEVITATE_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.WHITE + "Wand of Flight", "Gives the user the ability to fly", "Takes 100 mana/second", FLY_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.GRAY + "Wand of Mining", "Mines the block you look at", 20, MINING_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.BLUE + "Boots of No Fall", "No fall damage is taken when landing", 200, NO_FALL_ID, Material.DIAMOND_BOOTS, false));
		customItems.add(CustomItemForge.forgeItem(ChatColor.RED + "Chestplate of No Fire", "Prevents fire damage", 200, FIRE_RESIST_ID, Material.DIAMOND_CHESTPLATE, false));
		customItems.add(CustomItemForge.forgeItem(ChatColor.AQUA + "Helm of Less Shockingness", "Prevents lightning damage", 200, LIGHTNING_RESIST_ID, Material.DIAMOND_HELMET, false));		
		customItems.add(CustomItemForge.forgeItem(ChatColor.GOLD + "Test Gun", "This is a test gun", 100, GUN_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.GREEN + "Wand of Growth", "Has a bonemeal effect on whatever is right clicked", 50, GROWTH_ID));
		customItems.add(CustomItemForge.forgeItem(ChatColor.AQUA + "Boots of Less Gravity", "Makes gravity 50% less\nstrong and reduces fall damage by half", 1, LESS_GRAV_ID, Material.DIAMOND_BOOTS, false));
	}
	
	public ItemStack getItem(byte id) { return customItems.get(id); }
}