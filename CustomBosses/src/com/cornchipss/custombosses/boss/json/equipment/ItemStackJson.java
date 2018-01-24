package com.cornchipss.custombosses.boss.json.equipment;

import java.util.List;
import java.util.Map;

public class ItemStackJson
{
	private String material, display;
	private List<String> lore;
	private Map<String, Integer> enchants;
	
	public ItemStackJson(String material, String display, List<String> lore, Map<String, Integer> enchants) 
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
	
	public String getMaterial() { return material; }
	public void setMaterial(String material) { this.material = material; }

	public String getDisplay() { return display; }
	public void setDisplay(String display) { this.display = display; }

	public List<String> getLore() { return lore; }
	public void setLore(List<String> lore) { this.lore = lore; }

	public Map<String, Integer> getEnchants() { return enchants; }
	public void setEnchants(Map<String, Integer> enchants) { this.enchants = enchants; }
}
