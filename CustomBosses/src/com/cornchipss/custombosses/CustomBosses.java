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

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.json.JsonBoss;
import com.cornchipss.custombosses.boss.json.equipment.ArmorJson;
import com.cornchipss.custombosses.boss.json.equipment.BossEquipmentJson;
import com.cornchipss.custombosses.boss.json.equipment.HandJson;
import com.cornchipss.custombosses.listener.CornyListener;
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
		
		try {
			serializeBoss();
			deserializeBoss();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
	private static void serializeBoss() throws IOException
	{
		//int startHealth, String displayName, String mobType, BossEquipmentJson equipment
		List<String> handLore = new ArrayList<>();
		handLore.add("is good");
		handLore.add("is fun");
		
		Map<String, Integer> enchants = new HashMap<>();
		enchants.put("FIRE_ASPECT", 10);
		enchants.put("DAMAGE_ALL", 8);
		
		List<String> flags = new ArrayList<>();
		flags.add("unbreakable");
		flags.add("chilly");
		
		Map<String, String> drops = new HashMap<>();
		drops.put("COBBLESTONE", "3");
		drops.put("DIAMOND", "1-5");
		drops.put("IRON_INGOT", "200");
		
		HandJson hand = new HandJson("DIAMOND_SWORD", "Kewl Sword", handLore, enchants, flags);
		List<ArmorJson> aj = new ArrayList<>();
		aj.add(new ArmorJson("DIAMOND_HELMET", "DISPLAY NAME", new ArrayList<>(), new HashMap<>(), new ArrayList<>()));
		aj.add(new ArmorJson("DIAMOND_CHESTPLATE", "DISPLAY NAME CHESTPLATE", new ArrayList<>(), new HashMap<>(), new ArrayList<>()));
		aj.add(new ArmorJson("DIAMOND_LEGGINGS", "DISPLAY NAME LEGS", new ArrayList<>(), new HashMap<>(), new ArrayList<>()));
		aj.add(new ArmorJson("DIAMOND_BOOTS", "DISPLAY NAME BOOTIES", new ArrayList<>(), new HashMap<>(), new ArrayList<>()));
		BossEquipmentJson bj = new BossEquipmentJson(aj, hand);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		List<JsonBoss> bosses = new ArrayList<>();
		bosses.add(new JsonBoss(100, "Kewl Kid", "ZOMBIE", bj, drops));
		bosses.add(new JsonBoss(200, "Kewl Kid 2.0", "SKELETON", bj, drops));
		bosses.add(new JsonBoss(300, "Kewl Kid 2.1", "WITHER_SKELETON", bj, drops));
		
		String json = gson.toJson(bosses);
		System.out.println(json);
		BufferedWriter bw = new BufferedWriter(new FileWriter("bosses.json"));
		bw.write(json);
		bw.close();
	}
}
