package com.cornchipss.custombosses.listener;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.custombosses.CustomBosses;
import com.cornchipss.custombosses.boss.Boss;

public class CornyListener implements Listener
{
	private CustomBosses plugin;
	private List<Boss> possibleBosses;
	
	public CornyListener(CustomBosses plugin)
	{
		this.plugin = plugin;
		
		possibleBosses = plugin.getBosses();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void playerInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		ItemStack itemHeld = e.getPlayer().getItemInHand();
	}
}
