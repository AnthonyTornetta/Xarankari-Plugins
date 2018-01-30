package com.cornchipss.custombosses.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.boss.spawner.BossSpawnArea;
import com.cornchipss.custombosses.util.Reference;
import com.cornchipss.custombosses.util.mobheads.Head.Mob;

public class InventoryHelper 
{
	public static final int BOSS_AREA_ITEMSTACK_SLOT_POSITION = 4;
	
	public static void openBossEggGUI(List<Boss> bosses, Player p) 
	{
		final int ROWS = 2 + (int)Math.ceil(bosses.size() / 9.0);
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.BOSS_EGG_MENU_NAME);
		
		InventoryHelper.genBorders(ROWS, inv);
		
		for(int i = 0; i < bosses.size(); i++)
		{
			inv.setItem(10 + i, bosses.get(i).getSpawnItem());
		}
		
		p.openInventory(inv);
	}
	
	public static void openBossSpawnAreasGUI(List<BossSpawnArea> areas, Player p)
	{
		final int ROWS = (int)Math.ceil(areas.size() / 9.0);
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.BOSS_SPAWN_AREAS_GUI);
		
		for(int i = 0; i < areas.size(); i++)
		{
			BossSpawnArea b = areas.get(i);
			
			Location corner1 = b.getLocationX();
			Location corner2 = b.getLocationY();
			
			ItemStack locationInfo = new ItemStack(Material.MAP);
			ItemMeta im = locationInfo.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "");
			for(int j = 0; j < b.getBosses().size(); j++)
			{
				im.setDisplayName(im.getDisplayName() + b.getBosses().get(j).getId());
				if(j + 1 != b.getBosses().size())
					im.setDisplayName(im.getDisplayName() + ", ");
			}
			
			List<String> lore = new ArrayList<>();
			lore.add(corner1.getX() + ", " + corner1.getY() + ", " + corner1.getZ() + "; " + corner1.getWorld().getName());
			lore.add(corner2.getX() + ", " + corner2.getY() + ", " + corner2.getZ() + "; " + corner2.getWorld().getName());
			im.setLore(lore);
			
			locationInfo.setItemMeta(im);
			
			inv.setItem(i, locationInfo);
		}
		
		if(areas.size() > 0)
		{
			p.openInventory(inv);
		}
		else
		{
			p.sendMessage(ChatColor.GOLD + "There are no spawn areas to show :(");
		}
	}
	
	public static void openBossSpawnAreaGUI(BossSpawnArea area, Player p)
	{
		final int ROWS = 1;
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.BOSS_SPAWN_AREA_GUI);
		
		Location corner1 = area.getLocationX();
		Location corner2 = area.getLocationY();
		
		ItemStack locationInfo = new ItemStack(Material.MAP);
		ItemMeta im = locationInfo.getItemMeta();
		
		im.setDisplayName(ChatColor.GOLD + "");
		for(int j = 0; j < area.getBosses().size(); j++)
		{
			
			im.setDisplayName(im.getDisplayName() + area.getBosses().get(j).getId());
			if(j + 1 != area.getBosses().size())
				im.setDisplayName(im.getDisplayName() + ", ");
		}
		
		List<String> lore = new ArrayList<>();
		lore.add(corner1.getX() + ", " + corner1.getY() + ", " + corner1.getZ() + "; " + corner1.getWorld().getName());
		lore.add(corner2.getX() + ", " + corner2.getY() + ", " + corner2.getZ() + "; " + corner2.getWorld().getName());
		im.setLore(lore);
		
		locationInfo.setItemMeta(im);
		
		inv.setItem(BOSS_AREA_ITEMSTACK_SLOT_POSITION, locationInfo);
		
		ItemStack keep = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)13);
		ItemMeta keepMeta = keep.getItemMeta();
		keepMeta.setDisplayName(ChatColor.GREEN + "Ok");
		keep.setItemMeta(keepMeta);
		inv.setItem(0, keep);
		
		ItemStack discard = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)14);
		ItemMeta discardMeta = discard.getItemMeta();
		discardMeta.setDisplayName(ChatColor.RED + "Discard");
		discard.setItemMeta(discardMeta);
		inv.setItem(8, discard);
		
		p.openInventory(inv);
	}
	
	public static void openBossLocationGUI(List<LivingBoss> bosses, Player p)
	{
		final int ROWS = (int)Math.ceil(bosses.size() / 9.0);
		Inventory inv = Bukkit.createInventory(null, 9 * ROWS, Reference.BOSS_LOCATIONS_GUI);
		
		for(int i = 0; i < bosses.size(); i++)
		{
			LivingBoss b = bosses.get(i);
			ItemStack skull = Mob.getFromType(b.getEntity().getType()).getHead();
			ItemMeta skullMeta = skull.getItemMeta();
			skullMeta.setDisplayName(b.getBoss().getDisplayName());
			List<String> lore = new ArrayList<>();
			Location bLoc = b.getEntity().getLocation();
			
			lore.add(ChatColor.GOLD + "" + (int)Math.round(bLoc.getX()) + ", " + (int)Math.round(bLoc.getY()) + ", " + (int)Math.round(bLoc.getZ()) + ", " + bLoc.getWorld().getName());
			skullMeta.setLore(lore);
			skull.setItemMeta(skullMeta);
			
			inv.setItem(i, skull);
		}
		
		p.openInventory(inv);
	}
	
	public static void genBorders(int rows, Inventory inv)
	{
		genBorders(rows, inv, "");
	}

	public static void genBorders(int rows, Inventory inv, String display) 
	{
		if(display.isEmpty())
			display = " ";
		ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)13);
		ItemMeta greenGlassMeta = greenGlass.getItemMeta();
		greenGlassMeta.setDisplayName(display);
		greenGlass.setItemMeta(greenGlassMeta);
		
		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta closeMeta = close.getItemMeta();
		closeMeta.setDisplayName(ChatColor.RED + "Close");
		close.setItemMeta(closeMeta);
		
		for(int i = 0; i < 9; i++)
		{
			if(i == 8)
				inv.setItem(i, close);
			else
				inv.setItem(i, greenGlass);
			inv.setItem(i + ((rows - 1) * 9), greenGlass);
		}
	}
}
