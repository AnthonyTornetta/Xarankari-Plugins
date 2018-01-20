package com.cornchipss.custombosses.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cornchipss.custombosses.Debug;
import com.cornchipss.custombosses.boss.Boss;
import com.cornchipss.custombosses.boss.LivingBoss;
import com.cornchipss.custombosses.boss.handler.BossHandler;
import com.cornchipss.custombosses.listener.events.BossDeathEvent;
import com.cornchipss.custombosses.listener.events.BossSpawnEvent;
import com.cornchipss.custombosses.util.Helper;
import com.cornchipss.custombosses.util.Reference;

public class CornyListener extends Debug implements Listener
{
	private BossHandler bossHandler;
	// Because in 1.9 there are two hands, and I only have access to one (1.8), I must ignore the second call because that's for the off hand
	// So I store a list of players that have called it and if they are in said list I ignore the event and remove them
	private List<Player> playerThatInteracted = new ArrayList<>();
	
	public CornyListener(BossHandler handler)
	{		
		this.bossHandler = handler;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void playerInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if(playerThatInteracted.contains(p))
		{
			playerThatInteracted.remove(p);
			return;
		}
		else
		{
			playerThatInteracted.add(p);
		}
		
		if(e.isCancelled())
			return;
		
		ItemStack itemHeld = p.getItemInHand();
		
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK && itemHeld != null)
			return;
				
		for(Boss b : bossHandler.getLoadedBosses())
		{
			ItemStack spawnItem = b.getSpawnItem();		
			
			if(Helper.equiv(itemHeld, spawnItem))
			{
				LivingBoss newBoss = b.createLivingBoss();
				BossSpawnEvent bossSpawnEvent = new BossSpawnEvent(newBoss);
				
				Bukkit.getPluginManager().callEvent(bossSpawnEvent);
				if(bossSpawnEvent.isCancelled())
					return;
				
				// Make sure to spawn it before adding it, because it needs the location to serialize it
				newBoss.spawn(e.getClickedBlock().getLocation().add(0.0, 1.0, 0.0));
				bossHandler.addLivingBoss(newBoss);
				break;
			}
		}
	}

	// For use with the command invenotries
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerClickInventory(InventoryClickEvent e)
	{
		Inventory i = e.getInventory();
		Player p = (Player)e.getWhoClicked();

		if((i.getName().equals(Reference.BOSS_EGG_MENU_NAME)))
		{
			e.setCancelled(true); // Don't want them taking my blocks >:(
			if(e.getCurrentItem() == null)
				return;
							
			if(e.getCurrentItem().getType() == Material.BARRIER)
				p.closeInventory();
			else
			{
				for(Boss b : bossHandler.getLoadedBosses())
				{
					if(b.getSpawnItem().equals(e.getCurrentItem()))
					{
						// TODO: Create a cool effect that there is an infinite amount of them ;)
//						int slot = e.getSlot();
//						e.setCancelled(false);
//						i.setItem(slot, e.getCurrentItem().clone());
						p.getInventory().addItem(e.getCurrentItem());
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void entityDeath(EntityDeathEvent e)
	{
		LivingEntity ent = e.getEntity(); // More of a DeadEntity now (ba dum tss)
		
		for(LivingBoss b : bossHandler.getLivingBosses())
		{
			if(b.getEntity().equals(ent))
			{
				BossDeathEvent bossDeathEvent = new BossDeathEvent(b);
				Bukkit.getPluginManager().callEvent(bossDeathEvent);
				
				e.getDrops().clear();
				
				for(ItemStack i : bossDeathEvent.getDrops())
				{
					e.getDrops().add(i);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void entityDamagedByEntity(EntityDamageByEntityEvent e)
	{
		Entity damager = e.getDamager();
		Entity thingCausingDamage = damager;
		
		if(damager instanceof Projectile)
		{
			Projectile projectile = (Projectile)damager;
			thingCausingDamage = (Entity)projectile.getShooter();
		}
		
		for(LivingBoss b : bossHandler.getLivingBosses())
		{
			if(b.getEntity().equals(thingCausingDamage))
			{
				if(b.getBoss().getDamagePerHit() >= 0)
				{
					e.setDamage(b.getBoss().getDamagePerHit());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void bossSpawn(BossSpawnEvent e)
	{
		LivingBoss boss = e.getLivingBoss();
		Bukkit.broadcastMessage(ChatColor.GOLD + "The boss " + boss.getBoss().getDisplayName() + ChatColor.GOLD + " has spawned!");
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void bossDeath(BossDeathEvent e)
	{
		LivingBoss boss = e.getLivingBoss();
		bossHandler.removeLivingBoss(boss);
	}
}
