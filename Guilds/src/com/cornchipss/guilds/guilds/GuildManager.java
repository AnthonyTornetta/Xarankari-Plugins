package com.cornchipss.guilds.guilds;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.cornchipss.guilds.Guilds;
import com.cornchipss.guilds.ref.Reference;

public class GuildManager 
{
	private Guilds guilds;
	
	public GuildManager(Guilds guilds)
	{
		this.guilds = guilds;
	}
	
	public int getGuildIDFromUUID(UUID uuid)
	{
		if(guilds.getConfig().contains(uuid.toString()))
			return guilds.getConfig().getInt(uuid.toString());
		else
			return -1;
	}
	
	public String getGuildNameFromID(int id)
	{
		if(guilds.getConfig().contains(Reference.CFG_GUILD_PREFIX + id))
			return guilds.getConfig().getString(Reference.CFG_GUILD_PREFIX + id);
		else
			return null;
	}
	
	public ArrayList<Player> getOnlinePlayersInGuild(int id)
	{
		ArrayList<Player> playersInGuild = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(getGuildIDFromUUID(p.getUniqueId()) == id)
				playersInGuild.add(p);
		}
		
		return playersInGuild;
	}

	public boolean playerHasGuild(Player p) 
	{
		if(getGuildIDFromUUID(p.getUniqueId()) != -1)
			return true;
		return false;
	}
}
