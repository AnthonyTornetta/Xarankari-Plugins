package com.cornchipss.guilds.util;

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
	
	public static int iRandomRange(int min, int max)
	{
		return (int)Math.round(Math.random() * (max - min) + min);
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

	public static String firstLetterUpper(String str)
	{
		if(str == null)
			return null;
		if(str.length() == 1)
			return str.toUpperCase();
		
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}
}
