package com.cornchipss.custombosses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.json.JsonBoss;
import com.cornchipss.custombosses.listener.CornyListener;
import com.cornchipss.custombosses.util.Vector2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;


// https://dev.bukkit.org/projects/supplies/pages/material-list

public class CustomBosses extends JavaPlugin
{	
	@Override
	public void onEnable()
	{
		CornyListener cl = new CornyListener();
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(cl, this);
		
		ItemStack[] armor = new ItemStack[4];
		armor[0] = new ItemStack(Material.DIAMOND_HELMET);
		armor[1] = new ItemStack(Material.DIAMOND_CHESTPLATE);
		armor[2] = new ItemStack(Material.DIAMOND_LEGGINGS);
		armor[3] = new ItemStack(Material.DIAMOND_BOOTS);
		armor[0].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 200);
		
		ItemStack sword = new ItemStack(Material.DIAMOND_AXE);
		sword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
		
		Map<ItemStack, Vector2<Integer, Integer>> drops = new HashMap<>();
		drops.put(new ItemStack(Material.ANVIL), new Vector2<>(3, 20));
		
		Boss b = new Boss(100, EntityType.BLAZE, "&4Blaze o Doom", sword, armor, drops);
		
		try {
			serializeBoss(b);
			deserializeBoss();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onDisable()
	{
		
	}
	
	private static boolean deserializeBoss() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("bosses.json"));
		String json = "";
		for(String s = br.readLine(); s != null; s = br.readLine())
		{
			json += s;
		}
		br.close();
		
		Gson gson = new Gson();
		List<JsonBoss> jsonBawses;
		try
		{
			jsonBawses = Arrays.asList(gson.fromJson(json, JsonBoss[].class));
		}
		catch(JsonSyntaxException ex)
		{
			return false;
		}
				
		for(JsonBoss baws : jsonBawses)
		{
			Boss b = baws.createBoss();
			System.out.println(b);
		}
		
		return true;
	}
	
	private static void serializeBoss(Boss b) throws IOException
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		List<JsonBoss> bjs = new ArrayList<>();
		JsonBoss bj = JsonBoss.fromBoss(b);
		bjs.add(bj);
		
		String json = gson.toJson(bjs);
		
		System.out.println(json);
		BufferedWriter bw = new BufferedWriter(new FileWriter("bosses.json"));
		bw.write(json);
		bw.close();
	}
}
