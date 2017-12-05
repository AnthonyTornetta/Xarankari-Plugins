package com.cornchipss.buildbattlesmini;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

import com.cornchipss.buildbattlesmini.arenas.Arena;

public class Begin implements Runnable
{
	String theme;
	int durationSeconds;
	ArrayList<Arena> arenas;
	Timer t;
	BuildBattleMini bb;
	
	public Begin(BuildBattleMini bb, Timer t, String theme, ArrayList<Arena> arenas, int durationSeconds)
	{
		this.theme = theme;
		this.durationSeconds = durationSeconds;
		this.arenas = arenas;
		this.t = t;
		this.bb = bb;
	}
	
	@Override
	public void run()
	{
		for(Arena a: arenas)
		{
			a.broadcast(ChatColor.GREEN + "BUILD!", "Theme: " + theme, 20, 40, 20);
			int min = durationSeconds / 60, sec = durationSeconds % 60; // So it looks nicer :)
			
			String minStr = "minutes";
			String secStr = "seconds";
			if(min == 1)
				minStr = "minute";
			if(sec == 1)
				secStr = "second";
			
			a.sendMessage(ChatColor.GREEN + "You have " + min + " " + minStr + " and " + sec + " " + secStr + " to build!");
			a.playSound(Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
		}
		t = new Timer(bb, durationSeconds);
		t.run();
	}
}
