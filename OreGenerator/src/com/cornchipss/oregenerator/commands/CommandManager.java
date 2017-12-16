package com.cornchipss.oregenerator.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.oregenerator.OreGeneratorPlugin;
import com.cornchipss.oregenerator.generators.GeneratorItemForge;

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
			
			args[0] = args[0].toLowerCase();
			switch(args[0])
			{
			case "coal":
				is = GeneratorItemForge.createGenerator(GeneratorItemForge.GENERATOR_COAL_ID, plugin.getGeneratorMaterial(GeneratorItemForge.GENERATOR_COAL_ID));
				break;
			case "iron":
				is = GeneratorItemForge.createGenerator(GeneratorItemForge.GENERATOR_IRON_ID, plugin.getGeneratorMaterial(GeneratorItemForge.GENERATOR_IRON_ID));
				break;
			case "redstone":
				is = GeneratorItemForge.createGenerator(GeneratorItemForge.GENERATOR_REDSTONE_ID, plugin.getGeneratorMaterial(GeneratorItemForge.GENERATOR_REDSTONE_ID));
				break;
			case "lapis":
				is = GeneratorItemForge.createGenerator(GeneratorItemForge.GENERATOR_LAPIS_ID, plugin.getGeneratorMaterial(GeneratorItemForge.GENERATOR_LAPIS_ID));
				break;
			case "gold":
				is = GeneratorItemForge.createGenerator(GeneratorItemForge.GENERATOR_GOLD_ID, plugin.getGeneratorMaterial(GeneratorItemForge.GENERATOR_GOLD_ID));
				break;
			case "diamond":
				is = GeneratorItemForge.createGenerator(GeneratorItemForge.GENERATOR_DIAMOND_ID, plugin.getGeneratorMaterial(GeneratorItemForge.GENERATOR_DIAMOND_ID));
				break;
			case "emerald":
				is = GeneratorItemForge.createGenerator(GeneratorItemForge.GENERATOR_EMERALD_ID, plugin.getGeneratorMaterial(GeneratorItemForge.GENERATOR_EMERALD_ID));
			default:
				p.sendMessage(ChatColor.RED + "Invalid generator: " + args[0]);
			}
			p.getInventory().addItem(is);
			return true;
		}
		
		return false;
	}
}
