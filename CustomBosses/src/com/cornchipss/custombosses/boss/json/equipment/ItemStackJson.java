package com.cornchipss.custombosses.boss.json.equipment;

import java.util.List;
import java.util.Map;

public class ItemStackJson
{
	/*
	 * Storage containers for the information of the ItemStack
	 */
	private String material, display;
	private List<String> lore;
	private Map<String, Integer> enchants;
	
	/**
	 * A nice way of putting an ItemStack into JSON or visa versa
	 * @param material The String name of the material the ItemStack is
	 * @param display The display name of the ItemStack
	 * @param lore The lore of the ItemStack
	 * @param enchants The enchantments as their name and level
	 */
	public ItemStackJson(final String material, final String display, final List<String> lore, final Map<String, Integer> enchants) 
	{
		this.material = material;
		this.display = display;
		this.lore = lore;
		this.enchants = enchants;
	}
	
	@Override
	public String toString()
	{
		return "ArmorJson [" + getDisplay() + "; Material: " + getMaterial() + "; Lore: " + getLore() + "; Enchants: " + getEnchants() + "]";
	}
	
	// Getters & Setters //
	
	public String getMaterial() { return material; }
	public void setMaterial(String material) { this.material = material; }

	public String getDisplay() { return display; }
	public void setDisplay(String display) { this.display = display; }

	public List<String> getLore() { return lore; }
	public void setLore(List<String> lore) { this.lore = lore; }

	public Map<String, Integer> getEnchants() { return enchants; }
	public void setEnchants(Map<String, Integer> enchants) { this.enchants = enchants; }
}
