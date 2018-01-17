package com.cornchipss.custombosses.boss.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.util.Vector2;

public class BossHandler 
{
	private List<Boss> loadedBosses = new ArrayList<>();
	private List<LivingBoss> aliveBosses = new ArrayList<>();
	
	private Map<Boss, Vector2<Location, Location>> spawnBoundries;
	
	public BossHandler(List<Boss> loadedBosses) 
	{
		this.loadedBosses = loadedBosses;
		
		this.spawnBoundries = new HashMap<>();
	}
	
	public List<Boss> getLoadedBosses()
	{
		List<Boss> clone = new ArrayList<>();
		for(Boss b : loadedBosses)
			clone.add(b);
		return clone;
	}
	
	public List<LivingBoss> getLivingBosses()
	{
		List<LivingBoss> clone = new ArrayList<>();
		for(LivingBoss b : aliveBosses)
			clone.add(b);
		return clone;
	}
	
	public void addLivingBoss(LivingBoss b) { aliveBosses.add(b); }
	public void removeAliveBoss(LivingBoss b) { aliveBosses.remove(b); }
	
	public void setSpawnBoundries(Map<Boss, Vector2<Location, Location>> locs) { this.spawnBoundries = locs; }
	public Map<Boss, Vector2<Location, Location>> getSpawnLocations()
	{
		Map<Boss, Vector2<Location, Location>> locs = new HashMap<>();
		for(Boss b : spawnBoundries.keySet())
		{
			locs.put(b, spawnBoundries.get(b));
		}
		return locs;
	}
}
