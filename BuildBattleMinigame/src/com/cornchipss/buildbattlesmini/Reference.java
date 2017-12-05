package com.cornchipss.buildbattlesmini;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Reference 
{
	public static final String VERSION = "1.0";
	public static final String NAME = "Build Battle Minigame";
	public static final String AUTHOR = "Cornchip";
	
	public static final String CFG_DURATION = "duration";
	public static final String CFG_THEMES = "themes";
	public static final String BB_CMD_WINDOW_TITLE = ChatColor.DARK_GREEN + "Build Battle Commands";
	public static final String BB_CMD_SET_WINDOW_TITLE = ChatColor.DARK_GREEN + "Build Battle Set Commands";
		
	public static boolean locWithin(Location loc, Location bottomLeft, Location topRight)
	{
		if(!loc.getWorld().equals(bottomLeft.getWorld()) || !loc.getWorld().equals(topRight.getWorld()))
		{
			return false; // Dif worlds
		}
		
		double x = loc.getX(), y = loc.getY(), z = loc.getZ();
		if (x >= bottomLeft.getX() &&
			y >= bottomLeft.getY() &&
			z >= bottomLeft.getZ())
		{
			if (x <= topRight.getX() &&
				y <= topRight.getY() &&
				z <= topRight.getZ())
			{
				return true; // It is within the 2 locations
			}
		}
		return false; // It isnt
	}
	
	public static boolean isInt(String test)
	{
		try
		{
			Integer.parseInt(test);
			return true;
		}
		catch(NumberFormatException | NullPointerException ex)
		{
			return false;
		}
	}
	
	public static boolean isDouble(String test)
	{
		try
		{
			Double.parseDouble(test);
			return true;
		}
		catch(NumberFormatException | NullPointerException ex)
		{
			return false;
		}
	}
}