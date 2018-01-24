package com.cornchipss.custombosses.listener.events;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import com.cornchipss.custombosses.boss.LivingBoss;

public class BossDeathEvent extends EntityDeathEvent
{
	private LivingBoss b;
	private Player bossKiller;
	
	public BossDeathEvent(LivingBoss b, Player killer)
	{
		// TODO: Custom xp
		super(b.getEntity(), b.getBoss().getDropItems(), 100);
		this.b = b;
		this.bossKiller = killer;
	}
	
	public LivingBoss getLivingBoss () { return b; }
	public Player getKiller() { return bossKiller; }
}
