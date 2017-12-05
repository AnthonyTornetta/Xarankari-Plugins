package com.cornchipss.buildbattlesmini;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import com.cornchipss.buildbattlesmini.arenas.Arena;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Timer implements Runnable
{
	private int time = 0;
	private BuildBattleMini bb;
	private long startTime;
	
	public Timer(BuildBattleMini bb, int time)
	{
		this.time = time;
		this.bb = bb;
	}
	
	@Override
	public void run()
	{
		startTime = TimeUnit.SECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
		
		doitAGAIN();
	}
	
	public void doitAGAIN()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(bb, new Runnable()
		{
			@Override
			public void run()
			{
				int t = (int)((time + startTime - TimeUnit.SECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS)));
				
				int hr = t / 3600, min = t / 60 - hr, sec = t % 60; // So it looks nicer :)
				
				String hrStr = hr + "";
				String minStr = min + "";
				String secStr = sec + "";
				
				if(hr < 10)
					hrStr = "0" + hr;
				if(min < 10)
					minStr = "0" + min;
				if(sec < 10)
					secStr = "0" + sec;
				
				for(Arena a : bb.getArenas())
				{
					//p.sendMessage("asdf", ChatMessageType.ACTION_BAR);
					
					a.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + hrStr + ":" + minStr + ":" + secStr));
				}
				
				if(t == 60 * 60)
				{
					for(Arena a : bb.getArenas())
					{
						a.playSound(Sound.BLOCK_NOTE_SNARE, 1, 1);
						a.broadcast(ChatColor.RED + "1 Hour Remaining!", "", 20, 40, 20);
					}
				}
				
				if(t == 60 * 30)
				{
					for(Arena a : bb.getArenas())
					{
						a.playSound(Sound.BLOCK_NOTE_SNARE, 1, 1);
						a.broadcast(ChatColor.RED + "30 Minutes Remaining!", "", 20, 40, 20);
					}
				}
				
				if(t == 60 * 15)
				{
					for(Arena a : bb.getArenas())
					{
						a.playSound(Sound.BLOCK_NOTE_SNARE, 1, 1);
						a.broadcast(ChatColor.RED + "15 Minutes Remaining!", "", 20, 40, 20);
					}
				}
				
				if(t == 600)
				{
					for(Arena a : bb.getArenas())
					{
						a.playSound(Sound.BLOCK_NOTE_SNARE, 1, 1);
						a.broadcast(ChatColor.RED + "10 Minutes Remaining!", "", 20, 40, 20);
					}
				}
				
				if(t == 60 * 5)
				{
					for(Arena a : bb.getArenas())
					{
						a.playSound(Sound.BLOCK_NOTE_SNARE, 1, 1);
						a.broadcast(ChatColor.RED + "5 Minutes Remaining!", "", 20, 40, 20);
					}
				}
				
				if(t == 60)
				{
					for(Arena a : bb.getArenas())
					{
						a.playSound(Sound.BLOCK_NOTE_SNARE, 1, 1);
						a.broadcast(ChatColor.RED + "1 Minute Remaining!", "", 20, 40, 20);
					}
				}

				if(t == 30)
				{
					for(Arena a : bb.getArenas())
					{
						a.playSound(Sound.BLOCK_NOTE_SNARE, 1, 1);
						a.broadcast(ChatColor.RED + "30 Seconds Remaining!", "", 20, 40, 20);
					}
				}
				
				if(t == 10)
				{
					for(Arena a : bb.getArenas())
					{
						a.playSound(Sound.BLOCK_NOTE_SNARE, 1, 1);
						a.broadcast(ChatColor.RED + "10 Seconds Remaining!", "", 20, 40, 20);
					}
				}
				if(t < 10)
				{
					for(Arena a : bb.getArenas())
					{
						a.playSound(Sound.BLOCK_NOTE_SNARE, 1, 1);
					}
				}
				if(t <= 0)
				{
					bb.programEnd();
				}
				else
				{
					if(bb.isRunning())
						doitAGAIN();
					// If it isn't running it was forcibly ended by the user
				}
			}
		}, 20L);
	}
	
	public void extend(int time)
	{
		this.time += time;
		for(Arena a : bb.getArenas())
		{
			a.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			a.broadcast(ChatColor.GREEN + "Go! Go! Go!", ChatColor.GOLD + "Time extended by " + time + " seconds!", 20, 40, 20);
		}
	}
	
	public int getTime() { return time; }
}
