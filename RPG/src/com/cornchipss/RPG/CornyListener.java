package com.cornchipss.rpg;

import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.ConfigurationException;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.cornchipss.rpg.events.EntityMoveEvent;
import com.cornchipss.rpg.helper.Helper;
import com.cornchipss.rpg.helper.Reference;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;

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
	
	/**
	 * Whenever the player interacts with any entity it calls this
	 * @param e The entity it interacted with
	 * @throws ConfigurationException If the configuration file is not formatted correctly
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) throws ConfigurationException
	{
		Player p = e.getPlayer();
		Entity entity = e.getRightClicked();
		if(entity.hasMetadata("NPC"))
		{
			NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
			p.sendMessage(ChatColor.GREEN + npc.getName() + ChatColor.GREEN + "> " + "MURMCA");
			if(entity.getType().equals(EntityType.VILLAGER))
				entity.playEffect(EntityEffect.VILLAGER_HAPPY); // Happy villager :)
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerIneract(PlayerInteractEvent e)
	{
		Action a = e.getAction();
		Player p = e.getPlayer();
		
	}
	
	/**
	 * Whenever a block explods cancel it because we don't want anything blowing up :/
	 * @param e The explode event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockExplode(BlockExplodeEvent e)
	{
		// TODO: Add fancy block regen animation
		/*
		e.setYield(0);
		explodedBlocks.add(e.getBlock());
		*/
		e.setCancelled(true); // We don't want blocks exploding!
		System.out.println("FPLKMASDL:ADS");
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onNpcDeath(NPCDeathEvent e)
	{
		if(rpg.getConfig().contains(e.getNPC().getUniqueId().toString()))
		{
			rpg.getConfig().set(e.getNPC().getUniqueId().toString(), null); // Since they died there is no point in storing their value
		}
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
		
		///// Server Silence Checking
		
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
			return;
		}
		
		///// End Server Silence Checking
		
		///// Checking if the player is in the correct location for the chat to be sent
		
		if(!(message.length() >= 12 && message.substring(0, 12).toLowerCase().contains("global") && p.hasPermission("chat.global")))
		{
			for(Player recip : e.getRecipients())
			{
				double dist =  Helper.getDistance(p.getLocation(), recip.getLocation(), true);
				if(dist > Reference.DEFAULT_CHAT_BLOCKS || dist == -1)
					e.getRecipients().remove(recip);
			}
		}
		
		///// End checking if the player is within range for the chat
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
	/*public void regenBlocks()
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
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	private boolean blockIsSign(Block b)
	{
		return b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST;
	}
	*/
	
	/*
	 * 
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
	 * 
	 */
}