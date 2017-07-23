package com.cornchipss.rpg;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class CommandRegistry 
{
	public static final String[] CREATE_SUB_CMDS = 
	{
		"npc: Creates an npc"
	};
	
	
	public static boolean check(CommandSender sender, Command command, String label, String[] args, RPG rpg) 
	{
		String cmd = command.getLabel().toLowerCase();
		switch(cmd)
		{
			case "create":
			{
				if(!(sender instanceof Player))
				{
					sender.sendMessage(ChatColor.RED + "This is a player-only command!");
					return true;
				}
				Player p = (Player)sender;
				
				
				if(args.length < 1)
				{
					p.sendMessage(Reference.DEFAULT_CC + "Create sub commands");
					p.sendMessage(Reference.DEFAULT_CC + "+=================+");
					for(int i = 0; i < CREATE_SUB_CMDS.length; i++)
					{
						p.sendMessage(CREATE_SUB_CMDS[i]);
					}
					break;
				}
				
				String createWhat = args[0].toLowerCase();
				switch(createWhat)
				{
					case "npc":
					{
						NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, "Big man Al");
				        npc.spawn(p.getLocation());
				        npc.setProtected(true);
				        npc.setFlyable(true);
				        p.sendMessage(ChatColor.GREEN + "An npc was created");
				        break;
					}
					default:
					{
						p.sendMessage(Reference.DEFAULT_CC + "Create sub commands");
						p.sendMessage(Reference.DEFAULT_CC + "+=================+");
						for(int i = 0; i < CREATE_SUB_CMDS.length; i++)
						{
							p.sendMessage(CREATE_SUB_CMDS[i]);
						}
						break;
					}
				}
				
				break;
			}
			case "asdf":
			{
				if(!(sender instanceof Player))
				{
					sender.sendMessage(ChatColor.RED + "This is a player-only command!");
					return true;
				}
				Player p = (Player)sender;
				@SuppressWarnings("deprecation")
				Block b = p.getTargetBlock((HashSet<Byte>)null, 50);
				
				ArrayList<Block> found = new ArrayList<Block>();
				ArrayList<Block> searchBlocks = new ArrayList<Block>();
				ArrayList<Block> toSearch = new ArrayList<Block>();
				
				searchBlocks.add(b);
				
				while(true)
				{
					toSearch.clear();
					for(Block bs : searchBlocks)
					{
						if(!found.contains(bs))
						{
							found.add(bs);
						}
						
						ArrayList<Block> fetched = RPG.getSurrounding(bs);
						for(Block bf : fetched)
						{
							if(found.contains(bf) || toSearch.contains(bf)) 
								continue;
							toSearch.add(bf);
						}
					}
					
					if(toSearch.size() == 0)
						break;
					else
					{
						searchBlocks.clear();
						searchBlocks.addAll(toSearch);
						toSearch.clear();
						
						if(found.size() > 1000)
						{
							rpg.getLogger().info("Too many blocks - stopping the transformation");
							break;
						}
						
					}
				}
				for(Block bf : found)
					bf.setType(Material.WOOL);
				return true;
			}
		}		
		return true;
	}

}
