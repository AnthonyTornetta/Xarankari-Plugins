package com.cornchipss.custombosses.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.spawner.BossSpawnArea;

public class Reference 
{
	public static final String BOSSES_CONFIG_NAME    = File.separator + "bosses.json";
	public static final String LOCATIONS_CONFIG_NAME = File.separator + "spawn-locations.json";
	public static final String SAVED_ALIVED_BOSSES   = File.separator + "saved-alive-bosses.dat";
	
	public static final String BOSS_EGG_MENU_NAME   = ChatColor.DARK_GREEN + "Boss Egg Menu";
	public static final String BOSS_LOCATIONS_GUI   = ChatColor.DARK_GREEN + "Boss Locations";
	public static final String BOSS_SPAWN_AREAS_GUI = ChatColor.DARK_GREEN + "Boss Spawn Areas";
	public static final String BOSS_SPAWN_AREA_GUI  = ChatColor.DARK_GREEN + "Boss Spawn Area";
	
	public static final String PLUGIN_NAME    = "Custom Bosses";
	public static final String PLUGIN_VERSION = "1.0";
	public static final String PLUGIN_AUTHOR  = "Cornchip";
	
	
	
	public static List<Boss> cloneBosses(List<Boss> bosses)
	{
		List<Boss> clone = new ArrayList<>();
		for(Boss b : bosses)
			clone.add(b);
		return clone;
	}
	
	public static List<BossSpawnArea> cloneBossAreas(List<BossSpawnArea> bossSpawnAreas) 
	{
		List<BossSpawnArea> locs = new ArrayList<>();
		for(BossSpawnArea a : bossSpawnAreas)
			locs.add(a.clone());
		return locs;
	}
	
	public static List<Integer> getBossIds(List<Boss> bosses)
	{
		List<Integer> ids = new ArrayList<>();
		for(Boss b : bosses)
			ids.add(b.getId());
		return ids;
	}
	
	public static List<Boss> getBossesFromIds(List<Boss> loadedBosses, List<Integer> ids) 
	{
		List<Boss> bosses = new ArrayList<>();
		for(int id : ids)
		{
			Boss b = getBossFromId(loadedBosses, id);
			if(b == null)
			{
				Bukkit.getLogger().info("CustomBosses> Invalid id in spawn locations file - disabling to avoid damage");
				Bukkit.getPluginManager().disablePlugin(Reference.getPlugin());
			}
			bosses.add(b);
		}
		return bosses;
	}
	
	public static Boss getBossFromId(List<Boss> loadedBosses, int id)
	{
		for(Boss b : loadedBosses)
			if(b.getId() == id)
				return b;
		return null;
	}
	
	public static Plugin getPlugin()
	{
		return Bukkit.getPluginManager().getPlugin("CustomBosses");
	}
}
