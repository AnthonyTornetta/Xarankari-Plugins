package com.cornchipss.custombosses.boss.json.equipment;

import java.util.List;

public class BossEquipmentJson 
{
	List<ArmorJson> armor;
	HandJson hand;
	
	public BossEquipmentJson(List<ArmorJson> armor, HandJson hand) 
	{
		this.armor = armor;
		this.hand = hand;
	}

	public List<ArmorJson> getArmor() { return armor; }
	public void setArmor(List<ArmorJson> armor) { this.armor = armor; }

	public HandJson getHand() { return hand; }
	public void setHand(HandJson hand) { this.hand = hand; }
}
