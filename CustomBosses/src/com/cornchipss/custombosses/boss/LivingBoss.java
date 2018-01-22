package com.cornchipss.custombosses.boss;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

import com.cornchipss.custombosses.listener.events.BossDeathEvent;

public class LivingBoss
{	
	private LivingEntity entity = null;
	private Location spawnHereLocation = null;
	private Boss boss;
	private int timeSinceLastHit = 0;
	
	public LivingBoss(Boss b)
	{
		this.boss = b;
	}
	
	public LivingBoss(Boss b, Location loc)
	{
		this.boss = b;
		this.spawnHereLocation = loc;
	}
	
	public LivingBoss(Boss b, Entity e)
	{
		this.boss = b;
		this.entity = (LivingEntity)e;
	}
	
	public void spawn(Location loc)
	{
		World w = loc.getWorld();
		entity = (LivingEntity) w.spawnEntity(loc, boss.getEntityType());
		entity.setRemoveWhenFarAway(true);
		
		entity.setCustomName(boss.getDisplayName());
		AttributeInstance healthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		healthAttribute.setBaseValue(this.getBoss().getStartingHealth());
		entity.setHealth(boss.getStartingHealth());
		
		EntityEquipment equipment = entity.getEquipment();		
		equipment.setHelmet(boss.getArmor(0));
		equipment.setChestplate(boss.getArmor(1));
		equipment.setLeggings(boss.getArmor(2));
		equipment.setBoots(boss.getArmor(3));
		equipment.setItemInMainHand(boss.getHandEquipment());
		
		entity.getEquipment().setBootsDropChance(0);
		entity.getEquipment().setLeggingsDropChance(0);
		entity.getEquipment().setChestplateDropChance(0);
		entity.getEquipment().setHelmetDropChance(0);
		entity.getEquipment().setItemInOffHandDropChance(0);
	}
	
	public void spawn()
	{
		spawn(spawnHereLocation);
	}
	
	public Map<Integer, String> serialize()
	{
		Map<Integer, String> serialized = new HashMap<>();
		if(getEntity() == null)
			return null;
		
		serialized.put(getBoss().getId(), getEntity().getUniqueId() + ";" + getEntity().getWorld().getName());
		return serialized;
	}
	
	public static LivingBoss deserialize(List<Boss> loadedBosses, Map<Integer, String> serializedData)
	{
		for(int i : serializedData.keySet())
		{
			for(Boss b : loadedBosses)
			{
				if(b.getId() == i)
				{
					String[] split = serializedData.get(i).split(";");
					UUID entId = UUID.fromString(split[0]);
					Entity ent = null;
					for(Entity e : Bukkit.getWorld(split[1]).getEntities())
					{
						if(e.getUniqueId().equals(entId))
						{
							ent = e;
							break;
						}
					}
					return new LivingBoss(b, ent);
				}
			}
		}		
		return null;
	}
	
	public void remove() 
	{
		BossDeathEvent e = new BossDeathEvent(this);
		Bukkit.getPluginManager().callEvent(e);
		this.getEntity().remove();
	}
	
	@Override
	public String toString()
	{
		return this.getBoss() + "; " + this.getEntity();
	}
	
	public LivingEntity getEntity() { return this.entity; }
	public void setEntity(LivingEntity ent) { this.entity = ent; }
	
	public Boss getBoss() { return this.boss; }

	public void increaseTimeBetweenHits() { this.timeSinceLastHit++; }
	public void setTimeSinceLastHit(int time) { this.timeSinceLastHit = time; }
	public int getTimeSinceLastHit() { return this.timeSinceLastHit; }
}
