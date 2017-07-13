package com.cornchipss.main.helpers;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.main.Main;
import com.cornchipss.main.items.Items;

public class OnTick implements Runnable
{
	Main main;
	public OnTick(Main main)
	{
		this.main = main;
	}
	
	@Override
	public void run()
	{
		// Used for taking mana from players whenever they are using something that requires mana whenever it is active
		for(int i = 0; i < main.getServer().getOnlinePlayers().size(); i++)
		{
			Player p = (Player) main.getServer().getOnlinePlayers().toArray()[i];
			if(p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() == Material.DIAMOND_HOE)
			{
				ItemStack item = p.getInventory().getItemInOffHand();
				if(LoreParser.getId(item) == Items.MAGIC_HOLDER_ID)
				{
					if(p.getAllowFlight() && p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR)
					{
						if(!LoreParser.takeMana(5, p)) // Its whatever it is * 20 for the amount per second
						{
							p.setFlying(false);
							p.setAllowFlight(false);
							p.setFallDistance(0);
						}
					}
				}
			}
		}
	}
}
