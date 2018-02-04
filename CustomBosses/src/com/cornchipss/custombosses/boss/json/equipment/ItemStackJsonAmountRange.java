package com.cornchipss.custombosses.boss.json.equipment;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.cornchipss.custombosses.util.Vector2;
import com.cornchipss.custombosses.util.json.PluginJsonParser;

public class ItemStackJsonAmountRange extends ItemStackJson
{
	/*
	 * The drop-rate range of the item stack, from minimum range to maximum range
	 */
	Vector2<Integer, Integer> range;
	
	/**
	 * A nice way of putting an ItemStack into JSON or visa versa<br>
	 * It also stores the drop range of the ItemStack as a bonus
	 * @param material The String name of the material the ItemStack is
	 * @param display The display name of the ItemStack
	 * @param lore The lore of the ItemStack
	 * @param enchants The enchantments as their name and level
	 * @param range The range of the ItemStack to drop (min first, then max)
	 */
	public ItemStackJsonAmountRange(String material, String display, List<String> lore, Map<String, Integer> enchants, Vector2<Integer, Integer> range) 
	{
		super(material, display, lore, enchants);
		this.range = range;
	}
	
	/**
	 * Creates an instance of this from an ItemStack and the drop range
	 * @param itemStack The ItemStack to read data from
	 * @param range The drop rate range
	 * @return An instance of this create from an ItemStack and the drop range
	 */
	public static ItemStackJsonAmountRange fromData(final ItemStack itemStack, final Vector2<Integer, Integer> range)
	{
		ItemStackJsonAmountRange thing = new ItemStackJsonAmountRange(itemStack.getType().name(), itemStack.getItemMeta().getDisplayName(), itemStack.getItemMeta().getLore(), PluginJsonParser.serializeEnchants(itemStack.getEnchantments()), range.clone());
		return thing;
	}
	
	public Vector2<Integer, Integer> getRange() { return range; }
	public void setRange(Vector2<Integer, Integer> range) { this.range = range; }
}
