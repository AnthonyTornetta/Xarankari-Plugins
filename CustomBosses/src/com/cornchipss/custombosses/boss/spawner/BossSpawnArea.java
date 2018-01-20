package com.cornchipss.custombosses.boss.spawner;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.util.Helper;
import com.cornchipss.custombosses.util.Reference;
import com.cornchipss.custombosses.util.Vector2;

public class BossSpawnArea 
{
	Vector2<Location, Location> area;
	List<Boss> spawnableBosses;
	
	public BossSpawnArea(Vector2<Location, Location> area, List<Boss> spawnableBosses)
	{
		this.area = area;
		this.spawnableBosses = spawnableBosses;
	}
	
	public List<LivingBoss> run()
	{
		List<LivingBoss> spawned = new ArrayList<>();

		for(Boss b : spawnableBosses)
		{
			if(b.getSpawnChance() <= 0 || b.getSpawnChance() > 10000)
				continue;
			
			System.out.println("went rhru");
			
			if(Helper.iRandomRange(0, 10000 - b.getSpawnChance()) == b.getSpawnChance() - 1)
			{
				double diameterX = area.getX().getX() - area.getY().getX();
				double diameterZ = area.getY().getZ() - area.getY().getZ();
				
				double offsetX = Helper.dRandomRange(0, diameterX);
				double offsetZ = Helper.dRandomRange(0, diameterZ);
				
				double origY = Helper.iRandomRange(0, 255);
				
				Location firstLoc = area.getX();
				double x = firstLoc.getX() + offsetX;
				double z = firstLoc.getZ() + offsetZ;
				
				Location spawnPosition = null;
				
				for(double y = origY; y <= 255; y++)
				{
					Location testLocation = new Location(firstLoc.getWorld(), x, y, z);
					if(testLocation.getBlock().getType() == Material.AIR && testLocation.add(0, 1, 0).getBlock().getType() == Material.AIR)
					{
						spawnPosition = testLocation;
						break;
					}
				}
				
				if(spawnPosition == null)
				{
					for(double y = origY; y >= 0; y--)
					{
						Location testLocation = new Location(firstLoc.getWorld(), x, y, z);
						if(testLocation.getBlock().getType() == Material.AIR && testLocation.add(0, 1, 0).getBlock().getType() == Material.AIR)
						{
							spawnPosition = testLocation;
							break;
						}
					}
				}
				
				if(spawnPosition == null)
					continue; // Just give up.
				
				spawned.add(b.createLivingBoss(spawnPosition));
			}
		}
		
		return spawned;
	}

	public List<Boss> getBosses() 
	{
		return Reference.cloneBosses(spawnableBosses);
	}

	public Location getLocationX() 
	{
		return area.getX().clone();
	}
	
	public Location getLocationY()
	{
		return area.getY().clone();
	}
	
	public BossSpawnArea clone()
	{
		return new BossSpawnArea(area.clone(), Reference.cloneBosses(spawnableBosses));
	}
}
