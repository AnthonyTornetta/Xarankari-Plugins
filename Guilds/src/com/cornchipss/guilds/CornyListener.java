package com.cornchipss.guilds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.cornchipss.guilds.guilds.Guild;

public class CornyListener implements Listener
{
	private GuildsPlugin guildsPlugin;
	
	public CornyListener(GuildsPlugin guildsPlugin) 
	{
		this.guildsPlugin = guildsPlugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		guildsPlugin.updateTabList();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		guildsPlugin.updateTabList();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		
		if(guildsPlugin.getGuildChatters().contains(p))
		{
			if(!guildsPlugin.getGuildManager().playerHasGuild(p))
			{
				guildsPlugin.getGuildChatters().remove(p);
				return;
			}
			
			Guild guild = guildsPlugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
			
			List<Player> playersInGuild = guildsPlugin.getGuildManager().getOnlinePlayersInGuild(guild);
			
			e.setFormat(ChatColor.AQUA + "[GC] " + ChatColor.RESET + e.getFormat());
			
			for(Player onlinePlayer : Bukkit.getOnlinePlayers())
			{
				if(guildsPlugin.getSocialSpies().contains(onlinePlayer))
					continue;
				
				if(!playersInGuild.contains(onlinePlayer))
				{
					e.getRecipients().remove(onlinePlayer);
				}
			}
		}
	}
}
