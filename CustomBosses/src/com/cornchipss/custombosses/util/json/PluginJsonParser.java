package com.cornchipss.custombosses.util.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.json.JsonBoss;
import com.cornchipss.custombosses.boss.json.JsonLocations;
import com.cornchipss.custombosses.util.Vector2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class PluginJsonParser 
{
	public static List<Boss> deserializeBosses(String json) throws JsonSyntaxException
	{
		Gson gson = new Gson();
		List<JsonBoss> jsonBosses;
		
		jsonBosses = Arrays.asList(gson.fromJson(json, JsonBoss[].class));
		
		List<Boss> bosses = new ArrayList<>();
		for(JsonBoss jsonBoss : jsonBosses)
			bosses.add(jsonBoss.createBoss());
		
		return bosses;
	}
	
	public static String serializeBosses(List<Boss> bosses)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		List<JsonBoss> bjs = new ArrayList<>();
		
		for(Boss b : bosses)
			bjs.add(JsonBoss.fromBoss(b));
		
		return gson.toJson(bjs);
	}

	public static String serializeLocations(Map<Vector2<Location, Location>, List<Integer>> locs) 
	{
		Map<String, List<Integer>> serializeableData = new HashMap<>();
		
		for(Vector2<Location, Location> loc : locs.keySet())
		{
			List<Integer> bosses = locs.get(loc);
			
			serializeableData.put(loc.getX().getWorld().getName() + "," + loc.getX().getX() + "," + loc.getX().getY() + "," + loc.getX().getZ() + "-" +
									loc.getY().getWorld().getName() + "," + loc.getY().getX() + "," + loc.getY().getY() + "," + loc.getY().getZ(), bosses);
		}
		
		JsonLocations jsonLocs = new JsonLocations();
		jsonLocs.setSerializedLocations(serializeableData);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(jsonLocs);
	}

	public static Map<Vector2<Location, Location>, List<Integer>> deserializeLocations(String json) 
	{
		Map<Vector2<Location, Location>, List<Integer>> parsed = new HashMap<>();
		
		Gson gson = new Gson();
		JsonLocations locsClass;
		
		locsClass = gson.fromJson(json, JsonLocations.class);
		
		Map<String, List<Integer>> serializedLocs = locsClass.getSerializedLocations();
		
		for(String s : serializedLocs.keySet())
		{			
			Location[] locs = new Location[2];
			
			String[] splitApart = s.split("-");
			for(int i = 0; i < 2; i++)
			{
				String[] split = splitApart[i].split(",");
				locs[i] = new Location(Bukkit.getWorld(split[0].toLowerCase()), 
										Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
			}
			
			parsed.put(new Vector2<>(locs[0], locs[1]), serializedLocs.get(s));
		}
		
		return parsed;
	}
}
