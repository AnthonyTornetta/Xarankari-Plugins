package com.cornchipss.custombosses.util.json;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Serializer 
{
	public static String serializeLocation(Location loc)
	{
		return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
	}

	public static Location deserializeLocation(String serialized) 
	{
		String[] split = serialized.split(",");
		try
		{
			return new Location(Bukkit.getWorld(split[0].toLowerCase()), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
		}
		catch(Exception ex)
		{
			throw new IllegalArgumentException("Error parsing '" + serialized + "' into location! Format should be 'world,x,y,z'");
		}
	}
}
