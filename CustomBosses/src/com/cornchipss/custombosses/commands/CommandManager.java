package com.cornchipss.custombosses.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.custombosses.CustomBosses;
import com.cornchipss.custombosses.Debug;
import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.util.Helper;
import com.cornchipss.custombosses.util.Reference;
import com.cornchipss.custombosses.util.mobheads.Head.Mob;

public class CommandManager extends Debug
{
	public static boolean runThroughCommands(Command command, CommandSender sender, String[] args, CustomBosses customBosses) 
	{
		String cmd = command.getLabel().toLowerCase();
		
		if(cmd.equals("bossegg"))
		{
			if(!sender.hasPermission("custombosses.bossegg"))
			{
				sender.sendMessage(ChatColor.RED + "Invalid permissions!");
				return true;
			}
			
			if(!(sender instanceof Player))
			{
				sender.sendMessage(ChatColor.RED + "Only a player can use this command :(");
				return true;
			}
			
			Player p = (Player)sender;
			
			if(args.length == 0)
			{
				openBossEggGui(customBosses.getBossHandler().getLoadedBosses(), p);
			}
			else
			{
				if(Helper.isInt(args[0]))
				{
					int id = Integer.parseInt(args[0]);
					for(Boss b : customBosses.getBossHandler().getLoadedBosses())
					{
						if(b.getId() == id)
						{
							p.getInventory().addItem(b.getSpawnItem());
							p.sendMessage(b.getDisplayName() + ChatColor.GOLD + " spawn item given.");
							return true;
						}
					}
				}
				
				p.sendMessage(ChatColor.RED + "Invalid id: " + args[0] + "; must be an integer");
			}
		}
		else if(cmd.equals("killallbosses"))
		{
			if(sender.hasPermission("custombosses.killallbosses"))
			{
				int bawesesKilled = 0;
				for(LivingBoss boss : customBosses.getBossHandler().getLivingBosses())
				{
					boss.remove();
					bawesesKilled++;
				}
				
				sender.sendMessage(ChatColor.GOLD + "You brutally murdered " + bawesesKilled + " bosses.");
			}
			else
			{
				sender.sendMessage("You don't have proper permissions");
			}
		}
		else if(cmd.equals("bosses"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage("You must be a player to use this command");
				return true;
			}
			if(sender.hasPermission("custombosses.bosses"))
			{
				openBossLocationGui(customBosses.getBossHandler().getLivingBosses(), (Player)sender);
			}
		}
		
		return true;
	}

	private static void openBossEggGui(List<Boss> bosses, Player p) 
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
	
	private static void openBossLocationGui(List<LivingBoss> bosses, Player p)
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

}
