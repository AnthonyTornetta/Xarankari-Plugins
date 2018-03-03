package com.cornchipss.guilds.listeners;

import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.cornchipss.guilds.GuildsPlugin;
import com.cornchipss.guilds.guilds.Guild;
import com.cornchipss.guilds.ref.Reference;
import com.cornchipss.guilds.util.Vector2;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PluginListener implements Listener
{
	private GuildsPlugin plugin;
	
	public PluginListener(GuildsPlugin guildsPlugin) 
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
	public void onBlockExplode(BlockExplodeEvent e)
	{
		for(int i = 0; i < e.blockList().size(); i++)
		{
			Block b = e.blockList().get(i);
			
			if(plugin.getGuildManager().getGuildClaimingBlock(b) != null)
			{
				e.blockList().remove(b);
				i--;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityExplode(EntityExplodeEvent e)
	{
		for(int i = 0; i < e.blockList().size(); i++)
		{
			Block b = e.blockList().get(i);
			
			if(plugin.getGuildManager().getGuildClaimingBlock(b) != null)
			{
				e.blockList().remove(b);
				i--;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPistonExtend(BlockPistonExtendEvent e)
	{
		Guild pistonGuild = plugin.getGuildManager().getGuildClaimingBlock(e.getBlock());
		
		Vector2<Integer, Integer> moveDir = new Vector2<>(0, 0);
		
		if(e.getDirection() == BlockFace.NORTH)
		{
			moveDir.setY(-1);
		}
		else if(e.getDirection() == BlockFace.SOUTH)
		{
			moveDir.setY(1);
		}
		else if(e.getDirection() == BlockFace.EAST)
		{
			moveDir.setX(1);
		}
		else if(e.getDirection() == BlockFace.WEST)
		{
			moveDir.setX(-1);
		}
		else
			return;
		
		for(Block b : e.getBlocks())
		{
			Guild messedWithGuild = plugin.getGuildManager().getGuildClaimingBlock(b.getLocation().add(moveDir.getX(), 0, moveDir.getY()).getBlock());
			
			if(pistonGuild == null)
			{
				if(messedWithGuild != null)
				{
					e.setCancelled(true);
					return;
				}
			}
			else
			{
				if(!pistonGuild.equals(messedWithGuild))
				{
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPistonRetract(BlockPistonRetractEvent e)
	{		
		if(!e.isSticky())
			return;
		
		Guild pistonGuild = plugin.getGuildManager().getGuildClaimingBlock(e.getBlock());
		
		for(Block b : e.getBlocks())
		{
			Guild messedWithGuild = plugin.getGuildManager().getGuildClaimingBlock(b);
			
			if(pistonGuild == null)
			{
				if(messedWithGuild != null)
				{
					e.setCancelled(true);
					return;
				}
			}
			else
			{
				if(!pistonGuild.equals(messedWithGuild))
				{
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	 
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFromTo(BlockFromToEvent e) 
    {
        Block block = e.getBlock();
        Block blockTo = e.getToBlock();
        
        Guild gFrom = plugin.getGuildManager().getGuildClaimingBlock(block);
        Guild gTo = plugin.getGuildManager().getGuildClaimingBlock(blockTo);
        
        if(gTo == null)
        	return;
        
        // It's flowing from wilderness into guild territory
        if(gFrom == null)
        {
        	e.setCancelled(true);
        	return;
        }
        
        if(!gFrom.equals(gTo))
        {
        	e.setCancelled(true);
        }
	}
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockSpread(BlockSpreadEvent e) 
    {
    	if(plugin.getGuildManager().getGuildClaimingBlock(e.getBlock()) != null)
    		e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
    	if(!(e.getDamager() instanceof Player))
    		return;
    	
    	Player p = (Player)(e.getDamager());
    	
    	Guild gFrom = plugin.getGuildManager().getGuildFromUUID(p.getUniqueId());
        Guild gTo = plugin.getGuildManager().getGuildClaimingLocation(e.getEntity().getLocation());
        
        Entity ent = e.getEntity();
    	if(ent instanceof Monster)
    	{
    		return; // They can kill hostiles
    	}
        
        if(gTo == null)
        	return;
        
        // damageer from wilderness into guild territory
        if(gFrom == null)
        {
        	e.setCancelled(true);
        }
        
        if(!gFrom.equals(gTo))
        {
        	e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityInteract(EntityInteractEvent e)
    {
    	Block b = e.getBlock();
    	if(plugin.getGuildManager().getGuildClaimingBlock(b) != null)
    		e.setCancelled(true);
    }
    
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockInteract(PlayerInteractEvent e)
	{
		if(e.getClickedBlock() == null)
			return;
		
		Player p = e.getPlayer();
		
		if(!plugin.getGuildManager().canPlayerInteract(p, e.getClickedBlock().getLocation()))
		{
			sendActionbarMessage(p, ChatColor.RED + "That's claimed by the \"" + plugin.getGuildManager().getGuildClaimingBlock(e.getClickedBlock()).getName() + "\" guild.");
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent e)
	{
		Location loc = e.getTo();
		Location locFrom = e.getFrom();
		
		Guild fromGuild = plugin.getGuildManager().getGuildClaimingLocation(locFrom);
		Guild toGuild = plugin.getGuildManager().getGuildClaimingLocation(loc);
		
		Player p = e.getPlayer();
		
		if(fromGuild != null)
		{
			if(toGuild == null)
			{
				sendActionbarMessage(p, ChatColor.GREEN + "~ Wilderness ~");
			}
			else if(!fromGuild.equals(toGuild))
			{
				sendActionbarMessage(p, ChatColor.GREEN + "~ " + toGuild.getName() + " ~");
			}
		}
		else
		{
			if(toGuild != null)
			{
				sendActionbarMessage(p, ChatColor.GREEN + "~ " + toGuild.getName() + " ~");
			}
		}
	}
	
	private void sendActionbarMessage(Player p, String message)
	{
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}
}
