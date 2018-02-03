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

public class BossHandler extends Debug
{
	private List<Boss> loadedBosses = new ArrayList<>();
	private List<LivingBoss> livingBosses = new ArrayList<>();
	private List<BossSpawnArea> bossSpawnAreas;
	private Map<UUID, Integer> bossUUIDsNotLoaded = new HashMap<>();
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
	/**
	 * Creates a boss from a specified entity by checking its UUID against a list of UUIDS that haven't been loaded<br>
	 * If the UUID is not contained in the list, nothing happens
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
				
				debug("createBossFromPreviousEntity", b);
				
				addLivingBoss(b);
				
				uuidToRemove = uuid;
				break;
			}
		}
		
		if(uuidToRemove != null)
			getBossUUIDsNotLoaded().remove(uuidToRemove);
	}
}
