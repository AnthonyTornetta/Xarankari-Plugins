package com.cornchipss.custombosses.boss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.custombosses.util.Vector2;

public class Boss 
{
	String displayName;
	private int startingHealth;
	Map<ItemStack, Vector2<Integer, Integer>> drops;
	EntityType entityType;
	ItemStack handEquipment;
	ItemStack[] armor = new ItemStack[4];
	LivingEntity spawnedBoss = null;
	
	public Boss(int startingHealth, EntityType entityType, String displayName, ItemStack handEquipment, ItemStack[] armor, Map<ItemStack, Vector2<Integer, Integer>> drops) 
	{
		this.displayName = displayName;
		this.startingHealth = startingHealth;
		this.entityType = entityType;
		this.handEquipment = handEquipment;
		this.armor = armor;
		this.drops = drops;
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

	public Map<ItemStack, Vector2<Integer, Integer>> getDrops() { return drops; }

	public List<ItemStack> getDropItems() 
	{
		List<ItemStack> items = new ArrayList<>();
		for(ItemStack i : drops.keySet())
			items.add(i);
		return items;
	}
}
