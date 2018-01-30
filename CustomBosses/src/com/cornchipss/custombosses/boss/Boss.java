package com.cornchipss.custombosses.boss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.custombosses.util.Helper;
import com.cornchipss.custombosses.util.Vector2;

public class Boss
{
	private String displayName;
	private int startingHealth;
	private Map<ItemStack, Vector2<Integer, Integer>> drops;
	private EntityType entityType;
	private ItemStack handEquipment;
	private ItemStack[] armor = new ItemStack[4];
	private ItemStack spawnItem;
	int damagePerHit = 0;
	private int bossId;
	private int spawnChance;
	int xpDropped;
	String deathCommand;
	String deathMessage;
	
	public Boss(int startingHealth, EntityType entityType, String displayName, ItemStack handEquipment, ItemStack[] armor, Map<ItemStack, Vector2<Integer, Integer>> drops, 
				int damagePerHit, ItemStack spawnItem, int bossId, int spawnChance, int xpDropped, String deathCommand, String deathMessage) 
	{
		this.displayName = displayName;
		this.startingHealth = startingHealth;
		this.entityType = entityType;
		this.handEquipment = handEquipment;
		this.armor = armor;
		this.drops = drops;
		this.damagePerHit = damagePerHit;
		this.spawnItem = spawnItem;
		this.bossId = bossId;
		this.spawnChance = spawnChance;
		this.xpDropped = xpDropped;
		this.deathCommand = deathCommand;
		this.deathMessage = deathMessage;
	}
	
	public LivingBoss createLivingBoss()
	{
		return new LivingBoss(this);
	}
	
	public LivingBoss createLivingBoss(Location loc)
	{
		return new LivingBoss(this, loc);
	}
	
	@Override
	public String toString()
	{
		return "Boss [" + getDisplayName() + "; " + getEntityType() + "; " + getStartingHealth() + "; " + getHandEquipment() + "; " + getArmor(0) + ", " + getArmor(1) + ", " + getArmor(2) + ", " + getArmor(3) + "; " + getDropItems() + "; " + getDamagePerHit() + "; " + getSpawnItem() + "]";
	}
	
	public int getStartingHealth() { return startingHealth; }
	public void setStartingHealth(int startingHealth) { this.startingHealth = startingHealth; }

	public EntityType getEntityType() { return entityType; }
	public void setEntityType(EntityType entityType) { this.entityType = entityType; }

	public String getDisplayName() { return displayName; }
	public void setDisplayName(String displayName) { this.displayName = displayName; }

	public ItemStack getHandEquipment() { return handEquipment; }
	public void setHandEquipment(ItemStack handEquipment) { this.handEquipment = handEquipment; }

	public ItemStack getArmor(int i) { return getArmor()[i]; }
	public void setArmor(int i, ItemStack piece) { this.armor[i] = piece; }
	public ItemStack[] getArmor() { return this.armor; }

	public Map<ItemStack, Vector2<Integer, Integer>> getDrops() { return drops; }

	public List<ItemStack> getDropItems() 
	{
		List<ItemStack> drops = new ArrayList<>();
		
		Set<ItemStack> dropsList = getDrops().keySet();
		for(ItemStack temp : dropsList)
		{
			ItemStack i = temp.clone();
			
			Vector2<Integer, Integer> range = this.getDrops().get(i);
			int amt = Helper.iRandomRange(range.getX(), range.getY());
			if(amt <= 0)
				continue;
			i.setAmount(amt);
			
			drops.add(i);
		}
		
		return drops;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Boss)
		{
			Boss b = (Boss)obj;
			return b.getId() == getId();
		}
		return false;
	}
	
	public int getDamagePerHit() { return damagePerHit; }
	public void setDamagePerHit(int dmg) { this.damagePerHit = dmg; }

	public ItemStack getSpawnItem() { return spawnItem; }
	public void setSpawnItem(ItemStack i) { this.spawnItem = i; }
	
	public int getId() { return bossId; }

	public int getSpawnChance() { return spawnChance; }
	public void setSpawnChance(int chance) { this.spawnChance = chance; }

	public int getXpDropped() { return xpDropped; }
	public void setXpDropped(int xpDropped) { this.xpDropped = xpDropped; }

	public String getDeathCommand() { return deathCommand; }
	public void setDeathCommand(String deathCommand) { this.deathCommand = deathCommand; }

	public String getDeathMessage() { return deathMessage; }
	public void setDeathMessage(String deathMessage) { this.deathMessage = deathMessage; }	
}
