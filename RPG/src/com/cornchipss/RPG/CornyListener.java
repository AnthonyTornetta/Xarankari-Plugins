package com.cornchipss.rpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.naming.ConfigurationException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class CornyListener implements Listener
{
	HashMap<Entity, Location> dontMove = new HashMap<>();
	RPG rpg;
	ArrayList<Player> mutedPlayers = new ArrayList<>();
	boolean serverSilence = false;
	
	ArrayList<Block> explodedBlocks = new ArrayList<>();
	
	String[] muteMsgs =
		{
			"noone speak",
			"no one speak",
			"everyone stop talking",
			"everyone be quiet",
		};
	
	String[] unmuteMsgs = 
		{
			"you can all talk again",
			"everyone can talk again",
			"you may resume talking",
			"you may resume speech",
			"you can talk now",
			"you can talk",
			"everyone can speek"
		};
	
	public CornyListener(RPG rpg)
	{
		this.rpg = rpg;
	}
	
	public void onPlayerInteract(PlayerInteractEntityEvent e) throws ConfigurationException
	{
		Player p = e.getPlayer();
		Entity ent = e.getRightClicked();
		
		UUID id = ent.getUniqueId();
		if(rpg.getConfig().contains(id.toString()))
		{
			String data = rpg.getConfig().get(id.toString()).toString().toLowerCase();
			String[] split = data.split("<br>");
			if(split[0].equals("[npc-text]"))
			{
				if(split.length < 2)
					throw new ConfigurationException("Invalid format in the configuration file for the npc with a UUID of " + id);
				
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', split[1]));
				if(e instanceof Villager)
					((Villager) e).playEffect(EntityEffect.VILLAGER_HAPPY); // Happy villager :)
			}
		}
	}
	
	public void onBlockExplode(BlockExplodeEvent e)
	{
		e.setYield(0);
		explodedBlocks.add(e.getBlock());
		//e.setCancelled(true); // We don't want blocks exploding!
	}
	
	public void onEntityDeath(EntityDeathEvent e)
	{
		if(rpg.getConfig().contains(e.getEntity().getUniqueId().toString()))
		{
			rpg.getConfig().set(e.getEntity().getUniqueId().toString(), null); // Since they died there is no point in storing their value
		}
	}
	
	/*
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageByEntityEvent e)
	{
		Entity attacker = e.getDamager();
		Entity target = e.getEntity();
		
		// This is a test
		if(e.getCause() == DamageCause.ENTITY_ATTACK)
		{
			if(attacker instanceof Player)
			{
				ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) attacker.getNearbyEntities(5, 2, 5);
				if(e.getFinalDamage() - 1 >= 1)
				{
					for(int i = 0; i < nearbyEntities.size(); i++)
					{
						EntityDamageByEntityEvent dmgEvent = e; 
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityMove(EntityMoveEvent e)
	{
		
	}
	*/
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		String message = e.getMessage();
		
		if(p.getName().toLowerCase() == "cornchipss")
		{
			message = ChatColor.GOLD + message;
			e.setMessage(message);
		}
		
		if(strContains(message, muteMsgs))
		{
			if(p.hasPermission("management.mute.all"))
			{
				serverSilence = true;
				return;
			}
		}
		
		if(strContains(message, unmuteMsgs))
		{
			if(p.hasPermission("management.mute.all"))
			{
				serverSilence = false;
				return;
			}
		}
		
		if(serverSilence && !p.hasPermission("management.resist.mute"))
		{
			p.sendMessage(ChatColor.GRAY + "You seem to be at a loss for words");
			e.setCancelled(true);
		}
		
		
	}
	
	boolean strContains(String str, String[] possibilities)
	{
		for(int i = 0; i < possibilities.length; i++)
		{
			if(str.toLowerCase().contains(possibilities[i].toLowerCase()))
			{
				return true;
			}
		}
		return false;
	}
	
	public void regenAllBlocks()
	{
		for(int i = 0; i < explodedBlocks.size(); i++)
		{
			Block b = explodedBlocks.get(i);
			Location l = b.getLocation();
			
			if(l.getWorld().getBlockAt(l).getType() == Material.AIR)
			{
				l.getBlock().setType(b.getType());
				l.getWorld().playSound(l, Sound.BLOCK_LAVA_POP, 2.0f, 2.0f);
			}
		}
	}
	
	// TODO make this
	public void regenBlocks()
	{
		ArrayList<Block> blocksToRegen = explodedBlocks;
		for(int i = 0; i < explodedBlocks.size() / 4; i++)
		{
			Block b = explodedBlocks.get(i);
			Location l = b.getLocation();
			
			if(l.getWorld().getBlockAt(l).getType() == Material.AIR)
			{
				l.getBlock().setType(b.getType());
				l.getWorld().playSound(l, Sound.BLOCK_LAVA_POP, 2.0f, 2.0f);
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(rpg, new Runnable() 
		{
            public void run() 
            {
            	for(int i = 0; i < explodedBlocks.size() ; i++)
        		{
        			Block b = explodedBlocks.get(i);
        			Location l = b.getLocation();
        			
        			if(l.getWorld().getBlockAt(l).getType() == Material.AIR)
        			{
        				l.getBlock().setType(b.getType());
        				l.getWorld().playSound(l, Sound.BLOCK_LAVA_POP, 2.0f, 2.0f);
        			}
        		}
            }
        }, 20L);
	}
}