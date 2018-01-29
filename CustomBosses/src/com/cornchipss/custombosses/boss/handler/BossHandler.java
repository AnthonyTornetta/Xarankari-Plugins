package com.cornchipss.custombosses.boss.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.boss.json.JsonBoss;
import com.cornchipss.custombosses.boss.json.equipment.BossEquipmentJson;
import com.cornchipss.custombosses.boss.json.equipment.ItemStackJson;
import com.cornchipss.custombosses.boss.json.equipment.ItemStackJsonAmountRange;
import com.cornchipss.custombosses.boss.spawner.BossSpawnArea;
import com.cornchipss.custombosses.util.Reference;
import com.cornchipss.custombosses.util.Vector2;
import com.cornchipss.custombosses.util.json.PluginJsonParser;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

public class BossHandler 
{
	private List<Boss> loadedBosses = new ArrayList<>();
	private List<LivingBoss> livingBosses = new ArrayList<>();
	private List<BossSpawnArea> bossSpawnAreas;
	private final File folderPath;
	
	public BossHandler(File folderPath) throws IOException
	{		
		this.folderPath = folderPath;
		initFiles();
		setLoadedBosses(loadBosses());
		
		this.livingBosses = (loadLivingBosses(getLoadedBosses()));
		setSpawnAreas(loadSpawnAreas(getLoadedBosses()));
	}

	public void saveAll() throws IOException
	{
		saveBosses(getLoadedBosses());
		saveLivingBosses(getLivingBosses());
		saveSpawnAreas(getSpawnAreas());
	}
	
	public void saveBosses(List<Boss> bosses) throws IOException
	{
		List<JsonBoss> jsonBosses = JsonBoss.fromBossList(bosses);
		BufferedWriter bw = new BufferedWriter(new FileWriter(this.getDataFolder() + Reference.BOSSES_CONFIG_NAME));
		bw.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonBosses));
		bw.close();
	}
	
	public void saveLivingBosses(List<LivingBoss> livingBosses) throws IOException
	{		
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.getDataFolder() + Reference.SAVED_ALIVED_BOSSES));
			
			for(int i = 0; i < livingBosses.size(); i++)
			{
				Map<Integer, String> serializedAliveBosses = livingBosses.get(i).serialize();
				if(serializedAliveBosses == null)
					continue;
				Integer bossId = (Integer) serializedAliveBosses.keySet().toArray()[0];
				bw.write(bossId + ":" + serializedAliveBosses.get(bossId));
				bw.newLine();
			}
			
			bw.close();
	}
	
	public void saveSpawnAreas(List<BossSpawnArea> locations) throws IOException
	{
		String serializedJson = PluginJsonParser.serializeLocations(locations);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(this.getDataFolder() + Reference.LOCATIONS_CONFIG_NAME));
		bw.write(serializedJson);
		bw.close();
	}
	
	public void initFiles() throws IOException
	{
		this.getDataFolder().mkdirs();
		File bossFile = new File(this.getDataFolder() + Reference.BOSSES_CONFIG_NAME);
		
		if(bossFile.createNewFile())
		{
			List<ItemStackJson> aj = new ArrayList<>();
			Map<String, Integer> enchants = new HashMap<>();
			enchants.put("PROTECTION_ENVIRONMENTAL", 200);
			aj.add(new ItemStackJson(Material.DIAMOND_HELMET.name(), "Helmet ig uess", new ArrayList<>(), enchants));
			
			Map<String, Integer> hE = new HashMap<>();
			hE.put("DAMAGE_ALL", 200);
			ItemStackJson hand = new ItemStackJson(Material.DIAMOND_SWORD.name(), "Helmet ig uess", new ArrayList<>(), hE);
			BossEquipmentJson be = new BossEquipmentJson(aj, hand);
			
			List<ItemStackJsonAmountRange> drops = new ArrayList<>();
			drops.add(new ItemStackJsonAmountRange("GOLDEN_APPLE", "Super Apple", new ArrayList<>(), hE, new Vector2<>(4, 8)));
			
			Map<String, Integer> dropBlazerodEnchants = new HashMap<>();
			dropBlazerodEnchants.put("DAMAGE_ALL", 17);
			dropBlazerodEnchants.put("FIRE_ASPECT", 14);
			
			drops.add(new ItemStackJsonAmountRange("BLAZE_ROD", ChatColor.RED + "Blaze Rod of Doom", new ArrayList<>(), dropBlazerodEnchants, new Vector2<>(1, 1)));
			
			JsonBoss b = new JsonBoss(100, ChatColor.GOLD + "Blaze of Death", EntityType.BLAZE.name(), be, drops, 100, Material.BLAZE_POWDER.name(), 0, 5000, 100, "", "%player% has killed the mythical %boss%!");
			
			List<JsonBoss> jsonBosses = new ArrayList<>();
			jsonBosses.add(b);
			System.out.println("GOT HERE");
			System.out.println("ASDF: " + jsonBosses);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(bossFile));
			//bw.write(Reference.DEFAULT_BOSS_JSON);
			System.out.println(jsonBosses);
			String json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonBosses);
			bw.write(json);
			if(json.isEmpty())
			{
				System.out.println("BAD BOI");
			}
			System.out.println(json);
			bw.close();
		}
		
		File locsFile = new File(this.getDataFolder() + Reference.LOCATIONS_CONFIG_NAME);
		if(locsFile.createNewFile())
		{
			saveSpawnAreas(new ArrayList<>());
		}
		
		new File(this.getDataFolder() + Reference.SAVED_ALIVED_BOSSES).createNewFile();
	}
	
	public List<Boss> loadBosses() throws IOException, JsonParseException
	{		
		BufferedReader br = new BufferedReader(new FileReader(this.getDataFolder() + Reference.BOSSES_CONFIG_NAME));
		String json = "";
		for(String line = br.readLine(); line != null; line = br.readLine())
		{
			json += line + "\n";
		}
		
		br.close();
		
		return PluginJsonParser.deserializeBosses(json);
	}
	
	public List<LivingBoss> loadLivingBosses(final List<Boss> loadedBosses) throws IOException
	{
		List<LivingBoss> livingBosses = new ArrayList<>();
		
		BufferedReader br = new BufferedReader(new FileReader(this.getDataFolder() + Reference.SAVED_ALIVED_BOSSES));
		for(String s = br.readLine(); s != null; s = br.readLine())
		{
			if(s.trim().isEmpty())
				continue;
			
			Map<Integer, String> serializedAliveBosses = new HashMap<>();
			String[] split = s.split(":");
			int id = Integer.parseInt(split[0]);
			serializedAliveBosses.put(id, split[1]);
			if(!Reference.getBossIds(getLoadedBosses()).contains(id))
			{
				Bukkit.getLogger().info("CustomBosses> Invalid boss id in saved alive bosses file (" + id + ") - disabling to avoid damage");
				Bukkit.getPluginManager().disablePlugin(Reference.getPlugin());
			}
			livingBosses.add(LivingBoss.deserialize(loadedBosses, serializedAliveBosses));
		}
		br.close();
		
		return livingBosses;
	}
	
	public List<BossSpawnArea>loadSpawnAreas(final List<Boss> loadedBosses) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(getDataFolder() + Reference.LOCATIONS_CONFIG_NAME));
		String json = "";
		for(String line = br.readLine(); line != null; line = br.readLine())
		{
			json += line + "\n";
		}
		br.close();
				
		List<BossSpawnArea> locsDeserialized = PluginJsonParser.deserializeLocations(loadedBosses, json);
		
		return locsDeserialized;
	}
	
	private final File getDataFolder() { return folderPath; }

	public void addLivingBoss(LivingBoss b) 
	{ 
		livingBosses.add(b);
		try
		{
			saveLivingBosses(getLivingBosses());
		} 
		catch (IOException e) 
		{ e.printStackTrace(); }
	}
	public void removeLivingBoss(LivingBoss b) 
	{ 
		livingBosses.remove(b);
		try 
		{
			saveLivingBosses(getLivingBosses());
		} 
		catch (IOException e) 
		{ e.printStackTrace(); }
	}
	public void setLivingBosses(List<LivingBoss> livingBosses) 
	{ 
		this.livingBosses = livingBosses;
		try 
		{
			saveLivingBosses(getLivingBosses());
		} 
		catch (IOException e) 
		{ e.printStackTrace(); }
	}
	public List<LivingBoss> getLivingBosses()
	{
		List<LivingBoss> clone = new ArrayList<>();
		for(LivingBoss b : livingBosses)
			clone.add(b);
		return clone;
	}
	
	public List<BossSpawnArea> getSpawnAreas() 
	{ 
		return Reference.cloneBossAreas(bossSpawnAreas); 
	}
	public void setSpawnAreas(List<BossSpawnArea> bossSpawnAreas) 
	{
		this.bossSpawnAreas = bossSpawnAreas;
		try 
		{
			saveSpawnAreas(getSpawnAreas());
		}
		catch (IOException e) 
		{ e.printStackTrace(); }
	}
	public void addSpawnArea(Vector2<Location, Location> locs) 
	{
	}
	
	public List<Boss> getLoadedBosses()
	{
		return Reference.cloneBosses(loadedBosses);
	}
	public void setLoadedBosses(List<Boss> loadBosses) { this.loadedBosses = loadBosses; }
	
	public Boss getBossFromId(int id)
	{
		for(Boss b : getLoadedBosses())
		{
			if(b.getId() == id)
				return b;
		}
		return null;
	}
}
