package com.cornchipss.guilds.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

public class Serializer 
{
	public static String serializeLocation(Location loc)
	{
		if(loc == null)
			return "null";
		
		return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
	}
	
	public static Location deserializeLocation(String loc)
	{
		if(loc == null || loc.equals("null"))
			return null;
		
		String[] split = loc.split(",");
		
		return new Location(Bukkit.getWorld(split[0]), 
							Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
	}
	
	public static Map<String, Vector2<Integer, Integer>> serializeChunk(Chunk c)
	{
		Map<String, Vector2<Integer, Integer>> serialized = new HashMap<>();
		serialized.put(c.getWorld().getName(), 
				new Vector2<>(c.getX(), c.getZ()));
		
		return serialized;
	}
	
	public static List<Chunk> deserializeChunks(Map<String, Vector2<Integer, Integer>> serialized)
	{
		List<Chunk> chunks = new ArrayList<>();
		
		for(String s : serialized.keySet())
		{
			Vector2<Integer, Integer> position = serialized.get(s);
			
			Chunk c = Bukkit.getWorld(s).getChunkAt(position.getX(), position.getY());
			
			chunks.add(c);
		}
		
		return chunks;
	}
}
