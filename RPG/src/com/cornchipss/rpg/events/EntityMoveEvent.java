package com.cornchipss.rpg.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * TODO Do this
 */
public class EntityMoveEvent extends EntityEvent
{
	// idk a way to detect entity movement w/out causing major server lag
	// Thankfully, That's what spigot forums were made for :D
	// ok nope we're using barriers
	private static final HandlerList HANDLERS = new HandlerList();
	private Location previous, now;
	boolean isCancelled = false;
	
	public EntityMoveEvent(Entity what, Location previous, Location now)
	{
		super(what);
		this.previous = previous;
		this.now = now;
	}
	
	public boolean isCancelled() 
	{
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) 
    {
        this.isCancelled = isCancelled;
    }

	public Location getPrevious() 
	{
		return previous;
	}
	public void setPrevious(Location previous) 
	{
		this.previous = previous;
	}
	public Location getNow() {
		return now;
	}
	public void setNow(Location now) 
	{
		this.now = now;
	}
	
	
	
	@Override
    public HandlerList getHandlers() 
	{
        return HANDLERS;
    }

    public static HandlerList getHandlerList() 
    {
        return HANDLERS;
    }
}