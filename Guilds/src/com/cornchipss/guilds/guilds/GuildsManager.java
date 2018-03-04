package com.cornchipss.guilds.guilds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GuildsManager 
{
	private List<Guild> guilds = new ArrayList<>();
	
	private List<Player> guildChatters = new ArrayList<>();
	private List<Player> guildChatSpies = new ArrayList<>();
	
	private File guildsStorage;
	
	public GuildsManager(String guildsFilePath) throws IOException
	{
		this.guildsStorage = new File(guildsFilePath);
		guildsStorage.createNewFile();
		
		String json = "";
		
		BufferedReader br = new BufferedReader(new FileReader(guildsStorage));
		for(String line = br.readLine(); line != null; line = br.readLine())
		{
			json += line;
		}
		br.close();
		
		if(!json.isEmpty())
		{
			Gson gson = new Gson();
			
			List<GuildJson> jsonList = Arrays.asList(gson.fromJson(json, GuildJson[].class));
			for(GuildJson guildJson : jsonList)
			{
				Guild guild = guildJson.toGuild();
				
				guilds.add(guild);
			}
		}
	}
	
	public void saveGuilds() throws IOException
	{
		removeUselessGuilds();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(guildsStorage));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<GuildJson> guildJson = new ArrayList<>();
		for(Guild g : guilds)
		{
			guildJson.add(GuildJson.fromGuild(g));
		}
		
		bw.write(gson.toJson(guildJson));
		bw.close();
	}
	
	public Guild getGuildFromUUID(UUID id)
	{
		for(Guild g : guilds)
			if(g.getMembers().contains(id))
				return g;
		return null;
	}
	
	public Guild getGuildFromName(String name) 
	{
		for(Guild g : guilds)
		{
			if(g.getName().equalsIgnoreCase(name))
				return g;
		}
		return null;
	}

	public boolean playerHasGuild(Player p) 
	{
		return getGuildFromUUID(p.getUniqueId()) != null;
	}
	
	public boolean removeUselessGuilds()
	{
		boolean anyRemoved = false;
		
		for(int i = 0; i < getGuilds().size(); i++)
		{			
			if(getGuilds().get(i).getMembers().size() == 0)
			{
				getGuilds().remove(i);
				i--;
				anyRemoved = true;
			}
		}
		
		return anyRemoved;
	}

	public List<Player> getOnlinePlayersInGuild(Guild guild) 
	{
		List<Player> onlinePlayers = new ArrayList<>();
		
		for(UUID id : guild.getMembers())
		{
			Player potentialPlayer = Bukkit.getPlayer(id);
			if(potentialPlayer != null)
				onlinePlayers.add(potentialPlayer);
		}
		
		return onlinePlayers;
	}
	
	public Guild getGuildClaimingBlock(Block block) 
	{
		return getGuildClaimingLocation(block.getLocation());
	}
	
	public Guild getGuildClaimingLocation(Location loc) 
	{
		for(Guild g : getGuilds())
		{
			for(Chunk c : g.getOwnedChunks())
			{
				if(loc.getChunk().equals(c))
				{
					return g;
				}
			}
		}
		return null;
	}
	
	public boolean canPlayerInteract(Player p, Location loc)
	{
		for(Guild g : getGuilds())
		{
			for(Chunk c : g.getOwnedChunks())
			{
				if(loc.getChunk().equals(c))
				{
					if(getOnlinePlayersInGuild(g).contains(p))
						return true; // Guild owned but player is in the guild
					else
						return false; // Guild owned and player not in that guild
				}
			}
		}
		
		// No guild owns that location
		return true;
	}

	public boolean createGuild(String name, Player founder) throws IOException
	{		
		if(name == null)
			return false;
		
		if(name.equalsIgnoreCase("wilderness") || name.equalsIgnoreCase("guildless"))
			return false;
		
		for(Guild g : getGuilds())
		{
			if(g.getName().equalsIgnoreCase(name))
				return false;
		}
		
		Map<UUID, GuildRank> members = new HashMap<>();
		members.put(founder.getUniqueId(), GuildRank.KING);
		
		getGuilds().add(new Guild(name, members, new ArrayList<>(), null, 0.0));
		
		saveGuilds();
		
		return true;
	}
	
	public void addPlayerToGuild(Player p, Guild g, GuildRank rank) throws IOException 
	{
		g.addMember(p.getUniqueId(), rank);
		saveGuilds();
	}

	public void deleteGuild(Guild g) throws IOException 
	{
		getGuilds().remove(g);
		saveGuilds();
	}
	
	public List<Player> getGuildChatSpies() { return guildChatSpies; }
	public void setGuildChatSpies(List<Player> guildChatSpies) { this.guildChatSpies = guildChatSpies; }
	
	public List<Player> getGuildChatters() { return guildChatters; }
	public void setGuildChatters(List<Player> guildChatters) { this.guildChatters = guildChatters; }

	public List<Guild> getGuilds() { return guilds; }
	public void setGuilds(List<Guild> guilds) { this.guilds = guilds; }
}
