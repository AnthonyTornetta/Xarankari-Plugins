package com.cornchipss.custombosses.boss.json.equipment;

import java.util.List;
import java.util.Map;

public class ArmorJson
{
	private String material, display;
	private List<String> lore, flags;
	private Map<String, Integer> enchants;
	
	public ArmorJson(String material, String display, List<String> lore, Map<String, Integer> enchants, List<String> flags) 
	{
		this.material = material;
		this.display = display;
		this.lore = lore;
		this.enchants = enchants;
		this.flags = flags;
	}
	
	@Override
	public String toString()
	{
		return "ArmorJson [" + getDisplay() + "; Material: " + getMaterial() + "; Lore: " + getLore() + "; Enchants: " + getEnchants() + "; Flags: " + getFlags() + "]";
	}
	
	public String getMaterial() { return material; }
	public void setMaterial(String material) { this.material = material; }

	public String getDisplay() { return display; }
	public void setDisplay(String display) { this.display = display; }

	public List<String> getLore() { return lore; }
	public void setLore(List<String> lore) { this.lore = lore; }

	public Map<String, Integer> getEnchants() { return enchants; }
	public void setEnchants(Map<String, Integer> enchants) { this.enchants = enchants; }

	public List<String> getFlags() { return flags; }
	public void setFlags(List<String> flags) { this.flags = flags; }
}
