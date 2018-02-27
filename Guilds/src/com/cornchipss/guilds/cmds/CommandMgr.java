package com.cornchipss.guilds.cmds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.cornchipss.guilds.GuildsPlugin;
import com.cornchipss.guilds.guilds.Guild;

public class CommandMgr implements Listener
{
	private List<Player> possibleGuildDeletions = new ArrayList<>();
	private GuildsPlugin plugin;
	
	public CommandMgr(GuildsPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Runs through every build battle command
	 * @param command The command passed in by the sender
	 * @param sender The command sender
	 * @param args The arguments to the command
	 * @param bb The java plugin class
	 * @return True if it was successful
	 */
	public boolean runThruCommands(Command command, CommandSender sender, String[] args)
	{
		String cmd = command.getName().toLowerCase();
		
		if(cmd.equals("guild") || cmd.equals("guilds"))
		{
			if(args.length < 1)
			{
				displayHelp(sender);
				return true;
			}
			
			cmd = args[0].toLowerCase();
			
			if(cmd.equals("new") || cmd.equals("create"))
			{				
				if(!iop(sender) || !perm(sender, "guilds.create"))
					return true;
				
				Player p = (Player)sender;
				
				if(plugin.getGuildManager().playerHasGuild(p))
				{
					p.sendMessage(ChatColor.RED + "You are already in a guild! do /guild leave to leave your guild.");
					return true;
				}
				
				if(args.length < 2)
				{
					sender.sendMessage(ChatColor.RED + "You must specify the guild name.");
					return true;
				}
				else
				{
					try
					{
						plugin.getGuildManager().createGuild(args[1], p);
						p.sendMessage(ChatColor.GREEN + "Your guild \"" + args[1] + "\" has been created!");
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
						p.sendMessage(ChatColor.RED + "There was a server error when creating your guild!");
					}
					
					plugin.updateTabList();
				}
			}
			else if(cmd.equals("delete"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					if(!possibleGuildDeletions.contains(p))
						possibleGuildDeletions.add(p);
					
					if(!plugin.getGuildManager().playerHasGuild(p))
					{
						p.sendMessage(ChatColor.RED + "You must be in a guild to use this.");
						return true;
					}
					
					p.sendMessage(ChatColor.RED + "Are you sure you want to delete your guild? Type your guild's name to confirm. " + ChatColor.DARK_RED + "WARNING:" + ChatColor.RED + " THIS CANNOT BE UNDONE!!!");
				}
			}
		}
		
		// Guild Chat Stuff
		else if(cmd.equals("guildchat") || cmd.equals("gc") && perm(sender, "guilds.guildchat"))
		{
			if(iop(sender))
			{
				Player p = (Player)sender;
				
				if(!plugin.getGuildManager().playerHasGuild(p))
				{
					p.sendMessage(ChatColor.RED + "You are not in a guild!");
					return true;
				}
				
				if(plugin.getGuildManager().getGuildChatters().contains(p))
				{
					p.sendMessage(ChatColor.AQUA + "Guild Chat: Off");
					plugin.getGuildManager().getGuildChatters().remove(p);
				}
				else
				{
					p.sendMessage(ChatColor.AQUA + "Guild Chat: On");
					plugin.getGuildManager().getGuildChatters().add(p);
				}
			}
		}
		
		return true;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		if(possibleGuildDeletions.contains(p) && plugin.getGuildManager().playerHasGuild(p))
		{
			String msg = e.getMessage();
			Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
			if(msg.equalsIgnoreCase(g.getName()))
			{
				if(plugin.getGuildManager().playerHasGuild(p))
				{
					try
					{
						plugin.getGuildManager().deleteGuild(g);
						p.sendMessage(ChatColor.GREEN + "Guild successfully deleted.");
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
						p.sendMessage(ChatColor.RED + "A server error ocurred whilst trying to delete your guild");
					}
					e.setCancelled(true);
					
					plugin.updateTabList();
				}
			}
			else
			{
				p.sendMessage(ChatColor.RED + "Guild deletion cancelled");
				e.setCancelled(true);
			}
		}
		
		possibleGuildDeletions.remove(p);
	}
	
	private static void displayHelp(CommandSender sender) 
	{
		sender.sendMessage(ChatColor.GREEN + "=== Guilds Help ===");
		sender.sendMessage(ChatColor.GREEN + "- /gc - Enter Guild Chat (used without /guilds at the start)");
		
		sender.sendMessage(ChatColor.GREEN + "- new/create [name] - creates a new guild");
		sender.sendMessage(ChatColor.GREEN + "- delete - deletes your guild");
		sender.sendMessage(ChatColor.GREEN + "- join [name] - asks the officials of a guild if you can join");
	}

	private static boolean iop(CommandSender sender)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Sorry, but you must be a player to use this command");
			return false;
		}
		return true;
	}
	
	private static boolean perm(CommandSender p, String perm)
	{
		if(p.hasPermission(perm))
			return true;
		else
		{
			p.sendMessage(ChatColor.RED + "Insufficient permissions!");
			return false;
		}
	}
}
