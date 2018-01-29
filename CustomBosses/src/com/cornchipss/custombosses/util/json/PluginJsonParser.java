package com.cornchipss.custombosses.util.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.json.JsonBoss;
import com.cornchipss.custombosses.boss.json.JsonLocations;
import com.cornchipss.custombosses.boss.spawner.BossSpawnArea;
import com.cornchipss.custombosses.util.Reference;
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
		
		if(json.isEmpty())
			System.out.println("JSON IS REMPTY -- I REPEAT -- JSON ISSSS EMPTY!");
		
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

	public static String serializeLocations(List<BossSpawnArea> areas) 
	{
		Map<String, List<Integer>> serializeableData = new HashMap<>();
		
		for(BossSpawnArea area : areas)
		{
			try
			{
				List<Integer> bosses = Reference.getBossIds(area.getBosses());
				
				serializeableData.put(Serializer.serializeLocation(area.getLocationX()) + "/" + Serializer.serializeLocation(area.getLocationY()), bosses);
			}
			catch(NullPointerException ex)
			{
				Bukkit.getServer().getLogger().info("CustomBosses> Invalid boss id in locations file! Disabling to avoid any damage");
				Bukkit.getPluginManager().disablePlugin(Reference.getPlugin());
			}
		}
		
		JsonLocations jsonLocs = new JsonLocations();
		jsonLocs.setSerializedLocations(serializeableData);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(jsonLocs);
	}
	
	public static List<BossSpawnArea> deserializeLocations(List<Boss> loadedBosses, String json) 
	{
		List<BossSpawnArea> parsed = new ArrayList<>();
		
		Gson gson = new Gson();
		JsonLocations locsClass;
		
		locsClass = gson.fromJson(json, JsonLocations.class);
		
		if(locsClass == null)
			return new ArrayList<>();
		
		Map<String, List<Integer>> serializedLocs = locsClass.getSerializedLocations();
		
		for(String s : serializedLocs.keySet())
		{
			String[] locationsSerialized = s.split("/");
			
			parsed.add(new BossSpawnArea(new Vector2<>(Serializer.deserializeLocation(locationsSerialized[0]), 
														Serializer.deserializeLocation(locationsSerialized[1])), 
														Reference.getBossesFromIds(loadedBosses, serializedLocs.get(s))));
		}
		
		return parsed;
	}

	public static Map<String, Integer> serializeEnchants(Map<Enchantment, Integer> enchantments) 
	{
		Map<String, Integer> serialized = new HashMap<>();
		for(Enchantment e : enchantments.keySet())
		{
			serialized.put(e.getName(), enchantments.get(e));
		}
		return serialized;
	}
}
