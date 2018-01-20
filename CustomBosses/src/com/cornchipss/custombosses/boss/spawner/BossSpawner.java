package com.cornchipss.custombosses.boss.spawner;

import org.bukkit.Bukkit;

import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.boss.handler.BossHandler;
import com.cornchipss.custombosses.listener.events.BossSpawnEvent;

public class BossSpawner implements Runnable
{
	private BossHandler handler;
	
	public BossSpawner(BossHandler handler)
	{
		this.handler = handler;
	}
	
	@Override
	public void run() 
	{
		for(BossSpawnArea spawnArea : handler.getSpawnAreas())
		{
			for(LivingBoss toSpawn : spawnArea.run())
			{
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
