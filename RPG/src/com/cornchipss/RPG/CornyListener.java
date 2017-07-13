package com.cornchipss.RPG;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CornyListener implements Listener
{
	RPG rpg;
	ArrayList<Player> mutedPlayers = new ArrayList<>();
	boolean serverSilence = false;
	
	public CornyListener(RPG rpg)
	{
		this.rpg = rpg;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		String message = e.getMessage();
		if(message.toLowerCase().contains("everyone stop talking"))
		{
			if(p.hasPermission("management.mute.all"))
			{
				serverSilence = true;
				return;
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		if(serverSilence &&  !p.hasPermission("management.mute.all"))
		{
			
		}
	}
}