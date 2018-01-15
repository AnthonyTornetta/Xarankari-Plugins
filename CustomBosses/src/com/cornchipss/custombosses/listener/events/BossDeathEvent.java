package com.cornchipss.custombosses.listener.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;

import com.cornchipss.custombosses.boss.Boss;

public class BossDeathEvent extends EntityDeathEvent
{
	private Boss b;
	
	public BossDeathEvent(LivingEntity e, Boss b)
	{
		// TODO: Custom xp
		super(e, b.getDropItems(), 100);
		this.b = b;
	}
	
	public Boss getBoss () { return b; }
}
