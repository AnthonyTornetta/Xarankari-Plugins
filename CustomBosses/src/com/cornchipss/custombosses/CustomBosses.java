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

import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.custombosses.boss.json.JsonBoss;
import com.cornchipss.custombosses.boss.json.equipment.ArmorJson;
import com.cornchipss.custombosses.boss.json.equipment.BossEquipmentJson;
import com.cornchipss.custombosses.boss.json.equipment.HandJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CustomBosses extends JavaPlugin
{	
	@Override
	public void onEnable()
	{
		
	}
	
	public void onDisable()
	{
		
	}
	
	public static void main(String[] args) throws IOException
	{
		String json = "";
		BufferedReader br = new BufferedReader(new FileReader("./res/boss-file-default.json"));
		for(String s = br.readLine(); s != null; s = br.readLine())
		{
			json += s + "\n";
		}
		
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		
//		List<Test> tests = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Test.class));
//		for(Test t : tests)
//		{
//			System.out.println(t);
//		}
		
		//serializeBoss();
		deserializeBoss();
	}
	
	private static void serializeUserNested() throws IOException
	{
		//String street, String houseNumber, String city, String country
		UserAddress address = new UserAddress("Main Street", "322", "adsf", "asdf");
		UserNested un = new UserNested("Normy", "normy@jim.com", 27, true, address);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(un);
		System.out.println(json);
		BufferedWriter bw = new BufferedWriter(new FileWriter("bosses.json"));
		bw.write(json);
		bw.close();
	}
	
	private static void deserializeUserNested() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("bosses.json"));
		String json = "";
		for(String s = br.readLine(); s != null; s = br.readLine())
		{
			json += s;
		}
		
		Gson gson = new Gson();
		UserNested un = gson.fromJson(json, UserNested.class);
		System.out.println(un);
	}
	
	private static void deserializeBoss() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("bosses.json"));
		String json = "";
		for(String s = br.readLine(); s != null; s = br.readLine())
		{
			json += s;
		}
		
		Gson gson = new Gson();
		List<JsonBoss> jsonBawses = Arrays.asList(gson.fromJson(json, JsonBoss[].class));
		System.out.println(jsonBawses);
	}
	
	private static void serializeBoss() throws IOException
	{
		//int startHealth, String displayName, String mobType, BossEquipmentJson equipment
		List<String> handLore = new ArrayList<>();
		handLore.add("is good");
		handLore.add("is fun");
		
		Map<String, Integer> enchants = new HashMap<>();
		enchants.put("FIRE_ASPECT", 10);
		enchants.put("SHARPNESS", 8);
		
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
