package com.cornchipss.custombosses.boss.spawner;

import org.bukkit.Bukkit;

import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.boss.handler.BossHandler;
import com.cornchipss.custombosses.listener.events.BossSpawnEvent;

public class BossSpawner implements Runnable
{
	private final BossHandler handler;
	
	public BossSpawner(final BossHandler handler)
	{
		this.handler = handler;
	}
	
	@Override
	public void run() 
	{
		System.out.println("RANDED");
		for(BossSpawnArea spawnArea : handler.getSpawnAreas())
		{
			System.out.println("SPAWN AREAD");
			for(LivingBoss toSpawn : spawnArea.run())
			{
				System.out.println("ATTEMPTING");
				BossSpawnEvent bse = new BossSpawnEvent(toSpawn);
				Bukkit.getPluginManager().callEvent(bse);
				if(bse.isCancelled())
					continue;
				toSpawn.spawn();
				handler.addLivingBoss(toSpawn);
			}
		}
	}
}
