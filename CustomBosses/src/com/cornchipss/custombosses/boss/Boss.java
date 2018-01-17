package com.cornchipss.custombosses.boss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.custombosses.util.Vector2;

public class Boss 
{
	private String displayName;
	private int startingHealth;
	private Map<ItemStack, Vector2<Integer, Integer>> drops;
	private EntityType entityType;
	private ItemStack handEquipment;
	private ItemStack[] armor = new ItemStack[4];
	private int price = -1;
	private ItemStack spawnItem;
	int damagePerHit = 0;
	private int bossId;
	private int spawnChance;
	
	public Boss(int startingHealth, EntityType entityType, String displayName, ItemStack handEquipment, ItemStack[] armor, Map<ItemStack, Vector2<Integer, Integer>> drops, 
				int damagePerHit, int price, ItemStack spawnItem, int bossId, int spawnChance) 
	{
		this.displayName = displayName;
		this.startingHealth = startingHealth;
		this.entityType = entityType;
		this.handEquipment = handEquipment;
		this.armor = armor;
		this.drops = drops;
		this.damagePerHit = damagePerHit;
		this.price = price;
		this.spawnItem = spawnItem;
		this.bossId = bossId;
		this.spawnChance = spawnChance;
	}
	
	public LivingBoss createLivingBoss()
	{
		return new LivingBoss(this);
	}
	
	@Override
	public String toString()
	{
		return "Boss [" + getDisplayName() + "; " + getEntityType() + "; " + getStartingHealth() + "; " + getHandEquipment() + "; " + getArmor(0) + ", " + getArmor(1) + ", " + getArmor(2) + ", " + getArmor(3) + "; " + getDropItems() + "; " + getDamagePerHit() + "; " + getPrice() + "; " + getSpawnItem() + "]";
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

	public int getDamagePerHit() { return damagePerHit; }
	public void setDamagePerHit(int dmg) { this.damagePerHit = dmg; }

	public int getPrice() { return price; }
	public void setPrice(int p) { this.price = p; }

	public ItemStack getSpawnItem() { return spawnItem; }
	public void setSpawnItem(ItemStack i) { this.spawnItem = i; }
	
	public int getId() { return bossId; }

	public int getSpawnChance() { return spawnChance; }
	public void setSpawnChance(int chance) { this.spawnChance = chance; }
}
