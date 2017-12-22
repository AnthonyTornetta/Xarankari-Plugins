package com.cornchipss.oregenerator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.oregenerator.OreGeneratorPlugin;
import com.cornchipss.oregenerator.generators.GeneratorUtils;
import com.cornchipss.oregenerator.upgrades.UpgradeUtils;

import net.md_5.bungee.api.ChatColor;

public class CommandManager 
{
	public static boolean runThroughCommands(Command command, CommandSender sender, String[] args, OreGeneratorPlugin plugin) 
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("You must be a player!");
			return true;
		}
		
		Player p = (Player)sender;
		String cmd = command.getName().toLowerCase();
		
		if(cmd.equalsIgnoreCase("oregenerator") || cmd.equalsIgnoreCase("oregen") || cmd.equals("gen"))
		{
			if(args.length == 0)
			{
				CommandInventories.openGetGeneratorGUI(p);
				return true;
			}
			
			int genId = 0;
			
			switch(args[0].toLowerCase())
			{
			case "coal":
				genId = GeneratorUtils.GENERATOR_COAL_ID;
				break;
			case "iron":
				genId = GeneratorUtils.GENERATOR_IRON_ID;
				break;
			case "redstone":
				genId = GeneratorUtils.GENERATOR_REDSTONE_ID;
				break;
			case "lapis":
				genId = GeneratorUtils.GENERATOR_LAPIS_ID;
				break;
			case "gold":
				genId = GeneratorUtils.GENERATOR_GOLD_ID;
				break;
			case "diamond":
				genId = GeneratorUtils.GENERATOR_DIAMOND_ID;
				break;
			case "emerald":
				genId = GeneratorUtils.GENERATOR_EMERALD_ID;
			default:
				p.sendMessage(ChatColor.RED + "Invalid generator: " + args[0]);
				return true;
			}
			
			ItemStack is = GeneratorUtils.createGeneratorItemStack(GeneratorUtils.GENERATOR_COAL_ID, plugin.getGeneratorMaterial(genId));
			p.getInventory().addItem(is);
			return true;
		}
		
		if(cmd.equalsIgnoreCase("upgrades") || cmd.equalsIgnoreCase("upgrade"))
		{
			int upId = 0;
			
			switch(args[0].toLowerCase())
			{
			case "speed":
				upId = UpgradeUtils.UPGRADE_SPEED_ID;
				break;
			default:
				p.sendMessage(ChatColor.RED + "Invalid generator: " + args[0]);
				return true;
			}
			
			ItemStack is = UpgradeUtils.createUpgradeItemStack(upId, plugin.getUpgradeMaterial(upId));
			p.getInventory().addItem(is);
		}
		
		return false;
	}
}
