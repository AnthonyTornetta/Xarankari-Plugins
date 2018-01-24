package com.cornchipss.custombosses.boss.json.equipment;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.cornchipss.custombosses.util.Vector2;
import com.cornchipss.custombosses.util.json.PluginJsonParser;

public class ItemStackJsonAmountRange extends ItemStackJson
{
	Vector2<Integer, Integer> range;
	
	public ItemStackJsonAmountRange(String material, String display, List<String> lore, Map<String, Integer> enchants, Vector2<Integer, Integer> range) 
	{
		super(material, display, lore, enchants);
		this.range = range;
	}
	
	public static ItemStackJsonAmountRange fromData(ItemStack itemStack, Vector2<Integer, Integer> range)
	{
		ItemStackJsonAmountRange thing = new ItemStackJsonAmountRange(itemStack.getType().name(), itemStack.getItemMeta().getDisplayName(), itemStack.getItemMeta().getLore(), PluginJsonParser.serializeEnchants(itemStack.getEnchantments()), range);
		return thing;
	}
	
	public Vector2<Integer, Integer> getRange() { return range; }
	public void setRange(Vector2<Integer, Integer> range) { this.range = range; }
}
