package com.cornchipss.guilds.cmds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
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

import mkremins.fanciful.FancyMessage;

public class CommandMgr implements Listener
{
	private List<Player> possibleGuildDeletions = new ArrayList<>();
	private Map<Player, Guild> guildJoinProposals = new HashMap<>();
	
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
	 * @return true if it was successful
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
			else if(cmd.equals("accept"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					
					if(plugin.getGuildManager().playerHasGuild(p))
					{
						p.sendMessage(ChatColor.RED + "You are already in a guild!");
						return true;
					}
					
					if(guildJoinProposals.containsKey(p))
					{					
						Guild g = guildJoinProposals.get(p);
						try 
						{
							plugin.getGuildManager().addPlayerToGuild(p, g);
							p.sendMessage(ChatColor.GREEN + "Guild " + g.getName() + " successfully joined!");
						}
						catch (IOException e) 
						{
							e.printStackTrace();
							p.sendMessage(ChatColor.RED + "A server error occurred when trying to join the guild.");
						}
						
						guildJoinProposals.remove(p);
					}
					else
					{
						p.sendMessage(ChatColor.RED + "You have no pending invites.");
						return true;
					}
				}
			}
			else if(cmd.equals("deny"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					
					if(guildJoinProposals.containsKey(p))
					{					
						p.sendMessage(ChatColor.GREEN + "Guild request rejected.");
						guildJoinProposals.remove(p);
					}
					else
					{
						p.sendMessage(ChatColor.RED + "You have no pending invites.");
						return true;
					}
				}
			}
			else if(cmd.equals("invite"))
			{
				if(iop(sender))
				{
					Player p = (Player)sender;
					
					Guild g = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					
					if(g == null)
					{
						p.sendMessage(ChatColor.RED + "You aren't in a guild.");
						return true;
					}
					
					if(args.length < 2)
					{
						p.sendMessage(ChatColor.RED + "You must specify the player to invite.");
						return true;
					}
					
					Player toInvite = Bukkit.getPlayer(args[1]);
					if(toInvite == null)
					{
						p.sendMessage(ChatColor.RED + "Unable to find player " + args[1] + ".");
						return true;
					}
					
					Guild potentialGuild = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
					if(potentialGuild != null)
					{
						p.sendMessage(ChatColor.RED + p.getDisplayName() + ChatColor.RED + " is already in the guild \"" + potentialGuild.getName() + "\".");
						return true;
					}
					
					if(guildJoinProposals.containsKey(p))
					{
						p.sendMessage(ChatColor.RED + "That player, " + toInvite.getDisplayName() + ChatColor.RED + ", already has a pending invite.");
						return true;
					}
					
					p.sendMessage(ChatColor.GREEN + "Guild invite sent to " + toInvite.getDisplayName() + ChatColor.GREEN + ".");
					guildJoinProposals.put(toInvite, g);
					
					List<String> acceptText = new ArrayList<>();
					acceptText.add(ChatColor.GREEN + "Accepts the guild join request.");
					acceptText.add(ChatColor.GREEN + "/guilds accept");
					
					List<String> denyText = new ArrayList<>();
					denyText.add(ChatColor.RED + "Rejects the guild join request.");
					denyText.add(ChatColor.RED + "/guilds deny");
					
					FancyMessage inviteSpeil = new FancyMessage("You have been invited to the guild \"" + g.getName() + "\".\n").color(ChatColor.GREEN);
					inviteSpeil.then(ChatColor.BOLD + "Accept").color(ChatColor.GREEN).tooltip(acceptText).suggest("/guilds accept");
					inviteSpeil.then(ChatColor.BOLD + " - ");
					inviteSpeil.then(ChatColor.BOLD + "Deny").color(ChatColor.RED).tooltip(denyText).suggest("/guilds deny");
					
					inviteSpeil.send(toInvite);
				}
			}
			else
			{
				displayHelp(sender);
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
