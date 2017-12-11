package com.cornchipss.oregenerator.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.oregenerator.OreGeneratorPlugin;
import com.cornchipss.oregenerator.generators.Generator;

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
		
		if(cmd.equalsIgnoreCase("oregenerator"))
		{
			if(args.length == 0)
			{
				CommandInventories.openGetGeneratorGUI(p);
				return true;
			}
			
			ItemStack is = new ItemStack(Material.AIR);
			Material genMat = plugin.getGeneratorMaterial();
			
			args[0] = args[0].toLowerCase();
			switch(args[0])
			{
			case "coal":
				is = Generator.createGenerator(Generator.GENERATOR_COAL_ID, genMat);
				break;
			case "iron":
				is = Generator.createGenerator(Generator.GENERATOR_IRON_ID, genMat);
				break;
			case "redstone":
				is = Generator.createGenerator(Generator.GENERATOR_REDSTONE_ID, genMat);
				break;
			case "lapis":
				is = Generator.createGenerator(Generator.GENERATOR_LAPIS_ID, genMat);
				break;
			case "gold":
				is = Generator.createGenerator(Generator.GENERATOR_GOLD_ID, genMat);
				break;
			case "diamond":
				is = Generator.createGenerator(Generator.GENERATOR_DIAMOND_ID, genMat);
				break;
			case "emerald":
				is = Generator.createGenerator(Generator.GENERATOR_EMERALD_ID, genMat);
			default:
				p.sendMessage(ChatColor.RED + "Invalid generator: " + args[0]);
			}
			p.getInventory().addItem(is);
		}
		
		return false;
	}
}
