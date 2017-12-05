package com.cornchipss.guilds.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cornchipss.guilds.Guilds;
import com.cornchipss.guilds.ref.Reference;

public class CommandMgr 
{
	/**
	 * Runs through every build battle command
	 * @param command The command passed in by the sender
	 * @param sender The command sender
	 * @param args The arguments to the command
	 * @param bb The java plugin class
	 * @return True if it was successful
	 */
	public static boolean runThruCommands(Command command, CommandSender sender, String[] args, Guilds guildsPl)
	{
		String gCmd = command.getName().toLowerCase();
		
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(gCmd.equals("guilds") && perm(p, "guilds.help"))
			{
				p.sendMessage(ChatColor.AQUA + "You are now in the Armadale guild");
				guildsPl.getConfig().set(p.getUniqueId().toString(), 1);
				guildsPl.getConfig().set(Reference.CFG_GUILD_PREFIX + "1", "Armadale");
				guildsPl.saveConfig();
				
				guildsPl.updateTabList();
			}
			if(gCmd.equals("guildchat") || gCmd.equals("gc") && perm(p, "guilds.guildchat"))
			{
				if(!guildsPl.getGuildManager().playerHasGuild(p))
				{
					p.sendMessage(ChatColor.RED + "You are not in a guild!");
					return true;
				}
				
				if(guildsPl.getGuildChatters().contains(p))
				{
					p.sendMessage(ChatColor.AQUA + "Guild Chat: Off");
					guildsPl.getGuildChatters().remove(p);
				}
				else
				{
					p.sendMessage(ChatColor.AQUA + "Guild Chat: On");
					guildsPl.getGuildChatters().add(p);
				}
			}
		}
		else
		{
			// Almost all build battle commands are player-only so I just made sure only players can use them
			sender.sendMessage("You must be a player to use build battle commands");
		}
		return true;
	}
	
	private static boolean perm(Player p, String perm)
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
