package com.cornchipss.custombosses;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import com.cornchipss.custombosses.boss.handler.BossHandler;
import com.cornchipss.custombosses.listener.CornyListener;
import com.cornchipss.custombosses.util.Vector2;


// https://dev.bukkit.org/projects/supplies/pages/material-list

public class CustomBosses extends JavaPlugin
{
	private BossHandler bHandler = new BossHandler();
	
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
				
		Boss b = new Boss(100, EntityType.BLAZE, "&4Blaze o Doom", sword, armor, drops, 20, 1000000, new ItemStack(Material.BEACON), 0);
		
		bosses.add(b);
		
		String json = bHandler.serializeBosses(bosses);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("bosses.json"));
			bw.write(json);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Boss> bees = bHandler.deserializeBosses(json);
		System.out.println(bees);
		
		CornyListener cl = new CornyListener(this);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(cl, this);
	}
	
	public void onDisable()
	{
	}
	
	private String loadBossJson() throws IOException
	{
		//File f = new File();
		//BufferedReader br = new BufferedReader(new FileReader())
		throw new IOException();
	}

	public List<Boss> getBosses() 
	{
		
		return null;
	}
}
