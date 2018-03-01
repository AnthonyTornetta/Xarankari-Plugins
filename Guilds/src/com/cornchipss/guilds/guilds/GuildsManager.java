package com.cornchipss.guilds.guilds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		
		if(json.isEmpty())
		{
			List<GuildJson> tempGuilds = new ArrayList<>();
			List<String> uuids = new ArrayList<>();
			uuids.add("2e3f560c-7495-401c-98c6-d21b4460ad3c");
			tempGuilds.add(new GuildJson("Armadale", uuids, new ArrayList<>(), null));
			
			for(GuildJson guildJson : tempGuilds)
			{
				guilds.add(guildJson.toGuild());
			}
			
			saveGuilds();
		}
		else
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
		for(Guild g : getGuilds())
		{
			for(Chunk c : g.getOwnedChunks())
			{
				if(block.getChunk().equals(c))
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
		for(Guild g : getGuilds())
		{
			if(g.getName().equalsIgnoreCase(name))
				return false;
		}
		
		List<UUID> members = new ArrayList<>();
		members.add(founder.getUniqueId());
		
		getGuilds().add(new Guild(name, members, new ArrayList<>(), null));
		
		saveGuilds();
		
		return true;
	}
	
	public void addPlayerToGuild(Player p, Guild g) throws IOException 
	{
		g.getMembers().add(p.getUniqueId());
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
