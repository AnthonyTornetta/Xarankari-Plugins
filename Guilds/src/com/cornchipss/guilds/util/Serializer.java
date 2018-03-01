package com.cornchipss.guilds.util;

import java.util.ArrayList;
import java.util.List;

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
	
	public static List<Vector3<String, Integer, Integer>> serializeChunk(Chunk c)
	{
		List<Vector3<String, Integer, Integer>> serialized = new ArrayList<>();
		serialized.add(new Vector3<>(c.getWorld().getName(), c.getX(), c.getZ()));
		return serialized;
	}
	
	public static List<Chunk> deserializeChunks(List<Vector3<String, Integer, Integer>> serialized)
	{
		List<Chunk> chunks = new ArrayList<>();
		
		for(Vector3<String, Integer, Integer> serializedChunk : serialized)
		{			
			Chunk c = Bukkit.getWorld(serializedChunk.getX()).getChunkAt(serializedChunk.getY(), serializedChunk.getZ());
			
			chunks.add(c);
		}
		
		return chunks;
	}
}
