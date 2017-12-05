package com.cornchipss.guilds.ref;

public class Reference 
{
	public static final String VERSION = "1.0";
	public static final String NAME = "Guilds";
	public static final String AUTHOR = "Cornchip";
	
	public static final String CFG_DISPLAY_GUILD_TAB = "guild-tab-display"; // Tab menu
	public static final String CFG_DISPLAY_GUILD_TAG = "guild-tag-display"; // Name tag in chat
	
	public static final String CFG_GUILD_PREFIX = "guild-";
	
	
	
	
	
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
}
