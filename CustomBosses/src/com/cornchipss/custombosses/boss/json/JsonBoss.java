package com.cornchipss.custombosses.boss.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cornchipss.custombosses.Debug;
import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.json.equipment.BossEquipmentJson;
import com.cornchipss.custombosses.boss.json.equipment.ItemStackJson;
import com.cornchipss.custombosses.boss.json.equipment.ItemStackJsonAmountRange;
import com.cornchipss.custombosses.util.Reference;
import com.cornchipss.custombosses.util.Vector2;
import com.cornchipss.custombosses.util.json.PluginJsonParser;

import net.md_5.bungee.api.ChatColor;

public class JsonBoss extends Debug
{
	private int bossId;
	private int startHealth;
	private String displayName, mobType;
	private int xpDropped;
	private String deathCommand;
	private BossEquipmentJson equipment;
	private List<ItemStackJsonAmountRange> dropsJson;
	private int damagePerHit;
	private String spawnItem;
	private int spawnChance;
	private String deathMessage;
	
	
	public JsonBoss(int startHealth, String displayName, String mobType, BossEquipmentJson equipment, List<ItemStackJsonAmountRange> drops, 
					int damagePerHit, String spawnItem, int bossId, int spawnChance, int xpDropped, String deathCommand, String deathMessage)
	{
		this.startHealth = startHealth;
		this.displayName = displayName;
		this.mobType = mobType;
		this.equipment = equipment;
		this.dropsJson = drops;
		this.damagePerHit = damagePerHit;
		this.spawnItem = spawnItem;
		this.bossId = bossId;
		this.spawnChance = spawnChance;
		this.xpDropped = xpDropped;
		this.deathCommand = deathCommand;
		this.deathMessage = deathMessage;
	}
	
	public Boss createBoss()
	{
		// Take the armor from a String to actual armor, then add enchants
		List<ItemStackJson> armorJson = equipment.getArmor();		
		ItemStack[] armor = new ItemStack[4];
		
		for(int i = 0; i < armorJson.size(); i++)
		{
			ItemStackJson json = armorJson.get(i);
			armor[i] = new ItemStack(Material.valueOf(json.getMaterial()));
			for(String s : json.getEnchants().keySet())
			{
				Enchantment enchant = Enchantment.getByName(s);
				if(enchant == null)
				{
					Reference.getPlugin().getLogger().info("Error: enchantment '" + s + "' is not a valid enchantment");
					Reference.getPlugin().getLogger().info("See this list (https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html) for a full list of bukkit enchantments (middle column)");
					continue;
				}
				armor[i].addUnsafeEnchantment(enchant, json.getEnchants().get(s));
			}
		}
		
		// Create the item held in the hand from the material name and add enchants
		ItemStackJson handJson = equipment.getHand();
		ItemStack hand = new ItemStack(Material.valueOf(handJson.getMaterial()), 1);
		for(String s : handJson.getEnchants().keySet())
		{
			hand.addUnsafeEnchantment(Enchantment.getByName(s), handJson.getEnchants().get(s));
		}
		
		// Create the drops it will have
		Map<ItemStack, Vector2<Integer, Integer>> dropsComplete = new HashMap<>();
				
		for(ItemStackJsonAmountRange dropJson : dropsJson)
		{
			ItemStack item = new ItemStack(Material.valueOf(dropJson.getMaterial()));
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(dropJson.getDisplay());
			im.setLore(dropJson.getLore());
			item.setItemMeta(im);
			
			Vector2<Integer, Integer> dropRange = dropJson.getRange();
			
			Map<String, Integer> enchants = dropJson.getEnchants();
			for(String s : enchants.keySet())
			{
				item.addUnsafeEnchantment(Enchantment.getByName(s.toUpperCase()), enchants.get(s));
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
		
		return new Boss(startHealth, EntityType.valueOf(mobType), displayName, hand, armor, dropsComplete, damagePerHit, spawnItem, bossId, spawnChance, xpDropped, deathCommand, deathMessage);
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
		ItemStackJson hjson = new ItemStackJson(hand.getType().name(), hand.getItemMeta().getDisplayName(), hand.getItemMeta().getLore(), enchantsJson);
		
		List<ItemStackJson> ajson = new ArrayList<>();
		for(int i = 0; i < 4; i++)
		{
			ItemStack piece = b.getArmor(i);
			Map<Enchantment, Integer> armorEnchants = piece.getEnchantments();
			Map<String, Integer> armorEnchantsJson = new HashMap<>(); 
			for(Enchantment k : armorEnchants.keySet())
			{
				armorEnchantsJson.put(k.getName(), armorEnchants.get(k));
			}
			ajson.add(new ItemStackJson(piece.getType().name(), piece.getItemMeta().getDisplayName(), piece.getItemMeta().getLore(), armorEnchantsJson));
		}
		
		BossEquipmentJson ejson = new BossEquipmentJson(ajson, hjson);
		
		List<ItemStackJsonAmountRange> drops = new ArrayList<>();
		Map<ItemStack, Vector2<Integer, Integer>> bossDrops = b.getDrops();
		
		for(ItemStack is : bossDrops.keySet())
		{
			drops.add(new ItemStackJsonAmountRange(is.getType().name(), is.getItemMeta().getDisplayName(), is.getItemMeta().getLore(), 
						PluginJsonParser.serializeEnchants(is.getEnchantments()), bossDrops.get(is)));
		}
		
		JsonBoss jsonBoss = new JsonBoss(startHealth, displayName, mobType, ejson, drops, b.getDamagePerHit(), b.getSpawnItem().getType().name(), b.getId(), b.getSpawnChance(), b.getXpDropped(), b.getDeathCommand(), b.getDeathMessage());
		
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
	
	public List<ItemStackJsonAmountRange> getDrops() { return dropsJson; }
	public void setDrops(List<ItemStackJsonAmountRange> drops) { this.dropsJson = drops; }
	
	public int getSpawnChance() { return this.spawnChance; }
	public void setSpawnChance(int chance) { this.spawnChance = chance; }
}
