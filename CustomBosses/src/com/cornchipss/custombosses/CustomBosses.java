package com.cornchipss.custombosses;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.boss.handler.BossHandler;
import com.cornchipss.custombosses.listener.CornyListener;
import com.cornchipss.custombosses.util.Reference;
import com.cornchipss.custombosses.util.Vector2;
import com.cornchipss.custombosses.util.json.PluginJsonParser;
import com.google.gson.JsonParseException;


// https://dev.bukkit.org/projects/supplies/pages/material-list

public class CustomBosses extends JavaPlugin
{
	private BossHandler bHandler;
	
	@Override
	public void onEnable()
	{
		List<Boss> bosses = new ArrayList<>();
		ItemStack[] armor = new ItemStack[4];
		armor[0] = new ItemStack(Material.DIAMOND_HELMET);
		armor[1] = new ItemStack(Material.DIAMOND_CHESTPLATE);
		armor[2] = new ItemStack(Material.DIAMOND_LEGGINGS);
		armor[3] = new ItemStack(Material.DIAMOND_BOOTS);
		armor[0].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 200);
		
		ItemStack sword = new ItemStack(Material.DIAMOND_AXE);
		sword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
		
		Map<ItemStack, Vector2<Integer, Integer>> drops = new HashMap<>();
		ItemStack i = new ItemStack(Material.ANVIL);
		i.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 17);
		i.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 14);
		drops.put(i, new Vector2<>(3, 20));
				
		Boss b = new Boss(100, EntityType.BLAZE, ChatColor.translateAlternateColorCodes('&', "&4Blaze o Doom"), sword, armor, drops, 10, 1000000, new ItemStack(Material.STICK), 0, 20);
		
		bosses.add(b);
		
		List<Integer> ids = new ArrayList<>();
		ids.add(b.getId());
		
		Map<Vector2<Location, Location>, List<Integer>> locs = new HashMap<>();
		locs.put(new Vector2<>(new Location(this.getServer().getWorlds().get(0), 100, 100, 100), new Location(this.getServer().getWorlds().get(0), 200, 200, 200)), ids);
				
		String bossJson = PluginJsonParser.serializeBosses(bosses);
		String locsJson = PluginJsonParser.serializeLocations(locs);
		
		System.out.println(locsJson);
		
		try 
		{
			this.getDataFolder().mkdirs();
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.getDataFolder() + Reference.BOSSES_CONFIG_NAME));
			bw.write(bossJson);
			bw.close();
			
			bw = new BufferedWriter(new FileWriter(this.getDataFolder() + Reference.LOCATIONS_CONFIG_NAME));
			bw.write(locsJson);
			bw.close();
		} 
		catch (IOException ex)
		{
			ex.printStackTrace();
			return;
		}
		
		try
		{
			bHandler = new BossHandler(PluginJsonParser.deserializeBosses(bossJson));
		}
		catch(JsonParseException ex)
		{
			ex.printStackTrace();
			getLogger().info("Error parsing bosses file! Disabling");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		try
		{
			Map<Vector2<Location, Location>, List<Integer>> acutalLocs = PluginJsonParser.deserializeLocations(locsJson);
			System.out.println(acutalLocs);
		}
		catch(JsonParseException ex)
		{
			ex.printStackTrace();
			getLogger().info("Error parsing locations file! Disabling");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		CornyListener cl = new CornyListener(this.getBossHandler());
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(cl, this);
	}
	
	public void onDisable()
	{		
		List<LivingBoss> livingBosses = getBossHandler().getLivingBosses();
		for(int i = 0; i < livingBosses.size(); i++)
		{
			Map<Integer, String> serializedAliveBosses = livingBosses.get(i).serialize();
			Integer bossId = (Integer) serializedAliveBosses.keySet().toArray()[0];
			getConfig().set(bossId + "", serializedAliveBosses.get(bossId));
		}
		
		
	}
	
	@SuppressWarnings("unused")
	private String loadBossJson() throws IOException
	{
		throw new IOException();
	}

	public BossHandler getBossHandler() { return bHandler; }
}
