package com.cornchipss.custombosses.listener.events;

import org.bukkit.event.entity.EntityDeathEvent;

import com.cornchipss.custombosses.boss.LivingBoss;

public class BossDeathEvent extends EntityDeathEvent
{
	private LivingBoss b;
	
	public BossDeathEvent(LivingBoss b)
	{
		// TODO: Custom xp
		super(b.getEntity(), b.getBoss().getDropItems(), 100);
		this.b = b;
	}
	
	public LivingBoss getLivingBoss () { return b; }
}
