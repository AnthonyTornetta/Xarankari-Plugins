package com.cornchipss.custombosses.listener.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.cornchipss.custombosses.boss.Boss;

public class BossSpawnEvent extends EntitySpawnEvent
{
	private Boss b;
	
	public BossSpawnEvent(Entity spawnee, Boss b) 
	{
		super(spawnee);
		this.b = b;
	}

	public Boss getB() { return b; }
	public void setB(Boss b) { this.b = b; }
}
