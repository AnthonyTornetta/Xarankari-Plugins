package com.cornchipss.custombosses.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Reference 
{
	public static final String PLUGIN_NAME = "Custom Bosses";
	public static final String PLUGIN_VERSION = "1.0";
	public static final String PLUGIN_AUTHOR = "Cornchip";
	
	public static final String BOSSES_CONFIG_NAME = "\\bosses.json";
	public static final String LOCATIONS_CONFIG_NAME = "\\spawn-locations.json";
	
	/**
	 * Compares two itemstacks to see if they are basically equivalent (ignores amount, durability, and other various things)
	 * @param i1 The first itemstack
	 * @param i2 The second itemstack
	 * @return True if they are basically equivalent, false if not
	 */
	public static boolean equiv(ItemStack i1, ItemStack i2) 
	{
		ItemMeta m1 = i1.getItemMeta();
		ItemMeta m2 = i2.getItemMeta();
		
		if(m1 == null && m2 != null || m1 != null && m2 == null)
			return false;
		
		boolean metasSame = (m1 == null && m2 == null) || (m1.getDisplayName().equals(m2.getDisplayName()) && m1.getLore().equals(m2.getLore()));
		
		return metasSame && i1.getType() == i2.getType();
	}
}
