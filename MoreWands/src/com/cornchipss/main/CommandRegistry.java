package com.cornchipss.main;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cornchipss.main.items.Items;

/**
 * A class I made to make the command passed funtion in the main not huge.
 * Also, for organization.
 * To check a command just do CommandRegistry.check(CommandSender sender, Command command, String label, String[] args, Main main);
 */
public class CommandRegistry 
{
	/**
	 * Checks a command passed into it and runs it if it is recognized
	 * @param sender The command sender
	 * @param command The command
	 * @param label The label passed in
	 * @param args The command arguments
	 * @param main The main class that extends JavaPlugin
	 * @return True if it was successful, false if it wasn't
	 */
	public static boolean check(CommandSender sender, Command command, String label, String[] args, Main main)
	{
		String cmd = command.getName().toLowerCase();
		
		// Things that wont work in a big ol' switch statement!
		
		// Gamemode changing
		if(cmd.substring(0, 2).equals("gm"))
		{
			if(!sender.hasPermission("wand.gm"))
			{
				sender.sendMessage(ChatColor.RED + "Invalid permissions to perform the command!");
				return true;
			}
			Player target;
						
			if(args.length != 0)
			{
				// Player given
				target = main.getServer().getPlayer(args[0]);
				if(target == null)
				{
					// He's not online
					sender.sendMessage(ChatColor.RED + "Erorr: The player specified is not online!");
					return true;
				}
				else
				{
					sender.sendMessage(ChatColor.GOLD + "Player " + target.getDisplayName() + "'s gamemode has been set.");
					// Online
					return gmSomeone(target, cmd);
				}
			}
			
			if(iop(sender))
			{
				Player p = (Player)sender;
				// Player
				return gmSomeone(p, cmd);
			}
			else
			{
				// Console
				sender.sendMessage(ChatColor.RED + "Error: You must specify a player to heal!");
				return true;
			}
		}
		
		/* Things that will work in a big ol' switch statement */
		
		switch(cmd)
		{
			// Heal command
			case "heal":
			{
				if(args.length != 0)
				{ // There are arguments passed in
					Player target = main.getServer().getPlayer(args[0]);
					if(target != null)
					{
						heal(target, true);
						sender.sendMessage(ChatColor.GOLD + "The player " + target.getDisplayName() + " has been healed!");
						return true;
					}
					else
					{
						// The player isnt online or doesnt exist
						sender.sendMessage(ChatColor.RED + "Error: The player must be online!");
						return true;
					}
				}
				else
				{
					if(iop(sender))
					{
						// Its a player with no args
						heal((Player) sender, true);
					}
					else
					{
						// Its the console with no args
						sender.sendMessage(ChatColor.RED + "Error: The target must be specified!");
					}
				}
				return true;
			}
			case "wand":
			{
				if(args.length == 0)
				{
					sender.sendMessage(ChatColor.RED + "Usage: /wand [type] <receiver>");
					sender.sendMessage(ChatColor.RED + "Use /wand listings to see the types");
					return true;
				}
				else if(args.length == 1)
				{
					if(iop(sender))
					{
						Player p = (Player) sender;
						return giveWand(main, sender, args[0], p, false);
					}
					else
					{
						sender.sendMessage("Error: Please specify the player to give the wand to!");
						return true;
					}
				}
				else
				{
					Player target = main.getServer().getPlayer(args[1]);
					
					if(target == null)
					{
						sender.sendMessage(ChatColor.RED + "Error: The player must be online!");
						return true;
					}
					return giveWand(main, sender, args[0], target, true);
				}
			}
		}
		return false;
	}
	
	
	
	/**
	 * A helper function I made to make the wand and item stuff easier
	 * @param main The main class that extends JavaPlugin
	 * @param sender The command sender
	 * @param arg The argument with the wand name in it
	 * @param target The target player to give the wand to
	 * @return True if successful
	 */
	private static boolean giveWand(Main main, CommandSender sender, String arg, Player target, boolean displayMsg)
	{
		switch(arg.toLowerCase())
		{
		case "test":
			if(hasPerm(target, "wand.give.test"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.MAGIC_HOLDER_ID));
				target.getInventory().addItem(main.getItems().getItem(Items.LIGHTNING_ID));
				target.getInventory().addItem(main.getItems().getItem(Items.EXPLOSION_ID));
				target.getInventory().addItem(main.getItems().getItem(Items.REGEN_ID));
				target.getInventory().addItem(main.getItems().getItem(Items.WATER_BREATH_ID));
				target.getInventory().addItem(main.getItems().getItem(Items.FLY_ID));
				target.getInventory().addItem(main.getItems().getItem(Items.LEVITATE_ID));
				target.getInventory().addItem(main.getItems().getItem(Items.NO_FALL_ID));
				target.getInventory().addItem(main.getItems().getItem(Items.FIRE_RESIST_ID));
				target.getInventory().addItem(main.getItems().getItem(Items.LIGHTNING_RESIST_ID));
				target.getInventory().addItem(main.getItems().getItem(Items.MINING_ID));
				target.sendMessage(ChatColor.GOLD + "Test equipment added");
			}
			break;
		case "lightning":
			if(hasPerm(target, "wand.give.lightning"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.LIGHTNING_ID));
				target.sendMessage(ChatColor.GOLD + "You got a lightning wand!");
			}
			break;
		case "explosion":
			if(hasPerm(target, "wand.give.explosion"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.EXPLOSION_ID));
				target.sendMessage(ChatColor.GOLD + "You got an explosion wand!");
			}
			break;
		case "sheep":
			if(hasPerm(target, "wand.give.sheep"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.SHEEP_ID));
				target.sendMessage(ChatColor.GOLD + "You got a sheep summoning wand!");
			}
			break;
		case "speed":
			if(hasPerm(target, "wand.give.speed"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.SPEED_ID));
				target.sendMessage(ChatColor.GOLD + "You got a sheep summoning wand!");
			}
			break;
		case "regeneration":
			if(hasPerm(target, "wand.give.regeneration"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.REGEN_ID));
				target.sendMessage(ChatColor.GOLD + "You got a regeneration wand!");
			}
			break;
		case "invisibility":
			if(hasPerm(target, "wand.give.invisibility"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.INVIS_ID));
				target.sendMessage(ChatColor.GOLD + "You got a invisibility wand!");
			}
			break;
		case "haste":
			if(hasPerm(target, "wand.give.haste"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.HASTE_ID));
				target.sendMessage(ChatColor.GOLD + "You got a haste wand!");
			}
			break;
		case "health":
			if(hasPerm(target, "wand.give.health"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.HEALTH_ID));
				target.sendMessage(ChatColor.GOLD + "You got a health boost wand!");
			}
			break;
		case "jump":
			if(hasPerm(target, "wand.give.jump"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.JUMP_ID));
				target.sendMessage(ChatColor.GOLD + "You got a jump boost wand!");
			}
			break;
		case "waterbreath":
			if(hasPerm(target, "wand.give.waterbreath"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.WATER_BREATH_ID));
				target.sendMessage(ChatColor.GOLD + "You got a water breathing wand!");
			}
			break;
		case "levitation":
			if(hasPerm(target, "wand.give.levitation"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.LEVITATE_ID));
				target.sendMessage(ChatColor.GOLD + "You got a levitation wand!");
			}
			break;
		case "flight":
			if(hasPerm(target, "wand.give.flight"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.FLY_ID));
				target.sendMessage(ChatColor.GOLD + "You got a flight wand!");
			}
			break;
		case "nofallboots":
			if(hasPerm(target, "wand.give.nofallboots"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.NO_FALL_ID));
				target.sendMessage(ChatColor.GOLD + "You got boots of no fall!");
			}
			break;
		case "firechest":
			if(hasPerm(target, "wand.give.firechest"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.FIRE_RESIST_ID));
				target.sendMessage(ChatColor.GOLD + "You got a fire resistance chestplate!");
			}
			break;
		case "lightninghelm":
			if(hasPerm(target, "wand.give.lightninghelm"))
			{
			target.getInventory().addItem(main.getItems().getItem(Items.LIGHTNING_RESIST_ID));
			target.sendMessage(ChatColor.GOLD + "You got a lightning resistance helmet!");
			}
			break;
		case "core":
			if(hasPerm(target, "wand.give.core"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.MAGIC_HOLDER_ID));
				target.sendMessage(ChatColor.GOLD + "You got a kinetic magic core!");
			}
			break;
		case "mining":
			if(hasPerm(target, "wand.give.mining"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.MINING_ID));
				target.sendMessage(ChatColor.GOLD + "You got a mining wand!");
			}
			break;
		case "gun":
			if(hasPerm(target, "wand.give.gun"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.GUN_ID));
				target.sendMessage(ChatColor.GOLD + "You got a test gun!");
			}
			break;
		case "growth":
			if(hasPerm(target, "wand.give.growth"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.GROWTH_ID));
				target.sendMessage(ChatColor.GOLD + "You got a growth wand!");
			}
			break;
		case "lessgrav":
			if(hasPerm(target, "wand.give.lessgrav"))
			{
				target.getInventory().addItem(main.getItems().getItem(Items.LESS_GRAV_ID));
				target.sendMessage("You have recieved boots of less gravity!");
			}
			break;
		default:
			sender.sendMessage("Wands Available: Lightning, Explosion, Sheep, Speed, Regeneration, Invisibilty, Haste, Health, Jump, WaterBreath, Levatation, and Flight");
			sender.sendMessage("Armor available: LightningHelm, FireChest, NoFallBoots");
			sender.sendMessage("To get the wand core (which is required to use all spells) do /wand core");
			sender.sendMessage("Do /wand test to get the basic things");
			displayMsg = false;
		}
		if(displayMsg)
			sender.sendMessage(ChatColor.GOLD + "The wand was added to " + target.getDisplayName() + "'s inventory.");
		return true;
	}
	
	/**
	 * Saves me some typing - checks if the sender is a player or not
	 * @param sender The command sender
	 * @return True if its a player - false if it isnt
	 */
	private static boolean iop(CommandSender sender)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Error: You must be a player to use that command!");
			return false;
		}
		return true;
	}
	
	/**
	 * Helper function that heals the player and reduces typing
	 * @param p The player
	 * @param showMessage Show a message to the player
	 */
	private static void heal(Player p, boolean showMessage)
	{
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setSaturation(20);
		
		// Make them free from all potion types!
		for(int i = 0; i < Reference.BAD_POTION_TYPES.length; i++)
		{
			if(p.hasPotionEffect(Reference.BAD_POTION_TYPES[i]))
			{
				p.removePotionEffect(Reference.BAD_POTION_TYPES[i]);
			}
			p.setFireTicks(0);
		}
		
		if(showMessage)
			p.sendMessage(ChatColor.GOLD + "Thou hath been healed!");
	}
	
	/**
	 * Helper function that puts someone in a specified game mode from the command given
	 * @param p The target player
	 * @param cmd The command passed
	 * @return True if gamemoded
	 */
	private static boolean gmSomeone(Player p, String cmd)
	{
		switch(cmd.charAt(2))
		{
			case 'c':
				p.setGameMode(GameMode.CREATIVE);
				p.sendMessage(ChatColor.GOLD + "Your gamemode has been set to creative.");
				return true;
			case '1':
				p.setGameMode(GameMode.CREATIVE);
				p.sendMessage(ChatColor.GOLD + "Your gamemode has been set to creative.");
				return true;
			case '0':
				p.setGameMode(GameMode.SURVIVAL);
				p.sendMessage(ChatColor.GOLD + "Your gamemode has been set to survival.");
				return true;
			case 'a':
				p.setGameMode(GameMode.ADVENTURE);
				p.sendMessage(ChatColor.GOLD + "Your gamemode has been set to adventure.");
				return true;
			case '2':
				p.setGameMode(GameMode.ADVENTURE);
				p.sendMessage(ChatColor.GOLD + "Your gamemode has been set to adventure.");
				return true;
			case 's':
				if(cmd.length() == 3)
				{
					p.setGameMode(GameMode.SURVIVAL);
					p.sendMessage(ChatColor.GOLD + "Your gamemode has been set to survival.");
				}
				else if(cmd.charAt(3) == 'p')
				{
					p.setGameMode(GameMode.SPECTATOR);
					p.sendMessage(ChatColor.GOLD + "Your gamemode has been set to spectator.");
				}
				else
					return true;
				return true;
			case '3':
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage(ChatColor.GOLD + "Your gamemode has been set to spectator.");
				return true;
		}
		return true;
	}
	
	/**
	 * Checks if a player has permissions to execute a specified command
	 * @param p The player
	 * @param perm The permission
	 * @return True if they have perms, false if they don't and it auto prints an error
	 */
	private static boolean hasPerm(Player p, String perm)
	{
		if(p.hasPermission(perm))
			return true;
		else
		{
			p.sendMessage(ChatColor.RED + "Invalid permissions to perform the command!");
			return false;
		}
	}
}
