package com.cornchipss.guilds;

import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.cornchipss.guilds.guilds.Guild;
import com.cornchipss.guilds.ref.Reference;

public class CornyListener implements Listener
{
	private GuildsPlugin plugin;
	
	public CornyListener(GuildsPlugin guildsPlugin) 
	{
		this.plugin = guildsPlugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		plugin.updateTabList();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		plugin.updateTabList();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		
		if(plugin.getGuildManager().getGuildChatters().contains(p))
		{			
			if(!plugin.getGuildManager().playerHasGuild(p))
			{
				plugin.getGuildManager().getGuildChatters().remove(p);
				return;
			}
			
			Guild guild = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
			
			List<Player> playersInGuild = plugin.getGuildManager().getOnlinePlayersInGuild(guild);
			
			e.setFormat(ChatColor.AQUA + "[GC] " + ChatColor.RESET + e.getFormat());
			
			for(Player onlinePlayer : Bukkit.getOnlinePlayers())
			{
				if(plugin.getGuildManager().getGuildChatSpies().contains(onlinePlayer))
					continue;
				
				if(!playersInGuild.contains(onlinePlayer))
				{
					System.out.println("Removed.");
					e.getRecipients().remove(onlinePlayer);
				}
			}
		}
		else
		{
			boolean didContain = plugin.getMainConfig().containsKey(Reference.CFG_DISPLAY_GUILD_TAG);
			
			if(plugin.getMainConfig().getOrSetString(Reference.CFG_DISPLAY_GUILD_TAG, "true").equalsIgnoreCase("true"))
			{				
				String name;
				
				if(plugin.getGuildManager().playerHasGuild(p))
					name = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId()).getName();
				else
					name = "Guildless";
				
				e.setFormat(ChatColor.AQUA + "[" + name + ChatColor.AQUA + "] " + ChatColor.RESET + e.getFormat());
			}
			
			if(!didContain)
			{
				try 
				{
					plugin.getMainConfig().save();
				} 
				catch (IOException ex) 
				{
					ex.printStackTrace();
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent e)
	{
		Player p = e.getPlayer();
		
		Guild blockClaimedBy = plugin.getGuildManager().getGuildClaimingBlock(e.getBlock());
		if(blockClaimedBy == null || blockClaimedBy.equals(plugin.getGuildManager().getGuildFromUUID(p.getUniqueId())))
			return;
		else
		{
			sendActionbarMessage(p, ChatColor.RED + "That's claimed by the \"" + blockClaimedBy.getName() + "\" guild.");
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		Guild blockClaimedBy = plugin.getGuildManager().getGuildClaimingBlock(e.getClickedBlock());
		if(blockClaimedBy == null || blockClaimedBy.equals(plugin.getGuildManager().getGuildFromUUID(p.getUniqueId())))
			return;
		else
		{
			sendActionbarMessage(p, ChatColor.RED + "That's claimed by the \"" + blockClaimedBy.getName() + "\" guild.");
			e.setCancelled(true);
		}
	}
	
	private void sendActionbarMessage(Player p, String message)
	{
		// TODO: Code
	}
}
