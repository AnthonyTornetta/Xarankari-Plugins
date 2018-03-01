package com.cornchipss.guilds.guilds;

import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class Guild 
{
	private String name;
	private List<UUID> members;
	private List<Chunk> ownedChunks;
	private Location home;
	
	public Guild(String name, List<UUID> members, List<Chunk> ownedChunks, Location home)
	{
		this.name = name;
		this.members = members;
		this.ownedChunks = ownedChunks;
		
		this.home = home;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Guild)
		{
			Guild g = (Guild)obj;
			return name.equals(g.getName()) && members.equals(g.getMembers());
		}
		return false;
	}
	
	public void addOwnedChunk(Chunk c) 
	{
		getOwnedChunks().add(c);
	}
	
	public void removeOwnedChunk(Chunk c) 
	{
		getOwnedChunks().remove(c);
	}
	
	public void addMember(UUID uuid)
	{
		getMembers().add(uuid);
	}
	
	public void removeMember(UUID uuid) 
	{
		getMembers().remove(uuid);
	}
	
	// Getters & Setters //
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public List<UUID> getMembers() { return members; }
	public void setMembers(List<UUID> members) { this.members = members; }

	public List<Chunk> getOwnedChunks() { return ownedChunks; }
	public void setOwnedChunks(List<Chunk> ownedChunks) { this.ownedChunks = ownedChunks; }

	public Location getHome() { return home; }
	public void setHome(Location home) { this.home = home; }
}
