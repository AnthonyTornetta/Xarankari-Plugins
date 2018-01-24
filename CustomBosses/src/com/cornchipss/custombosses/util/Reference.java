package com.cornchipss.custombosses.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.spawner.BossSpawnArea;

public class Reference 
{
	public static final String PLUGIN_NAME = "Custom Bosses";
	public static final String PLUGIN_VERSION = "1.0";
	public static final String PLUGIN_AUTHOR = "Cornchip";
	
	public static final String BOSSES_CONFIG_NAME = "\\bosses.json";
	public static final String LOCATIONS_CONFIG_NAME = "\\spawn-locations.json";
	public static final String SAVED_ALIVED_BOSSES = "\\saved-alive-bosses.dat";
	public static final String BOSS_EGG_MENU_NAME = ChatColor.DARK_GREEN + "Boss Egg Menu";
	public static final String BOSS_LOCATIONS_GUI = ChatColor.DARK_GREEN + "Boss Locations";
	
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
	
	// TODO: Put this in a file at some point
	public static final String DEFAULT_BOSS_JSON =
	"[\r\n" + 
	"  {\r\n" + 
	"    \"startHealth\": 100,\r\n" + 
	"    \"displayName\": \"§4Blaze o Doom\",\r\n" + 
	"    \"mobType\": \"BLAZE\",\r\n" + 
	"    \"equipment\": {\r\n" + 
	"      \"armor\": [\r\n" + 
	"        {\r\n" + 
	"          \"material\": \"DIAMOND_HELMET\",\r\n" + 
	"          \"flags\": [],\r\n" + 
	"          \"enchants\": {\r\n" + 
	"            \"PROTECTION_ENVIRONMENTAL\": 200\r\n" + 
	"          }\r\n" + 
	"        },\r\n" + 
	"        {\r\n" + 
	"          \"material\": \"DIAMOND_CHESTPLATE\",\r\n" + 
	"          \"flags\": [],\r\n" + 
	"          \"enchants\": {\r\n" + 
	"            \"PROTECTION_ENVIRONMENTAL\": 200\r\n" + 
	"          }\r\n" + 
	"        },\r\n" + 
	"        {\r\n" + 
	"          \"material\": \"DIAMOND_LEGGINGS\",\r\n" + 
	"          \"flags\": [],\r\n" + 
	"          \"enchants\": {\r\n" + 
	"            \"PROTECTION_ENVIRONMENTAL\": 200\r\n" + 
	"          }\r\n" + 
	"        },\r\n" + 
	"        {\r\n" + 
	"          \"material\": \"DIAMOND_BOOTS\",\r\n" + 
	"          \"flags\": [],\r\n" + 
	"          \"enchants\": {\r\n" + 
	"            \"PROTECTION_ENVIRONMENTAL\": 200\r\n" + 
	"          }\r\n" + 
	"        }\r\n" + 
	"      ],\r\n" + 
	"      \"hand\": {\r\n" + 
	"        \"material\": \"DIAMOND_AXE\",\r\n" + 
	"        \"flags\": [],\r\n" + 
	"        \"enchants\": {\r\n" + 
	"          \"LOOT_BONUS_MOBS\": 10\r\n" + 
	"        }\r\n" + 
	"      }\r\n" + 
	"    },\r\n" + 
	"    \"drops\": {\r\n" + 
	"      \"GOLDEN_APPLE\": \"4\",\r\n" + 
	"      \"ANVIL\": \"3-20;DAMAGE_ALL#17,FIRE_ASPECT#14\"\r\n" + 
	"    },\r\n" + 
	"    \"damagePerHit\": 1000,\r\n" + 
	"    \"price\": 1000000,\r\n" + 
	"    \"spawnItem\": \"BLAZE_POWDER\",\r\n" + 
	"    \"bossId\": 0,\r\n" + 
	"    \"spawnChance\": 20\r\n" + 
	"  },\r\n" + 
	"  {\r\n" + 
	"    \"startHealth\": 600,\r\n" + 
	"    \"displayName\": \"§0Space Zombie\",\r\n" + 
	"    \"mobType\": \"ZOMBIE\",\r\n" + 
	"    \"equipment\": {\r\n" + 
	"      \"armor\": [\r\n" + 
	"        {\r\n" + 
	"          \"material\": \"GLASS\",\r\n" + 
	"          \"flags\": [],\r\n" + 
	"          \"enchants\": {\r\n" + 
	"            \"THORNS\": 20\r\n" + 
	"          }\r\n" + 
	"        },\r\n" + 
	"        {\r\n" + 
	"          \"material\": \"DIAMOND_CHESTPLATE\",\r\n" + 
	"          \"flags\": [],\r\n" + 
	"          \"enchants\": {\r\n" + 
	"            \"PROTECTION_ENVIRONMENTAL\": 4\r\n" + 
	"          }\r\n" + 
	"        },\r\n" + 
	"        {\r\n" + 
	"          \"material\": \"DIAMOND_LEGGINGS\",\r\n" + 
	"          \"flags\": [],\r\n" + 
	"          \"enchants\": {\r\n" + 
	"            \"PROTECTION_ENVIRONMENTAL\": 4\r\n" + 
	"          }\r\n" + 
	"        },\r\n" + 
	"        {\r\n" + 
	"          \"material\": \"DIAMOND_BOOTS\",\r\n" + 
	"          \"flags\": [],\r\n" + 
	"          \"enchants\": {\r\n" + 
	"            \"PROTECTION_ENVIRONMENTAL\": 4\r\n" + 
	"          }\r\n" + 
	"        }\r\n" + 
	"      ],\r\n" + 
	"      \"hand\": {\r\n" + 
	"        \"material\": \"DIAMOND_SWORD\",\r\n" + 
	"        \"flags\": [],\r\n" + 
	"        \"enchants\": {\r\n" + 
	"          \"LOOT_BONUS_MOBS\": 10,\r\n" + 
	"          \"DAMAGE_ALL\": 30\r\n" + 
	"        }\r\n" + 
	"      }\r\n" + 
	"    },\r\n" + 
	"    \"drops\": {\r\n" + 
	"      \"ROTTEN_FLESH\": \"1000-1000\"\r\n" + 
	"    },\r\n" + 
	"    \"damagePerHit\": -1,\r\n" + 
	"    \"price\": 1000000,\r\n" + 
	"    \"spawnItem\": \"ROTTEN_FLESH\",\r\n" + 
	"    \"bossId\": 1,\r\n" + 
	"    \"spawnChance\": 80\r\n" + 
	"  }\r\n" + 
	"]";
}
