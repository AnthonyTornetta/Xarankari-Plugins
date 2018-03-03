package com.cornchipss.guilds.guilds;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.cornchipss.guilds.money.CurrencyExchangeResult;

public class Guild 
{
	private String name;
	private Map<UUID, GuildRank> members;
	private List<Chunk> ownedChunks;
	private Location home;
	private double balance;
	
	/**
	 * A group of players in a membership that has claimed land<br>
	 * @param name The name of the guild to show up to players
	 * @param members The members in the guild
	 * @param ownedChunks The chunks the guild owns
	 * @param home The home of the guild (null if none)
	 * @param balance The balance of the guild
	 */
	public Guild(String name, Map<UUID, GuildRank> members, List<Chunk> ownedChunks, Location home, double balance)
	{
		this.ownedChunks = ownedChunks;
		this.members = members;
		this.balance = balance;
		this.name = name;
		this.home = home;	
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Guild)
		{
			Guild g = (Guild)obj;
			return name.equals(g.getName()) && members.equals(g.getMembersFull());
		}
		return false;
	}
	
	/**
	 * Checks if the chunk should be added
	 * @param c The chunk to check
	 * @return true if it can be added - false if not
	 */
	public boolean shouldAddChunk(Chunk c)
	{
		return true;
	}
	
	/**
	 * Adds an owned chunks to the owned chunks of the faction<br>
	 * Prevents duplicate additions
	 * @param c The chunk to add
	 */
	public void addOwnedChunk(Chunk c) 
	{
		if(getOwnedChunks().contains(c))
			return;
		getOwnedChunks().add(c);
	}
	
	/**
	 * Checks if the chunk should be removed
	 * @param c The chunk to check
	 * @return true if it can be removed - false if not
	 */
	public boolean shouldRemoveChunk(Chunk c)
	{
		return true;
	}
	
	/**
	 * Removes an owned chunk from the owned chunks of the guild<br>
	 * Sets the faction home to null if the home was in said chunk
	 * @param c The chunk to remove
	 */
	public void removeOwnedChunk(Chunk c) 
	{
		getOwnedChunks().remove(c);
		if(getHome().getChunk().equals(c))
			setHome(null);
	}
	
	/**
	 * Adds a member to the guild<br>
	 * This will override an existing member with the same UUID
	 * @param uuid The uuid of the member
	 * @param rank The rank of the member
	 */
	public void addMember(UUID uuid, GuildRank rank)
	{
		getMembersFull().put(uuid, rank);
	}
	
	/**
	 * Gets a member's rank
	 * @param uuid The member's UUID
	 * @return The member's rank
	 */
	public GuildRank getMemberRank(UUID uuid)
	{
		return getMembersFull().get(uuid);
	}
	
	/**
	 * Removes a member from the guild
	 * @param uuid The UUID of the member to remove
	 */
	public void removeMember(UUID uuid) 
	{
		getMembersFull().remove(uuid);
	}
	
	/**
	 * Checks if you can withdraw a given amount from the guild's balance (no withdraw is actually made)
	 * @param amt The amount to test
	 * @return true if you are able - false if not
	 */
	public boolean canWithdrawAmount(double amt)
	{
		return amt < balance && amt > 0;
	}
	
	/**
	 * Withdraws an amount from the guild's balance - note that if the new balance is Infinity, NaN, or negative the balance is reset to 0
	 * @param amt The amount to deposit (must be > 0)
	 * @return NOT_ENOUGH_FUNDS - If the balance didn't have enough funds <br>
	 * 	       BALANCE_RESET - If the balance was in an unsafe bounds and it was reset to 0 <br> 
	 * 	       SUCCESS - The deposit was a success
	 */
	public CurrencyExchangeResult withdrawAmount(double amt)
	{
		if(!canWithdrawAmount(amt))
			return CurrencyExchangeResult.NOT_ENOUGH_FUNDS;
		balance -= amt;
		
		return checkBalance();
	}
	
	/**
	 * Deposits an amount into the guild's balance - note that if the new balance is Infinity, NaN, or negative the balance is reset to 0
	 * @param amt The amount to deposit (must be >= 0)
	 * @return NOT_ENOUGH_FUNDS - Amount was < 0 <br>
	 * 	       BALANCE_RESET - If the balance was in an unsafe bounds and it was reset to 0 <br> 
	 * 	       SUCCESS - The deposit was a success
	 */
	public CurrencyExchangeResult deposit(double amt)
	{
		if(amt < 0)
			return CurrencyExchangeResult.NOT_ENOUGH_FUNDS;
		
		balance += amt;
		
		return checkBalance();
	}
	
	/**
	 * Checks if the balance is not infinity, NaN, or negative; If it is it is then reset to 0
	 * @return BALANCE_RESET if it was reset, or SUCCESS if no changes were needed
	 */
	private CurrencyExchangeResult checkBalance()
	{
		if(Double.isInfinite(balance) || Double.isNaN(balance) || balance < 0)
		{
			balance = 0;
			return CurrencyExchangeResult.BALANCE_RESET;
		}
		return CurrencyExchangeResult.SUCCESS;
	}
	
	/**
	 * Sets a guild member's rank to a specified GuildRank<br>
	 * The member specified must be currently in the guild<br>
	 * <strong>Note: </strong> You must save the guild to keep these changes.
	 * @param uuid The member to set the rank of
	 * @param guildRank The rank to set the member to
	 * @return true if the member was in the guild, false if the member was not.
	 */
	public boolean setMemberRank(UUID uuid, GuildRank guildRank) 
	{
		if(getMembersFull().containsKey(uuid))
		{
			getMembersFull().put(uuid, guildRank);
			return true;
		}
		return false;
	}
	
	/**
	 * Gets chunks around a specified chunk that the guild owns
	 * @param c The chunk to search around
	 * @return A list in the order of: East chunk, West chunk, South chunk, North chunk
	 */
	public List<Chunk> getChunksAround(Chunk c)
	{
		List<Chunk> chunksAround = new LinkedList<>();
		
		World w = c.getWorld();
		
		// East
		{
			Chunk check = w.getChunkAt(c.getX() + 1, c.getZ());
			
			if(getOwnedChunks().contains(check))
				chunksAround.add(check);
		}
		
		// West
		{
			Chunk check = w.getChunkAt(c.getX() - 1, c.getZ());
			
			if(getOwnedChunks().contains(check))
				chunksAround.add(check);
		}
		
		// South
		{
			Chunk check = w.getChunkAt(c.getX(), c.getZ() + 1);
			
			if(getOwnedChunks().contains(check))
				chunksAround.add(check);
		}
		
		// North
		{
			Chunk check = w.getChunkAt(c.getX(), c.getZ() - 1);
			
			if(getOwnedChunks().contains(check))
				chunksAround.add(check);
		}
		
		return chunksAround;
	}
	
	// Getters & Setters //
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public Set<UUID> getMembers() 
	{
		return members.keySet();
	}
	public Map<UUID, GuildRank> getMembersFull() { return this.members; }
	public void setMembers(Map<UUID, GuildRank> members) { this.members = members; }
	
	public List<Chunk> getOwnedChunks() { return ownedChunks; }
	public void setOwnedChunks(List<Chunk> ownedChunks) { this.ownedChunks = ownedChunks; }

	public Location getHome() { return home; }
	public void setHome(Location home) { this.home = home; }
	
	public double getBalance() { return balance; }
	public void setBalance(double balance) { this.balance = balance; }

	// For Debug
	public void showBorders() 
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
