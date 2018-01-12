package com.cornchipss.custombosses.boss.json;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.json.equipment.ArmorJson;
import com.cornchipss.custombosses.boss.json.equipment.BossEquipmentJson;
import com.cornchipss.custombosses.boss.json.equipment.HandJson;

public class JsonBoss 
{
	private int startHealth;
	private String displayName, mobType;
	private BossEquipmentJson equipment;
	
	public JsonBoss(int startHealth, String displayName, String mobType, BossEquipmentJson equipment) 
	{
		this.startHealth = startHealth;
		this.displayName = displayName;
		this.mobType = mobType;
		this.equipment = equipment;
	}
	
	public Boss createBoss()
	{
		
		//int startingHealth, EntityType entityType, String displayName, ItemStack handEquipment, ItemStack[] armor
		List<ArmorJson> armorJson = equipment.getArmor();		
		ItemStack[] armor = new ItemStack[4];		
		for(int i = 0; i < armorJson.size(); i++)
		{
			ArmorJson json = armorJson.get(0);
			armor[i] = new ItemStack(Material.valueOf(json.getMaterial()));
			for(String s : json.getEnchants().keySet())
			{
				armor[i].addUnsafeEnchantment(Enchantment.getByName(s), json.getEnchants().get(s));
			}
		}
		
		HandJson handJson = equipment.getHand();
		ItemStack hand;
		hand = new ItemStack(Material.valueOf(handJson.getMaterial()));
		for(String s : handJson.getEnchants().keySet())
		{
			hand.addUnsafeEnchantment(Enchantment.getByName(s), handJson.getEnchants().get(s));
		}
		
		return new Boss(startHealth, EntityType.valueOf(mobType), displayName, hand, armor);
	}

	public int getStartHealth() { return startHealth; }
	public void setStartHealth(int startHealth) { this.startHealth = startHealth; }

	public String getDisplayName() { return displayName; }
	public void setDisplayName(String displayName) { this.displayName = displayName; }

	public String getMobType() { return mobType; }
	public void setMobType(String mobType) { this.mobType = mobType; }

	public BossEquipmentJson getEquipment() { return equipment; }
	public void setEquipment(BossEquipmentJson equipment) { this.equipment = equipment; }
}
