package com.cornchipss.rpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.cornchipss.rpg.events.EntityMoveEvent;

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
	
	String[] npcRetort =
		{
				"Ouch! Stop that.",
				"Insert Generic 'don't attack me' here",
				"Insert Generic 'don't attack me' here",
				"Insert Generic 'don't attack me' here"
		};
	
	public CornyListener(RPG rpg)
	{
		this.rpg = rpg;
	}
	
	/**
	 * Whenever the player interacts with any entity it calls this
	 * @param e The entity it interacted with
	 * @throws ConfigurationException If the configuration file is not formatted correctly
	 */
	public void onPlayerInteract(PlayerInteractEntityEvent e) throws ConfigurationException
	{
		Player p = e.getPlayer();
		Entity ent = e.getRightClicked();
		
		UUID id = ent.getUniqueId(); // The UUID of the entity
		
		// If the config file contains the entity's UUID then look for the string to share w/ the player
		if(rpg.getConfig().contains(id.toString()))
		{
			String data = rpg.getConfig().get(id.toString()).toString().toLowerCase();
			// <br> is used for data formatting NOT new paragraphs, use \n for a new line
			String[] split = data.split("<br>"); 
			// [npc-text] is when the entity tells you something
			if(split[0].equals("[npc-text]"))
			{
				// Make sure it has all the arguments required
				if(split.length < 3)
					throw new ConfigurationException("Invalid format in the configuration file for the npc with a UUID of " + id);
				
				// Make sure the level required is a vaild integer
				if(!Helper.isInteger(split[2]) || Integer.parseInt(split[2]) < 0)
					throw new ConfigurationException("Invalid format in the configuration file for the npc with a UUID of " + id);
				
				// Get the minimum level required to read the NPC's text
				int minRequiredLevel = Integer.parseInt(split[2]);
				
				// TODO: Add skillapi level checking here to see if they are of the required level to interact
				
				// To add multiple lines
				String[] withParagraphs = split[1].split("\\n");
				
				for(int i = 0; i < withParagraphs.length; i++)
				{
					// Used to display text with multiple lines to the user
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', withParagraphs[i]));
				}
				
				// It doesn't have to be a villager, but if it is it'l be a happy one :)
				if(e instanceof Villager)
					((Villager) e).playEffect(EntityEffect.VILLAGER_HAPPY); // Happy villager :)
				
				
				
				// I don't want any shopping menus to appear when all they want is dialogue
				e.setCancelled(true);
			}
		}
	}
	
	/**
	 * Whenever a block explods cancel it because we don't want anything blowing up :/
	 * @param e The explode event
	 */
	public void onBlockExplode(BlockExplodeEvent e)
	{
		// TODO: Add fancy block regen animation
		/*
		e.setYield(0);
		explodedBlocks.add(e.getBlock());
		*/
		e.setCancelled(true); // We don't want blocks exploding!
	}
	
	/**
	 * 
	 * @param e
	 */
	public void onEntityDeath(EntityDeathEvent e)
	{
		if(rpg.getConfig().contains(e.getEntity().getUniqueId().toString()))
		{
			rpg.getConfig().set(e.getEntity().getUniqueId().toString(), null); // Since they died there is no point in storing their value
		}
	}
	
	/**
	 * Whenever an entity takes damage do this
	 * @param e The entity that took damage
	 * @throws ConfigurationException If the configuration file is incorrect
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageByEntityEvent e) throws ConfigurationException
	{
		Entity attacker = e.getDamager();
		Entity target = e.getEntity();
		UUID uuid = target.getUniqueId();
		
		// If the entity is a specific NPC we want to cancel all damage
		if(rpg.getConfig().contains(uuid.toString()))
		{
			if(attacker instanceof Player)
			{
				Player p = (Player)attacker;
				String[] split = rpg.getConfig().get(uuid.toString()).toString().split(Reference.SPLIT_KEYWORD);
				
				if(split.length < 2)
				{
					throw new ConfigurationException("Invalid format in the configuration file for the npc with a UUID of " + uuid.toString());
				}
				
				String name = split[1];
				Random rdm = new Random();
				// Get a random retort from the array above to shout at the player
				String retort = npcRetort[rdm.nextInt(npcRetort.length)];
				
				p.sendMessage(ChatColor.GOLD + name + ChatColor.RED + "> " + retort);
			}
			e.setCancelled(true);
		}
		
		// TODO: Do a sweeping edge attack type thing
		/*
		// This isn't working atm
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
		*/
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityMove(EntityMoveEvent e)
	{
		// TODO: Use barriers
	}
	
	/**
	 * Whenever a player chats do this
	 * @param e The player chat event
	 */
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		String message = e.getMessage();
		
		// Make it a fancy golden color
		if(p.getName().toLowerCase() == "cornchipss" || p.getName().toLowerCase() == "joey_dev")
		{
			message = ChatColor.GOLD + message;
			e.setMessage(message);
		}
		
		// Mute everyone if it contains a message that mute everyone
		if(strContains(message, muteMsgs))
		{
			if(p.hasPermission("management.mute.all"))
			{
				serverSilence = true;
				return;
			}
			// If they don't have perms for this they will just look stupid and nothing will happen :)
		}
		
		// Unmute everyone if the chat message contains an unmute string
		if(strContains(message, unmuteMsgs))
		{
			if(p.hasPermission("management.mute.all"))
			{
				serverSilence = false;
				return;
			}
		}
		
		// Still chat to the server if they can resist the mute, but otherwise block it
		if(serverSilence && !p.hasPermission("management.resist.mute"))
		{
			p.sendMessage(ChatColor.GRAY + "You seem to be at a loss for words");
			e.setCancelled(true);
		}
		
		
	}
	
	/**
	 * Checks if a string contains something in an array of strings
	 * @param str The string
	 * @param possibilities The strings it could contain
	 * @return True if it contained one of the strings found in the array
	 */
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
	
	/**
	 * Regenerates all the blocks in the explodedBlocks arraylist
	 */
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
	
	// TODO make this a fancy regen animation type thingy
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