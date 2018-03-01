package com.cornchipss.guilds.guilds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;

import com.cornchipss.guilds.util.Serializer;
import com.cornchipss.guilds.util.Vector3;

public class GuildJson 
{
	private String guildName;
	private List<String> members;
	List<Vector3<String, Integer, Integer>> ownedChunks;
	private String guildHome;
	
	public GuildJson(String name, List<String> members, List<Vector3<String, Integer, Integer>> ownedChunkLocations, String homeLocation)
	{
		this.guildName = name;
		this.members = members;
		this.ownedChunks = ownedChunkLocations;
		this.guildHome = homeLocation;
	}
	
	public Guild toGuild()
	{
		List<UUID> memberUuids = new ArrayList<>();
		for(String s : members)
		{
			memberUuids.add(UUID.fromString(s));
		}
		
		return new Guild(guildName, memberUuids, Serializer.deserializeChunks(ownedChunks), Serializer.deserializeLocation(guildHome));
	}
	
	public static GuildJson fromGuild(Guild g)
	{
		List<String> memberUUIDStrings = new ArrayList<>();
		for(UUID id : g.getMembers())
		{
			memberUUIDStrings.add(id.toString());
		}
		
		List<Vector3<String, Integer, Integer>> ownedChunkLocations = new ArrayList<>();
		
		for(Chunk c : g.getOwnedChunks())
		{
			ownedChunkLocations.addAll(Serializer.serializeChunk(c));
		}
		
		String homeLocation = Serializer.serializeLocation(g.getHome());
		
		return new GuildJson(g.getName(), memberUUIDStrings, ownedChunkLocations, homeLocation);
	}
}
