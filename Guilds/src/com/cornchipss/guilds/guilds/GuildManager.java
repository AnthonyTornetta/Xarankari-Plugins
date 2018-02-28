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
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GuildManager 
{
	private List<Guild> guilds = new ArrayList<>();
	
	private List<Player> guildChatters = new ArrayList<>();
	private List<Player> guildChatSpies = new ArrayList<>();
	
	private File guildsStorage;
	
	public GuildManager(String guildsFilePath) throws IOException
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
			uuids.add("bede35f6-75aa-404c-b591-cf7d722ca8db");
			tempGuilds.add(new GuildJson("Armadale", uuids, new HashMap<>(), null));
			
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

	public List<Player> getGuildChatters() { return guildChatters; }
	public void setGuildChatters(List<Player> guildChatters) { this.guildChatters = guildChatters; }

	public List<Guild> getGuilds() { return guilds; }
	public void setGuilds(List<Guild> guilds) { this.guilds = guilds; }

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
}
