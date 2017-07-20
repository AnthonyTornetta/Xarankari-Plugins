package com.cornchipss.rpg;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;

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
		}
		
		return true;
	}

}
