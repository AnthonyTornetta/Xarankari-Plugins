package com.cornchipss.main;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Effects 
{
	Main main;
	
	boolean lastOver = true;
	
	public Effects(Main main)
	{
		this.main = main;
	}
	
	/**
	 * NOT WORKING - DO NOT USE!!!!
	 * @param p Player that you want effected
	 */
	public void blockWalking(Player p)
	{
		if(lastOver)
		{
			Location loc = p.getLocation().subtract(0, 1, 0);
			if(!loc.getBlock().getType().equals(Material.AIR) && loc.add(0, 1, 0).getBlock().getType() == Material.AIR) // Don't want to walk on air, and make sure I dont do any perm damage
			{
				loc.subtract(0, 1, 0);
				Block block = loc.getBlock();
				Material oldMat = block.getType();
				Collection<ItemStack> oldDrops = block.getDrops();
				block.setType(Material.DIAMOND_BLOCK);
				block.getDrops().removeAll(block.getDrops());
				block.getDrops().addAll(oldDrops);
				lastOver = false;
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
		            @Override
		            public void run() {
		                loc.getBlock().setType(oldMat);
		                lastOver = true;
		            }
		        }, 100);
			}
		}
	}
}
