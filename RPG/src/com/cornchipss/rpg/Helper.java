package com.cornchipss.rpg;

public class Helper 
{
	/**
	 * Checks if a string is an integer
	 * @param s The string to check
	 * @return True if it is an integer
	 */
	public static boolean isInteger(String s) 
	{
	    try 
	    { 
	        Integer.parseInt(s);
	    } 
	    catch(NumberFormatException e) 
	    {
	        return false; 
	    } 
	    catch(NullPointerException e)
	    {
	        return false;
	    }
	    // Only got here if we didn't return false
	    return true;
	}
	
}
