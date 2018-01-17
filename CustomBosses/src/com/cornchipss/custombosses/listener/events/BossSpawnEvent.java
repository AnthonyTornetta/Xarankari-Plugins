package com.cornchipss.custombosses.listener.events;

import org.bukkit.event.entity.EntitySpawnEvent;

import com.cornchipss.custombosses.boss.LivingBoss;

public class BossSpawnEvent extends EntitySpawnEvent
{
	private LivingBoss b;
	
	public BossSpawnEvent(LivingBoss b) 
	{
		super(b.getEntity());
		this.b = b;
	}

	public LivingBoss getLivingBoss() { return b; }
	public void setLivingBoss(LivingBoss b) { this.b = b; }
}
