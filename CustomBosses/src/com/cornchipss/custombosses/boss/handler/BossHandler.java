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
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.cornchipss.custombosses.Debug;
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

/**
 * The boss handler handles all boss templates (loaded bosses), bosses in the world (living bosses)<br>
 * It also loads them into the game and spawns them in
 */
public class BossHandler extends Debug
{
	/*
	 * Loaded bosses are templates for how to make a living boss.
	 * They are almost like a holder for the equipment they will wear and how they will function.
	 */
	private List<Boss> loadedBosses = new ArrayList<>();
	
	/*
	 * Living bosses are the actual bosses that are in the world.
	 * These get their information from the Boss template they contain, and use those traits to enhance their stored entity
	 */
	private List<LivingBoss> livingBosses = new ArrayList<>();
	
	/*
	 * These are areas where bosses will spawn at random.
	 */
	private List<BossSpawnArea> bossSpawnAreas;
	
	/*
	 * This stores the UUIDs as well as the ids of bosses who could not be loaded in on a startup.
	 * It will then store the information the boss is loaded in, when it will be deleted and a living boss loaded in it's place.
	 * This is primarily used for the world not finding the entities needed because their chunks aren't loaded
	 */
	private Map<UUID, Integer> bossUUIDsNotLoaded = new HashMap<>();
	
	/*
	 * Just stores the folder path where all the saved data will go to
	 */
	private final File folderPath;
	
	/**
	 * The boss handler handles all boss templates (loaded bosses), bosses in the world (living bosses)<br>
	 * It also loads them into the game and spawns them in
	 * @param folderPath The folder path to read and save the config files to
	 * @throws IOException if there is an error reading/writing the config files
	 */
	public BossHandler(final File folderPath) throws IOException
	{		
		this.folderPath = folderPath;
		initFiles();
		setLoadedBosses(loadBosses());
		
		this.livingBosses = (loadLivingBosses(getLoadedBosses()));
		setSpawnAreas(loadSpawnAreas(getLoadedBosses()));
	}
	
	/**
	 * Saves every configuration file
	 * @throws IOException if there is an error writing the config files
	 */
	public void saveAll() throws IOException
	{
		saveBosses(getLoadedBosses());
		saveLivingBosses(getLivingBosses());
		saveSpawnAreas(getSpawnAreas());
	}
	
	/**
	 * Saves a list of Boss templates to the config file
	 * @param bosses The List of boss templates to save
	 * @throws IOException if there is an error writing to the config file
	 */
	public void saveBosses(final List<Boss> bosses) throws IOException
	{
		List<JsonBoss> jsonBosses = JsonBoss.fromBossList(bosses);
		BufferedWriter bw = new BufferedWriter(new FileWriter(this.getDataFolder() + Reference.BOSSES_CONFIG_NAME));
		bw.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonBosses));
		bw.close();
	}
	
	/**
	 * Saves a list of living bosses to their storage file
	 * @param livingBosses The List of living bosses to save
	 * @throws IOException if there is an error writing to the storage file
	 */
	public void saveLivingBosses(final List<LivingBoss> livingBosses) throws IOException
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
	
	/**
	 * Saves a list of boss spawn areas to their storage file
	 * @param locations The List of areas to save
	 * @throws IOException if there is an error writing to the storage file
	 */
	public void saveSpawnAreas(final List<BossSpawnArea> locations) throws IOException
	{
		String serializedJson = PluginJsonParser.serializeLocations(locations);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(this.getDataFolder() + Reference.LOCATIONS_CONFIG_NAME));
		bw.write(serializedJson);
		bw.close();
	}
	
	/**
	 * Makes sure all the files exist, and makes sure the directories to the configs exist<br>
	 * If the file(s) do not exist, it creates them, and if needed adds starting content to them
	 * @throws IOException if there is an error writing to the files
	 */
	private void initFiles() throws IOException
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

			String json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonBosses);
			bw.write(json);
			
			bw.close();
		}
		
		File locsFile = new File(this.getDataFolder() + Reference.LOCATIONS_CONFIG_NAME);
		if(locsFile.createNewFile())
		{
			saveSpawnAreas(new ArrayList<>());
		}
		
		new File(this.getDataFolder() + Reference.SAVED_ALIVED_BOSSES).createNewFile();
	}
	
	/**
	 * Loads every boss template from the json config file, and returns them in a List
	 * @return The loaded in boss templates
	 * @throws IOException if there is an error reading from the file
	 * @throws JsonParseException if the json is invalid
	 */
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
	
	/**
	 * Loads every living boss from the data file, while also making sure the entity exists, and if not storing it for later
	 * @param loadedBosses The boss templates to read from to form the living bosses
	 * @return The list of living bosses able to be loaded in
	 * @throws IOException if there is an error reading the data file
	 */
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
				Bukkit.getLogger().info("CustomBosses> Invalid boss id in saved alive bosses file (" + id + ") - disabling plugin to avoid weird effects");
				Bukkit.getPluginManager().disablePlugin(Reference.getPlugin());
			}
			LivingBoss b = LivingBoss.deserialize(loadedBosses, serializedAliveBosses);
			if(b != null)
				livingBosses.add(b);
			else
			{
				Entry<Integer, String> es = serializedAliveBosses.entrySet().iterator().next();
				String[] bossInfo = es.getValue().split(";");
				bossUUIDsNotLoaded.put(UUID.fromString(bossInfo[0]), es.getKey());
			}
		}
		br.close();
		
		return livingBosses;
	}
	
	/**
	 * Loads every spawn area from the data file
	 * @param loadedBosses Boss templates to read from to let it know which bosses to spawn
	 * @return A list of boss spawn areas
	 * @throws IOException if there is an error reading from the data file
	 */
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
	
	/**
	 * Creates a boss from a specified entity, while making sure that entity hasn't been loaded in yet<br>
	 * If the entity has been loaded in, or is not on the list to be loaded in, nothing happens
	 * @param e The entity to check
	 */
	public void createBossFromPreviousEntity(Entity e)
	{
		if(!isUUIDNotLoaded(e.getUniqueId()))
			return;
		
		UUID uuidToRemove = null;
		
		for(UUID uuid : getBossUUIDsNotLoaded().keySet())
		{
			if(e.getUniqueId().equals(uuid))
			{
				int bossId = getBossUUIDsNotLoaded().get(uuid);
				
				LivingBoss b = new LivingBoss(Reference.getBossFromId(getLoadedBosses(), bossId), e);
				
				addLivingBoss(b);
				
				uuidToRemove = uuid;
				break;
			}
		}
		
		if(uuidToRemove != null)
			getBossUUIDsNotLoaded().remove(uuidToRemove);
	}
	
	// Getters & Setters //
	
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
	public void addSpawnArea(Vector2<Location, Location> locs, List<Boss> bossesThatWillSpawn) 
	{
		List<BossSpawnArea> spawnAreas = getSpawnAreas();
		spawnAreas.add(new BossSpawnArea(locs, bossesThatWillSpawn));
		setSpawnAreas(spawnAreas);
	}
	public void removeSpawnArea(BossSpawnArea area)
	{
		List<BossSpawnArea> spawnAreas = getSpawnAreas();
		spawnAreas.remove(area);
		setSpawnAreas(spawnAreas);
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
	
	public Map<UUID, Integer> getBossUUIDsNotLoaded() { return this.bossUUIDsNotLoaded; }
	public boolean isUUIDNotLoaded(UUID id)
	{
		return getBossUUIDsNotLoaded().containsKey(id);
	}
}
