package com.cornchipss.custombosses.boss.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.json.JsonBoss;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class BossHandler 
{
	private List<Boss> loadedBosses;
	private List<Boss> aliveBosses;
	
	public List<Boss> deserializeBosses(String json)
	{
		Gson gson = new Gson();
		List<JsonBoss> jsonBosses;
		
		try
		{
			jsonBosses = Arrays.asList(gson.fromJson(json, JsonBoss[].class));
		}
		catch(JsonSyntaxException ex)
		{
			return null;
		}
		
		List<Boss> bosses = new ArrayList<>();
		for(JsonBoss jsonBoss : jsonBosses)
			bosses.add(jsonBoss.createBoss());
		
		return bosses;
	}
	
	public String serializeBosses(List<Boss> bosses)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		List<JsonBoss> bjs = new ArrayList<>();
		
		for(Boss b : bosses)
			bjs.add(JsonBoss.fromBoss(b));
		
		return gson.toJson(bjs);
	}

		/*ItemStack[] armor = new ItemStack[4];
		armor[0] = new ItemStack(Material.DIAMOND_HELMET);
		armor[1] = new ItemStack(Material.DIAMOND_CHESTPLATE);
		armor[2] = new ItemStack(Material.DIAMOND_LEGGINGS);
		armor[3] = new ItemStack(Material.DIAMOND_BOOTS);
		armor[0].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 200);
		
		ItemStack sword = new ItemStack(Material.DIAMOND_AXE);
		sword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
		
		Map<ItemStack, Vector2<Integer, Integer>> drops = new HashMap<>();
		drops.put(new ItemStack(Material.ANVIL), new Vector2<>(3, 20));
		
		Boss b = new Boss(100, EntityType.BLAZE, "&4Blaze o Doom", sword, armor, drops);*/
	
	public List<Boss> getLoadedBosses()
	{
		List<Boss> clone = new ArrayList<>();
		for(Boss b : loadedBosses)
			clone.add(b);
		return clone;
	}
	
	public List<Boss> getSpawnedBosses()
	{
		List<Boss> clone = new ArrayList<>();
		for(Boss b : aliveBosses)
			clone.add(b);
		return clone;
	}
	
	public void addAliveBoss(Boss b)
	{
		aliveBosses.add(b);
	}
	public void removeAliveBoss(Boss b)
	{
		aliveBosses.remove(b);
	}
}
