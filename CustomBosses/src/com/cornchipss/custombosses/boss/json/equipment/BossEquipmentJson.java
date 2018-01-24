package com.cornchipss.custombosses.boss.json.equipment;

import java.util.List;

public class BossEquipmentJson 
{
	private List<ItemStackJson> armor;
	private ItemStackJson hand;
	
	public BossEquipmentJson(List<ItemStackJson> armor, ItemStackJson hand) 
	{
		this.armor = armor;
		this.hand = hand;
	}

	public List<ItemStackJson> getArmor() { return armor; }
	public void setArmor(List<ItemStackJson> armor) { this.armor = armor; }

	public ItemStackJson getHand() { return hand; }
	public void setHand(ItemStackJson hand) { this.hand = hand; }
	
	@Override
	public String toString()
	{
		return "Equipment [" + getArmor() + "; " + getHand() + "]";
	}
}
