package com.cornchipss.custombosses.boss;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class Boss 
{
	String displayName;
	private int startingHealth;
	EntityType entityType;
	ItemStack handEquipment;
	ItemStack[] armor = new ItemStack[4];
	LivingEntity spawnedBoss = null;
	
	public Boss(int startingHealth, EntityType entityType, String displayName, ItemStack handEquipment, ItemStack[] armor) 
	{
		this.displayName = displayName;
		this.startingHealth = startingHealth;
		this.entityType = entityType;
		this.handEquipment = handEquipment;
		this.armor = armor;
	}
	
	public Entity spawn(Location loc)
	{
		World w = loc.getWorld();
		spawnedBoss = (LivingEntity) w.spawnEntity(loc, getEntityType());
		
		spawnedBoss.setCustomName(displayName);
		EntityEquipment equipment = spawnedBoss.getEquipment();
		equipment.setHelmet(getArmor(0));
		equipment.setChestplate(getArmor(1));
		equipment.setLeggings(getArmor(2));
		equipment.setBoots(getArmor(3));
		equipment.setItemInHand(getHandEquipment());

		return spawnedBoss;
	}
	
	public int getStartingHealth() { return startingHealth; }
	public void setStartingHealth(int startingHealth) { this.startingHealth = startingHealth; }

	public EntityType getEntityType() { return entityType; }
	public void setEntityType(EntityType entityType) { this.entityType = entityType; }

	public String getDisplayName() { return displayName; }
	public void setDisplayName(String displayName) { this.displayName = displayName; }

	public ItemStack getHandEquipment() { return handEquipment; }
	public void setHandEquipment(ItemStack handEquipment) { this.handEquipment = handEquipment; }

	public ItemStack getArmor(int i) { return armor[i]; }
	public void setArmor(int i, ItemStack piece) { this.armor[i] = piece; }
}
