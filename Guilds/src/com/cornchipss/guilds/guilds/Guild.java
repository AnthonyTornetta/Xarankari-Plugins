package com.cornchipss.guilds.guilds;

import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Guild 
{
	private String name;
	private List<UUID> members;
	private List<Chunk> ownedChunks;
	private Location home;
	private double balance;
	
	public Guild(String name, List<UUID> members, List<Chunk> ownedChunks, Location home, double balance)
	{
		this.name = name;
		this.members = members;
		this.ownedChunks = ownedChunks;
		
		this.balance = balance;
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
	
	public boolean addOwnedChunk(Chunk c) 
	{
		getOwnedChunks().add(c);
		return true;
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
	
	public boolean canWithdrawAmount(double amt)
	{
		return amt < balance && amt > 0;
	}
	
	public boolean withdrawAmount(double amt)
	{
		if(!canWithdrawAmount(amt))
			return false;
		balance -= amt;
		
		if(Double.isInfinite(balance) || Double.isNaN(balance))
		{
			balance = 0;
		}
		
		return true;
	}
	
	public void deposit(double amt)
	{
		balance += amt;
		
		if(Double.isInfinite(balance) || Double.isNaN(balance))
		{
			balance = 0;
		}
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
	
	public double getBalance() { return balance; }
	public void setBalance(double balance) { this.balance = balance; }

	public void toggleBorders() 
	{
		Material matToSetTo = Material.GOLD_BLOCK;
		
		for(Chunk c : getOwnedChunks())
		{
			World w = c.getWorld();
			
			// This starts at the northwest corner at y of 0
			Location locOfChunk = new Location(c.getWorld(), c.getX() * 16.0, 0.0, c.getZ() * 16.0);
			
			if(!getOwnedChunks().contains(w.getChunkAt(c.getX() + 1, c.getZ())))
			{
				// east
				Location temp = locOfChunk.clone().add(15, 0, 0);
				
				for(int z = 0; z < 16; z++)
				{
					w.getHighestBlockAt(temp.clone().add(0, 0, z)).getLocation().subtract(0, 1, 0).getBlock().setType(matToSetTo);
				}
			}
			if(!getOwnedChunks().contains(w.getChunkAt(c.getX() - 1, c.getZ())))
			{
				// west
				Location temp = locOfChunk.clone().subtract(0, 0, 0);
				
				for(int z = 0; z < 16; z++)
				{
					w.getHighestBlockAt(temp.clone().add(0, 0, z)).getLocation().subtract(0, 1, 0).getBlock().setType(matToSetTo);
				}
			}
			if(!getOwnedChunks().contains(w.getChunkAt(c.getX(), c.getZ() + 1)))
			{
				// south
				Location temp = locOfChunk.clone().add(0, 0, 15);
				
				for(int x = 0; x < 16; x++)
				{
					w.getHighestBlockAt(temp.clone().add(x, 0, 0)).getLocation().subtract(0, 1, 0).getBlock().setType(matToSetTo);
				}
			}
			if(!getOwnedChunks().contains(w.getChunkAt(c.getX(), c.getZ() - 1)))
			{
				// north
				Location temp = locOfChunk.clone().subtract(0, 0, 0);
				
				for(int x = 0; x < 16; x++)
				{
					w.getHighestBlockAt(temp.clone().add(x, 0, 0)).getLocation().subtract(0, 1, 0).getBlock().setType(matToSetTo);
				}
			}
		}
	}
}
