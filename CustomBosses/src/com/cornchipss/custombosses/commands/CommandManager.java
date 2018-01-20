package com.cornchipss.custombosses.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.cornchipss.custombosses.CustomBosses;
import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.util.Helper;
import com.cornchipss.custombosses.util.Reference;

public class CommandManager 
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
			int bawesesKilled = 0;
			for(LivingBoss boss : customBosses.getBossHandler().getLivingBosses())
			{
				boss.kill();
				bawesesKilled++;
			}
			
			sender.sendMessage(ChatColor.GOLD + "You brutally murdered " + bawesesKilled + " bosses.");
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

}
