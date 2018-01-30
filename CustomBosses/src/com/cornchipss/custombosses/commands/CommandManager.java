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

import com.cornchipss.custombosses.CustomBosses;
import com.cornchipss.custombosses.Debug;
import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.boss.spawner.BossSpawnArea;
import com.cornchipss.custombosses.util.Helper;
import com.cornchipss.custombosses.util.Reference;
import com.cornchipss.custombosses.util.Vector2;

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
				InventoryHelper.openBossEggGUI(customBosses.getBossHandler().getLoadedBosses(), p);
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
				InventoryHelper.openBossLocationGUI(customBosses.getBossHandler().getLivingBosses(), (Player)sender);
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
			if(sender.hasPermission("custombosses.setspawnarea"))
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
				p.sendMessage(ChatColor.GOLD + "Spawn corner " + posNum + " set to " + locOfPlayer.getX() + ", " + locOfPlayer.getY() + ", " + locOfPlayer.getZ());
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
			}
		}
		else if(cmd.equals("savespawnarea"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage("You must be a player to use this command");
				return true;
			}
			if(sender.hasPermission("custombosses.save.spawnarea"))
			{
				Player p = (Player)sender;
				
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
					p.sendMessage("You must specify which bosses will spawn (by their id) like so: /savespawnarea 0 1 2 3 etc...");
					return true;
				}
				
				List<Integer> bossIds = new ArrayList<>();
				for(String s : args)
				{
					if(Helper.isInt(s))
					{
						int id = Integer.parseInt(s);
						if(!Reference.getBossIds(customBosses.getBossHandler().getLoadedBosses()).contains(id))
						{
							sender.sendMessage(ChatColor.RED + "Invalid boss id! Do /bossegg and look at the number in the brackets to see all the ids");
							return true;
						}
						bossIds.add(id);
					}
					else
					{
						sender.sendMessage("The id of " + s + " is not a valid integer.");
						return true;
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
				if(!locs.getX().getWorld().equals(locs.getY().getWorld()))
				{
					p.sendMessage(ChatColor.RED + "The corners must be in the same world!");
					return true;
				}
				
				List<Boss> bossesThatWillSpawn = Reference.getBossesFromIds(customBosses.getBossHandler().getLoadedBosses(), bossIds);
				
				customBosses.getBossHandler().addSpawnArea(locs, bossesThatWillSpawn);
				
				playersSettingLocations.remove(p);
				
				p.sendMessage(ChatColor.GOLD + "Boss Spawn Location set!");
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You do not have the proper permissions.");
			}
		}
		else if(cmd.equals("listspawnareas"))
		{
			if(sender.hasPermission("custombosses.list.spawnareas"))
			{
				if(sender instanceof Player)
				{
					InventoryHelper.openBossSpawnAreasGUI(customBosses.getBossHandler().getSpawnAreas(), (Player)sender);
				}
				else
				{
					for(BossSpawnArea a : customBosses.getBossHandler().getSpawnAreas())
					{
						sender.sendMessage(a.getLocationX().getX() + ", " + a.getLocationX().getY() + ", " + a.getLocationX().getZ() + "; " + a.getLocationX().getWorld());
						sender.sendMessage(a.getLocationY().getX() + ", " + a.getLocationY().getY() + ", " + a.getLocationY().getZ() + "; " + a.getLocationY().getWorld());
						sender.sendMessage("---");
					}
				}
			}
		}
		
		return true;
	}
}
