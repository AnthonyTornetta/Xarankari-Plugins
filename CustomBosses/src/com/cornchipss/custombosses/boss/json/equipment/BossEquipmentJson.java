package com.cornchipss.custombosses.boss.json.equipment;

import java.util.List;

public class BossEquipmentJson 
{
	/*
	 * Equipment storage containers
	 */
	private List<ItemStackJson> armor;
	private ItemStackJson hand;
	
	/**
	 * Just a holder class for easier JSON conversion - holds armor and hand equipment
	 * @param armor The armor it should store
	 * @param hand The hand equipment it should store (main hand only atm)
	 */
	public BossEquipmentJson(List<ItemStackJson> armor, ItemStackJson hand) 
	{
		this.armor = armor;
		this.hand = hand;
	}
	
	@Override
	public String toString()
	{
		return "Equipment [" + getArmor() + "; " + getHand() + "]";
	}
	
	// Getters & Setters //
	
	public List<ItemStackJson> getArmor() { return armor; }
	public void setArmor(List<ItemStackJson> armor) { this.armor = armor; }

	public ItemStackJson getHand() { return hand; }
	public void setHand(ItemStackJson hand) { this.hand = hand; }
}
