package com.cornchipss.custombosses.boss;

import com.cornchipss.custombosses.boss.handler.BossHandler;

public class BossExpirer implements Runnable 
{
	private BossHandler handler;
	
	public BossExpirer(BossHandler handler)
	{
		this.handler = handler;
	}
	
	@Override
	public void run() 
	{
		for(LivingBoss b : handler.getLivingBosses())
		{
			b.increaseTimeBetweenHits();
			if(b.getTimeSinceLastHit() > 500)
			{
				b.remove();
				handler.removeLivingBoss(b);
			}
		}
	}

}
