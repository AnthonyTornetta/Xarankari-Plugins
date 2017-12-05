package com.cornchipss.buildbattlesmini.arenas;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.buildbattlesmini.BuildBattleMini;
import com.cornchipss.buildbattlesmini.Reference;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;

public class Arena 
{
	Location bottomLeft, topRight;
	Player assignedPlayer;
	ArrayList<Block> blocksPlaced = new ArrayList<>();
	ArrayList<Thread> clearThreads = new ArrayList<>();
	String name;
	
	public Arena(Location bottomLeft, Location topRight, String name)
	{
		this.bottomLeft = bottomLeft;
		this.topRight = topRight;
		this.name = name;
	}
	
	public void clear()
	{
		ArenaCleaner ac = new ArenaCleaner(blocksPlaced);
		ac.start();
	}
	
	public boolean placeBlock(Block b)
	{
		if(!Reference.locWithin(b.getLocation(), bottomLeft, topRight))
			return false;		
		blocksPlaced.add(b);
		return true;
	}
	
	public boolean breakBlock(Block b)
	{
		if(blocksPlaced.contains(b))
			return true;
		return false;
	}
	
	public boolean assign(Player p)
	{
		if(assignedPlayer != null)
		{
			assignedPlayer = p;
			return true;
		}
		return false; // Theres already a player assigned to this plot
	}
	
	public boolean playerLeave(Player p)
	{
		if(p.equals(assignedPlayer))
		{
			assignedPlayer = null;
			return true;
		}
		return false;
	}
	
	public Player getPlayer()
	{
		return assignedPlayer;
	}

	public void sendMessage(String string) 
	{
		assignedPlayer.sendMessage(string);
	}
	
	public void sendMessage(ChatMessageType cmt, BaseComponent[] msg) 
	{
		assignedPlayer.spigot().sendMessage(cmt, msg);
	}

	public void broadcast(String title, String msg, int i, int j, int k) 
	{
		assignedPlayer.sendTitle(title, msg, i, j, k);
	}

	public Location getLocation() 
	{
		double x = (bottomLeft.getX() + topRight.getX()) / 2;
		double z = (bottomLeft.getZ() + topRight.getZ()) / 2;
		double y = bottomLeft.getY();
		Location temp = bottomLeft.clone();
		while(temp.add(0, 1, 0).getBlock().getType() != Material.AIR)
		{
			y++;
		}
		
		return new Location(bottomLeft.getWorld(), x, y, z);
	}

	public void playSound(Sound sound, int i, int j) 
	{
		assignedPlayer.playSound(assignedPlayer.getLocation(), sound, i, j);
	}
	
	public void reset(HashMap<Player, ItemStack[]> playerInventories) 
	{
		assignedPlayer.setGameMode(GameMode.SURVIVAL);
		assignedPlayer.playSound(assignedPlayer.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
		assignedPlayer.getInventory().clear();
		assignedPlayer.getInventory().setContents(playerInventories.get(assignedPlayer));
		
		assignedPlayer.sendTitle(ChatColor.DARK_RED + "BUILD BATTLE OVER!", "", 20, 40, 20);
		
		clear();
	}
	
	public void begin(BuildBattleMini bb) 
	{
		assignedPlayer.teleport(getLocation());
		assignedPlayer.sendMessage(ChatColor.GREEN + "Build battle beginning!");
		bb.getPlayerInventories().put(assignedPlayer, assignedPlayer.getInventory().getContents());
		
		assignedPlayer.getInventory().clear();
		assignedPlayer.setGameMode(GameMode.CREATIVE);
		
		assignedPlayer.sendTitle(ChatColor.GREEN + "Get Ready!", ChatColor.GREEN + "Build Battle Beginning in 5 seconds!", 20, 40, 20);
	}
	
	public boolean contains(CommandSender sender) 
	{
		return assignedPlayer.equals(sender);
	}
	
	public String getName()  { return name; }

	public int getPlayerCount() 
	{
		if(assignedPlayer != null && assignedPlayer.isOnline())
			return 1;
		return 0;
	}
}
