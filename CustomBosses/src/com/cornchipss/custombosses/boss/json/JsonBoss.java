package com.cornchipss.custombosses.boss.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.custombosses.Debug;
import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.json.equipment.ArmorJson;
import com.cornchipss.custombosses.boss.json.equipment.BossEquipmentJson;
import com.cornchipss.custombosses.boss.json.equipment.HandJson;
import com.cornchipss.custombosses.util.Vector2;

import net.md_5.bungee.api.ChatColor;

public class JsonBoss extends Debug
{
	private int startHealth;
	private String displayName, mobType;
	private BossEquipmentJson equipment;
	private Map<String, String> drops;
	private int damagePerHit;
	private int price;
	private String spawnItem;
	private int bossId;
	private int spawnChance;
	
	public JsonBoss(int startHealth, String displayName, String mobType, BossEquipmentJson equipment, Map<String, String> drops, int damagePerHit, int price, String spawnItem, int bossId, int spawnChance) 
	{
		this.startHealth = startHealth;
		this.displayName = displayName;
		this.mobType = mobType;
		this.equipment = equipment;
		this.drops = drops;
		this.damagePerHit = damagePerHit;
		this.price = price;
		this.spawnItem = spawnItem;
		this.bossId = bossId;
		this.spawnChance = spawnChance;
	}
	
	public Boss createBoss()
	{
		// Take the armor from a String to actual armor, then add enchants
		List<ArmorJson> armorJson = equipment.getArmor();		
		ItemStack[] armor = new ItemStack[4];
		
		for(int i = 0; i < armorJson.size(); i++)
		{
			ArmorJson json = armorJson.get(i);
			armor[i] = new ItemStack(Material.valueOf(json.getMaterial()));
			for(String s : json.getEnchants().keySet())
			{
				armor[i].addUnsafeEnchantment(Enchantment.getByName(s), json.getEnchants().get(s));
			}
		}
		
		// Create the item held in the hand from the material name and add enchants
		HandJson handJson = equipment.getHand();
		ItemStack hand = new ItemStack(Material.valueOf(handJson.getMaterial()), 1);
		for(String s : handJson.getEnchants().keySet())
		{
			hand.addUnsafeEnchantment(Enchantment.getByName(s), handJson.getEnchants().get(s));
		}
		
		// Create the drops it will have
		Map<ItemStack, Vector2<Integer, Integer>> dropsComplete = new HashMap<>();
				
		for(String itemName : drops.keySet())
		{
			ItemStack item = new ItemStack(Material.valueOf(itemName));
			String[] split = drops.get(itemName).replaceAll(" ", "").split("-");
			Vector2<Integer, Integer> dropRange;
			if(split.length == 1)
			{
				int val = Integer.parseInt(split[0]);
				dropRange = new Vector2<>(val, val);
			}
			else
			{
				String[] splitAgain = split[1].split(";");
				dropRange = new Vector2<>(Integer.parseInt(split[0]), Integer.parseInt(splitAgain[0]));
				if(splitAgain.length > 1)
				{
					String[] enchants = splitAgain[1].split(",");
					for(int i = 0; i < enchants.length; i++)
					{
						String[] enchantAndAmount = enchants[i].split("#");
						item.addUnsafeEnchantment(Enchantment.getByName(enchantAndAmount[0].toUpperCase()), Integer.parseInt(enchantAndAmount[1]));
					}
				}
			}			
			dropsComplete.put(item, dropRange);
		}
		
		ItemStack spawnItem = new ItemStack(Material.valueOf(this.spawnItem));
		ItemMeta spawnMeta = spawnItem.getItemMeta();
		spawnMeta.setDisplayName(ChatColor.GOLD + "Spawn " + displayName);
		List<String> spawnLore = new ArrayList<>();
		spawnLore.add("Boss Spawn Item [" + bossId + "]");
		spawnMeta.setLore(spawnLore);
		spawnItem.setItemMeta(spawnMeta);
		
		return new Boss(startHealth, EntityType.valueOf(mobType), displayName, hand, armor, dropsComplete, damagePerHit, price, spawnItem, bossId, spawnChance);
	}
	
	public static JsonBoss fromBoss(Boss b)
	{
		int startHealth = b.getStartingHealth();
		String displayName = b.getDisplayName();
		String mobType = b.getEntityType().name();
		
		ItemStack hand = b.getHandEquipment();
		Map<Enchantment, Integer> enchants = hand.getEnchantments();
		Map<String, Integer> enchantsJson = new HashMap<>(); 
		for(Enchantment k : enchants.keySet())
		{
			enchantsJson.put(k.getName(), enchants.get(k));
		}
		HandJson hjson = new HandJson(hand.getType().name(), hand.getItemMeta().getDisplayName(), hand.getItemMeta().getLore(), enchantsJson, new ArrayList<String>());
		
		List<ArmorJson> ajson = new ArrayList<>();
		for(int i = 0; i < 4; i++)
		{
			ItemStack piece = b.getArmor(i);
			Map<Enchantment, Integer> armorEnchants = piece.getEnchantments();
			Map<String, Integer> armorEnchantsJson = new HashMap<>(); 
			for(Enchantment k : armorEnchants.keySet())
			{
				armorEnchantsJson.put(k.getName(), armorEnchants.get(k));
			}
			ajson.add(new ArmorJson(piece.getType().name(), piece.getItemMeta().getDisplayName(), piece.getItemMeta().getLore(), armorEnchantsJson, new ArrayList<>()));
		}
		
		BossEquipmentJson ejson = new BossEquipmentJson(ajson, hjson);
		
		Map<String, String> drops = new HashMap<>();
		Map<ItemStack, Vector2<Integer, Integer>> bossDrops = b.getDrops();
		
		for(ItemStack is : bossDrops.keySet())
		{
			Vector2<Integer, Integer> range = bossDrops.get(is);
			String matName = is.getType().name();
			
			String amount = range.getX() + "";
			if(range.getX() != range.getY())
				amount += "-" + range.getY();
			
			Map<Enchantment, Integer> itemEnchants = is.getEnchantments();
			Set<Enchantment> keySet = itemEnchants.keySet();
			if(keySet.size() == 0)
				drops.put(matName, amount);
			else
			{
				String enchantsStr = "";
				int i = 0;
				for(Enchantment en : keySet)
				{
					enchantsStr += en.getName() + "#" + itemEnchants.get(en);
					if(i + 1 != keySet.size())
					{
						enchantsStr += ",";
					}
					i++;
				}
				
				drops.put(matName, amount + ";" + enchantsStr);
			}
		}
		
		JsonBoss jsonBoss = new JsonBoss(startHealth, displayName, mobType, ejson, drops, b.getDamagePerHit(), b.getPrice(), b.getSpawnItem().getType().name(), b.getId(), b.getSpawnChance());
		
		return jsonBoss;
	}
	
	public static List<JsonBoss> fromBossList(List<Boss> bosses) 
	{
		List<JsonBoss> jsonBosses = new ArrayList<>();
		for(Boss b : bosses)
			jsonBosses.add(fromBoss(b));
		return jsonBosses;
	}
	
	@Override
	public String toString()
	{
		return "JsonBoss [" + getDisplayName() + ":" + getStartHealth() + "; " + getEquipment() + "; Mob Type: " + getMobType() + "; " + getDrops() + "]";
	}
	
	public int getStartHealth() { return startHealth; }
	public void setStartHealth(int startHealth) { this.startHealth = startHealth; }

	public String getDisplayName() { return displayName; }
	public void setDisplayName(String displayName) { this.displayName = displayName; }

	public String getMobType() { return mobType; }
	public void setMobType(String mobType) { this.mobType = mobType; }

	public BossEquipmentJson getEquipment() { return equipment; }
	public void setEquipment(BossEquipmentJson equipment) { this.equipment = equipment; }
	
	public Map<String, String> getDrops() { return drops; }
	public void setDrops(Map<String, String> drops) { this.drops = drops; }
	
	public int getSpawnChance() { return this.spawnChance; }
	public void setSpawnChance(int chance) { this.spawnChance = chance; }
}
