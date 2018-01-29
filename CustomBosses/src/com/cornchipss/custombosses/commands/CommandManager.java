package com.cornchipss.custombosses.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cornchipss.custombosses.util.Vector2;
import com.cornchipss.custombosses.util.mobheads.Head.Mob;

public class CommandManager extends Debug
{
	private static Map<Player, Vector2<Location, Location>> playersSettingLocations = new HashMap<>();
	
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
				if(args.length != 2)
				{
					sender.sendMessage("You must specify the boss id and player");
					return true;
				}
				
				Player p = Bukkit.getPlayer(args[1]);
				
				if(p == null)
				{
					sender.sendMessage("The player '" + args[1] + "' could not be found");
					return true;
				}
				
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
				else
				{
					p.sendMessage(ChatColor.RED + "Invalid id: " + args[0] + " (must be an integer)");
				}
			}
			
			Player p = (Player)sender;
			
			if(args.length == 0)
			{
				openBossEggGui(customBosses.getBossHandler().getLoadedBosses(), p);
			}
			else
			{
				Player giveTo = p;
				if(args.length > 1)
				{
					if(sender.hasPermission("custombosses.bossegg.giveto"))
					{
						giveTo = Bukkit.getPlayer(args[1]);
						if(giveTo == null)
						{
							sender.sendMessage(ChatColor.RED + "Player '" + args[1] + "' could not be found");
							return true;
						}
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
						return true;
					}
				}
				
				if(Helper.isInt(args[0]))
				{
					int id = Integer.parseInt(args[0]);
					for(Boss b : customBosses.getBossHandler().getLoadedBosses())
					{
						if(b.getId() == id)
						{
							giveTo.getInventory().addItem(b.getSpawnItem());
							giveTo.sendMessage(b.getDisplayName() + ChatColor.GOLD + " spawn item given.");
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
				sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
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
				if(customBosses.getBossHandler().getLivingBosses().size() == 0)
				{
					sender.sendMessage(ChatColor.GOLD + "There are no alive bosses");
					return true;
				}
				openBossLocationGui(customBosses.getBossHandler().getLivingBosses(), (Player)sender);
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
			}
		}
		else if(cmd.equals("bosspos1") || cmd.equals("bosspos2"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage("You must be a player to use this command");
				return true;
			}
			if(sender.hasPermission("custombosses.setspawnlocation"))
			{
				Player p = (Player)sender;
				int posNum = Integer.parseInt(cmd.charAt(cmd.length() - 1) + "");
				Location locOfPlayer = p.getLocation().clone();
				locOfPlayer.setX(Helper.roundToNthDecimal(locOfPlayer.getX(), 1));
				locOfPlayer.setY(Helper.roundToNthDecimal(locOfPlayer.getY(), 1));
				locOfPlayer.setZ(Helper.roundToNthDecimal(locOfPlayer.getZ(), 1));
				
				Vector2<Location, Location> locs = new Vector2<>();
				for(Player alreadySetPlayer : playersSettingLocations.keySet())
				{
					if(alreadySetPlayer.equals(p))
					{
						locs = playersSettingLocations.get(p);
						break;
					}
				}
				
				if(posNum == 1)
				{
					locs.setX(locOfPlayer);
				}
				else
				{
					locs.setY(locOfPlayer);
				}
				
				playersSettingLocations.put(p, locs);
				p.sendMessage(ChatColor.RED + "Spawn corner " + posNum + " set to " + locOfPlayer.getX() + ", " + locOfPlayer.getY() + ", " + locOfPlayer.getZ());
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
			}
		}
		else if(cmd.equals("savebosspos"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage("You must be a player to use this command");
				return true;
			}
			if(sender.hasPermission("custombosses.savespawnlocation"))
			{
				Player p = (Player)sender;
				Location locOfPlayer = p.getLocation().clone();
				
				Vector2<Location, Location> locs = null;
				for(Player alreadySetPlayer : playersSettingLocations.keySet())
				{
					if(alreadySetPlayer.equals(p))
					{
						locs = playersSettingLocations.get(p);
						break;
					}
				}
				
				if(args.length == 0)
				{
					p.sendMessage("You must specify which bosses will spawn (by their id) like so: /savespawnlocation 0 1 2 3 etc...");
				}
				
				List<Integer> bossIds = new ArrayList<>();
				for(String s : args)
				{
					if(Helper.isInt(s))
					{
						bossIds.add(Integer.);
					}
					else
					{
						
					}
				}
				
				if(locs == null)
				{
					p.sendMessage(ChatColor.RED + "No spawn corners were set! Use /bosspos1 or /bosspos2 to set them");
					return true;
				}
				
				if(locs.getX() == null)
				{
					p.sendMessage(ChatColor.RED + "You haven't set the first spawn corner yet");
					return true;
				}
				if(locs.getY() == null)
				{
					p.sendMessage(ChatColor.RED + "You haven't set the second spawn corner yet");
					return true;
				}
				
				List<Boss> bossesThatWillSpawn = new ArrayList<>();
				
				customBosses.getBossHandler().addSpawnArea(locs, bossesThatWillSpawn);
				
				playersSettingLocations.remove(p);
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
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
