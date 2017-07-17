package com.cornchipss.buildbattle;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CornyListener implements Listener
{
	BuildBattle bb;
	
	Material blackListedBlocks[] =
		{
			Material.BEDROCK,
			Material.BARRIER
		};
	
	Material storageBlocks[] =
		{
			Material.CHEST,
			Material.ENDER_CHEST,
			Material.BLACK_SHULKER_BOX,
			Material.BLUE_SHULKER_BOX,
			Material.BROWN_SHULKER_BOX,
			Material.CYAN_SHULKER_BOX,
			Material.GRAY_SHULKER_BOX,
			Material.GREEN_SHULKER_BOX,
			Material.LIGHT_BLUE_SHULKER_BOX,
			Material.LIME_SHULKER_BOX,
			Material.MAGENTA_SHULKER_BOX,
			Material.ORANGE_SHULKER_BOX,
			Material.PINK_SHULKER_BOX,
			Material.PURPLE_SHULKER_BOX,
			Material.RED_SHULKER_BOX,
			Material.SILVER_SHULKER_BOX,
			Material.WHITE_SHULKER_BOX,
			Material.YELLOW_SHULKER_BOX
		};
	
	public CornyListener(BuildBattle bb)
	{
		this.bb = bb;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerSendCommand(PlayerCommandPreprocessEvent e)
	{
		if(!bb.getPlayers().contains(e.getPlayer()))
			return; // Player isn't in the build battle
		
		if(bb.isRunning())
		{
			if(!e.getMessage().split(" ")[0].equals("/bb") && !e.getMessage().split(" ")[0].equals("/buildbattle"))
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED + "You cannot execute non-buildbattle commands during a build battle!");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItem(PlayerDropItemEvent e)
	{
		if(!bb.getPlayers().contains(e.getPlayer()))
			return; // Player isn't in the build battle
		
		if(bb.isRunning())
		{
			e.getItemDrop().remove(); // Remove it instantly, Don't want them throwing items on the ground ;)
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if(bb.isRunning())
		{
			Action a = e.getAction();
			if(a.equals(Action.RIGHT_CLICK_BLOCK))
			{
				Material bm = e.getClickedBlock().getType();
				for(Material m : storageBlocks)
				{
					if(m.equals(bm))
					{
						e.getPlayer().sendMessage(ChatColor.RED + "You cannot access any storage containers during a build battle!");
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			if(((Player)e.getEntity()).getName().toLowerCase().equals("cornchipss"))
			{
				Player p = (Player)e.getEntity();
				Location l = p.getLocation();
				World w = p.getWorld();
				
				int radius = 20;
		        
		        /*for(int i = 0; i < precision; i++) 
		        {
			        Bukkit.getScheduler().scheduleSyncDelayedTask(bb, new LightItUp(i, precision, radius, w, l), 0);
			    }*/
		        
		        double x;
		        double y = l.getY();
		        double z;
		               
		        for (double i = 0.0; i < 360.0; i += 2) {
		        double angle = i * Math.PI / 180;
		            x = (int)(l.getX() + radius * Math.cos(angle));
		            z = (int)(l.getZ() + radius * Math.sin(angle));
		     
		            w.strikeLightning(new Location(w, x, y, z));
		        }
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBreak(BlockBreakEvent e)
	{
		if(bb.isRunning())
		{
			Block b = e.getBlock();
			Player p = e.getPlayer();
						
			for(int i = 0; i < blackListedBlocks.length; i++)
			{
				if(b.getType().equals(blackListedBlocks[i]))
				{
					break;
				}
				if(i + 1 == blackListedBlocks.length)
					return; // It's not blacklisted so we don't care
			}
			
			if(bb.getPlayers().contains(p))
			{
				if(!p.hasPermission("bb.break.blacklisted"))
				{
					p.sendMessage(ChatColor.RED + "You cannot break that block during a build battle");
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPlace(BlockPlaceEvent e)
	{
		if(bb.isRunning())
		{
			Block b = e.getBlock();
			Player p = e.getPlayer();
						
			for(int i = 0; i < blackListedBlocks.length; i++)
			{
				if(b.getType().equals(blackListedBlocks[i]))
				{
					break;
				}
				if(i + 1 == blackListedBlocks.length)
					return; // It's not blacklisted so we don't care
			}
			
			if(bb.getPlayers().contains(p))
			{
				if(!p.hasPermission("bb.break.blacklisted"))
				{
					p.sendMessage(ChatColor.RED + "You cannot place that block during a build battle");
					e.setCancelled(true);
				}
			}
		}
	}
}
