package com.cornchipss.custombosses.boss;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

import com.cornchipss.custombosses.util.json.Serializer;

public class LivingBoss
{	
	LivingEntity entity;
	Boss boss;
	
	public LivingBoss(Boss b)
	{
		this.boss = b;
	}
	
	public LivingBoss(Boss b, Location loc)
	{
		this.boss = b;
		spawn(loc);
	}
	
	public void spawn(Location loc)
	{
		World w = loc.getWorld();
		entity = (LivingEntity) w.spawnEntity(loc, boss.getEntityType());
		
		entity.setCustomName(boss.getDisplayName());
		entity.setMaxHealth(boss.getStartingHealth());
		entity.setHealth(boss.getStartingHealth());
		EntityEquipment equipment = entity.getEquipment();
		equipment.setHelmet(boss.getArmor(0));
		equipment.setChestplate(boss.getArmor(1));
		equipment.setLeggings(boss.getArmor(2));
		equipment.setBoots(boss.getArmor(3));
		equipment.setItemInHand(boss.getHandEquipment());
	}
	
	public Map<Integer, String> serialize()
	{
		Map<Integer, String> serialized = new HashMap<>();
		serialized.put(getBoss().getId(), Serializer.serializeLocation(getEntity().getLocation()));
		return serialized;
	}
	
	public static LivingBoss deserialize(List<Boss> loadedBosses, Map<Integer, String> serializedData)
	{
		int[] keySet = (int[]) serializedData.keySet().toArray()[0];
		for(Boss b : loadedBosses)
		{
			if(b.getId() == keySet[0])
			{
				return new LivingBoss(b, Serializer.deserializeLocation(serializedData.get(keySet[0])));
			}
		}
		return null;
	}
	
	public LivingEntity getEntity() { return this.entity; }
	public void setEntity(LivingEntity ent) { this.entity = ent; }
	
	public Boss getBoss() { return this.boss; }
}
