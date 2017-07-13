package com.cornchipss.main.helpers;

import org.bukkit.Location;

public class Helper 
{
	/**
	 * Used to clamp a variable between a min and max value
	 * @param var The variable
	 * @param min The minimum value
	 * @param max The maximum value
	 * @return The clamped variable
	 */
	public static int clamp(int var, int min, int max)
	{
		if(var < min)
			var = min;
		if(var > max)
			var = max;
		return var;
	}
	
	/**
	 * Calculates the distance between 2 locations
	 * @param previous The previous location
	 * @param now The new location
	 * @return The distance between
	 */
	public static double vectorDistance(Location previous, Location now)
	{
		double z = now.getZ() - previous.getZ();
		double y = now.getY() - previous.getY();
		double x = now.getX() - previous.getX();
		double distance = Math.sqrt(z * z + y * y + x * x);
		return distance;
	}
}