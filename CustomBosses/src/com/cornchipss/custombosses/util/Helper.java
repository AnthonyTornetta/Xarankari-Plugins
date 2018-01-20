package com.cornchipss.custombosses.util;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Helper 
{
	public static boolean isWhole(double num)
	{
		return (num % 1) == 0;
	}
	
	public static int sign(double num)
	{
		if (num > 0)
			return 1;
		if (num < 0)
			return -1;
		return 0;
	}
	
	public static double clamp(double val, double min, double max)
	{
		if(val < min)
			val = min;
		if(val > max)
			val = max;
		return val;
	}
	
	public static boolean isInt(String possibleNumber)
	{
		boolean validNumber = false; // We use this to see if the last number checked was a number too
		
		for(int i = 0; i < possibleNumber.length(); i++)
		{
			char checking = possibleNumber.charAt(i);
			
			if(checking == '-' || checking == '+')
			{
				if(possibleNumber.length() == i + 1) // All the number is is '-' or '+'
					return false;
				if(validNumber) // There were already numbers found
					return false;
			}
			else
			{
				for(char j = '0'; j <= '9'; j++)
				{
					if(checking == j)
					{
						validNumber = true;
					}
				}
				if(!validNumber)
					return false;
			}
		}
		return true; // We only got here if no exception was found and it is a number
	}
	
	public static boolean isDouble(String possibleDouble)
	{
		boolean decimalFound = false;
		boolean validNumber = false;
		
		for(int i = 0; i < possibleDouble.length(); i++)
		{
			char checking = possibleDouble.charAt(i);
			
			if(checking == '.')
			{
				if(decimalFound)
					return false; // 2 decimals found
				decimalFound = true;
			}
			else if(checking == '-' || checking == '+')
			{
				if(possibleDouble.length() == i + 1) // All the number is is '-'
					return false;
				if(validNumber || decimalFound) // There were already numbers found
					return false;
			}
			else
			{
				for(char j = '0'; j <= '9'; j++)
				{
					if(checking == j)
					{
						validNumber = true;
					}
				}
				if(!validNumber)
					return false;
			}
		}
		return true; // We only got here if no exception was found and it is a number
	}
	
	// TODO this
	public Location findSpawnableLocation(Location startSearch)
	{		
		return null;
	}
	
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
		
		if(m1.getLore() == null && m2.getLore() != null || m1.getLore() != null && m2.getLore() == null)
			return false;
		
		boolean metasSame = ((m1 == null && m2 == null) || (m1.getDisplayName().equals(m2.getDisplayName()))) && (m1 == null && m2 == null || m1.getLore().equals(m2.getLore()));
		
		return metasSame && i1.getType() == i2.getType();
	}
	
	public static int iRandomRange(int min, int max)
	{
		if(min == max)
			return min;
		assert max > min;
		return (int)(Math.random() * max - min);
	}
	
	public static double dRandomRange(double min, double max) 
	{
		if(Double.compare(min, max) == 0)
			return min;
		assert max > min;
		return Math.random() * max - min;
	}
	
	public static boolean within(double x, double minX, double maxX)
	{
		return x >= minX && x <= maxX;
	}
	
	public static boolean within(double x, double y, double minX, double minY, double maxX, double maxY)
	{
		return within(x, minX, maxX) && within(y, minY, maxY);
	}
	
	public static int round(double d)
	{
		return (int)(d + 0.5);
	}
	
	public static int floor(double d)
	{
		return (int)Math.floor(d);
	}
	
	public static int ceil(double d)
	{
		return (int)Math.ceil(d);
	}

	public static int reverseIndexOf(String str, String lookFor) 
	{
		assert str.length() > lookFor.length();
		
		for(int i = str.length() - lookFor.length(); i >= 0; i--)
		{
			if(str.substring(i, i + lookFor.length()).equals(lookFor))
				return i + 1;
		}
		return -1;
	}
}
