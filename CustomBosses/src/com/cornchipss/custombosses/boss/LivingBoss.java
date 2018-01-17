package com.cornchipss.custombosses.boss;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

public class LivingBoss
{	
	LivingEntity entity;
	Boss boss;
	
	public LivingBoss(Boss b)
	{
		this.boss = b;
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
	
	public LivingEntity getEntity() { return this.entity; }
	public void setEntity(LivingEntity ent) { this.entity = ent; }
	
	public Boss getBoss() { return this.boss; }
}
